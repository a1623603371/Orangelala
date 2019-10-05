package com.mayikt.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.base.BaseWebController;
import com.mayikt.bean.MeiteBeanUtils;
import com.mayikt.bese.BaseResponse;
import com.mayikt.member.feign.MemberExitServiceFeign;
import com.mayikt.member.feign.MemberServiceFeign;
import com.mayikt.member.input.dto.UserExitDTO;
import com.mayikt.member.output.dto.UserOutDTO;
import com.mayikt.web.constants.Constants;
import com.mayikt.web.utils.CookieUtils;
import com.xxl.sso.core.conf.Conf;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: 退出
 * @Package: com.mayikt.member.controller.req
 * @ClassName: ExitController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/29 10:39
 * @Version: 1.0
 */
@Controller
public class ExitController extends BaseWebController {


    /**
     * 重定向到首页
     */
    private static final String REDIRECT_INDEX = "redirect:/";

    /**
     * 跳转到登陆页面页面
     */
    private static final String MB_LOGIN_FTL = "member/login";
    @Autowired
    private MemberExitServiceFeign memberExitServiceFeign;
    @Autowired
    private MemberServiceFeign memberServiceFeign;

    @GetMapping("/exit")
    public String exit(Model model, HttpServletRequest request, HttpServletResponse response) {
        // 1.从cookie 中 获取 会员token
        String token = CookieUtils.getCookieValue(request, Conf.SSO_SESSIONID, true);
        if (!StringUtils.isEmpty(token)) {
            // 2.调用会员服务接口,查询会员用户信息
            BaseResponse<UserOutDTO> userInfo = memberServiceFeign.getInfo(token);
            if (!isSuccess(userInfo)) {
                return REDIRECT_INDEX;
            }
            UserOutDTO data = userInfo.getData();

            if (data != null) {
                String mobile = data.getMobile();
                UserExitDTO userExitDTO = new UserExitDTO();
                userExitDTO.setMobile(mobile);
                String info = webBrowserInfo(request);
                userExitDTO.setDeviceInfor(info);
                userExitDTO.setLoginType(com.mayikt.constants.Constants.MEMBER_LOGIN_TYPE_PC);
                //调用退出接口
                BaseResponse<JSONObject> exit = memberExitServiceFeign.exit(userExitDTO);
                if (!isSuccess(exit)) {
                    setErrorMsg(model, exit.getMsg());
                    return REDIRECT_INDEX;
                }
                //删除cookie中的值
                CookieUtils.deleteCookie(request,response, Constants.LOGIN_TOKEN_COOKIENAME);
                return MB_LOGIN_FTL;
            }
        }
            return REDIRECT_INDEX;
    }
}