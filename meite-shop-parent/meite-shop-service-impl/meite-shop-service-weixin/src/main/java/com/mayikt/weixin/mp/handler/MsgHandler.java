package com.mayikt.weixin.mp.handler;
import com.mayikt.member.output.dto.UserOutDTO;
import com.mayikt.weixin.mp.builder.TextBuilder;
import com.mayikt.bese.BaseResponse;
import com.mayikt.constants.Constants;
import com.mayikt.core.utils.RedisUtil;
import com.mayikt.core.utils.RegexUtils;
import com.mayikt.weixin.feign.MemberServiceFeign;
import me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {
	/**
	 * 发送验证码消息
	 */
	@Value("${mayikt.weixin.registration.code.message}")
	private String registrationCodeMessage;
	/**
	 * 默认回复消息
	 */
	@Value("${mayikt.weixin.default.registration.code.message}")
	private String defaultRegistrationCodeMessage;

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private MemberServiceFeign memberServiceFeign;

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService,
			WxSessionManager sessionManager) {

		if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
			// TODO 可以选择将消息保存到本地
		}

		// 当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
		try {
			if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
					&& weixinService.getKefuService().kfOnlineList().getKfOnlineList().size() > 0) {
				return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE().fromUser(wxMessage.getToUser())
						.toUser(wxMessage.getFromUser()).build();
			}
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
		// 1.获取客户端发送的消息
		String fromContent = wxMessage.getContent();
		// 2.如果客户端发送消息为手机号码，则发送验证码
		if (RegexUtils.checkMobile(fromContent)) {
		    //根据手机号调用会员服务接口，查询手机是否存在
            BaseResponse<UserOutDTO> reusltUserInfo=memberServiceFeign.existMobile(fromContent);
            //根据状态码判断手机是否注册
            if (reusltUserInfo.getCode().equals(Constants.HTTP_RES_CODE_200)){
                return  new TextBuilder().build("该手机"+fromContent+"已被注册",wxMessage,weixinService);
            }
            if (!reusltUserInfo.getCode().equals(Constants.HTTP_RES_CODE_203)){
                return new TextBuilder().build(reusltUserInfo.getMsg(),wxMessage,weixinService);
            }
			// 3.生成随机四位注册码
			int registCode = registCode();
			String content = String.format(registrationCodeMessage, registCode);
			// 4.将验证码存放在Redis中
			redisUtil.setString(Constants.WEIXINCODE_KEY + fromContent, registCode + "", Constants.WEIXINCODE_TIMEOUT);
			return new TextBuilder().build(content, wxMessage, weixinService);
		}
		return new TextBuilder().build(defaultRegistrationCodeMessage, wxMessage, weixinService);
	}
	// 获取注册码
	private int registCode() {
		int registCode = (int) (Math.random() * 9000 + 1000);
		return registCode;
	}

}
