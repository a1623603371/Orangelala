package com.xxl.sso.server.controller;
import com.mayikt.base.BaseWebController;
import com.mayikt.bese.BaseResponse;
import com.mayikt.bese.GenerateToken;
import com.mayikt.constants.Constants;
import com.mayikt.core.transaction.RedisDataSoureceTransaction;
import com.mayikt.core.utils.RedisUtil;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import com.mayikt.member.output.dto.UserOutDTO;
import com.xxl.sso.core.conf.Conf;
import com.xxl.sso.core.login.SsoWebLoginHelper;
import com.xxl.sso.core.store.SsoLoginStore;

import com.xxl.sso.core.user.XxlSsoUser;
import com.xxl.sso.core.util.JedisUtil;
import com.xxl.sso.server.feign.MemberServiceFeign;
import com.xxl.sso.core.store.SsoSessionIdHelper;
import com.xxl.sso.server.mapper.UserTokenMapper;
import com.xxl.sso.server.mapper.entity.UserTokenDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * sso server (for web)
 *
 * @author xuxueli 2017-08-01 21:39:47
 */
@Controller
public class WebController extends BaseWebController {
	@Autowired
	private MemberServiceFeign memberServiceFeign;
	@Autowired
	private UserTokenMapper userTokenMapper;
	@Autowired
    private GenerateToken generateToken;
     @Autowired
	private RedisDataSoureceTransaction redisDataSoureceTransaction;
	@RequestMapping("/")
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {

		// login check
		XxlSsoUser xxlUser = SsoWebLoginHelper.loginCheck(request, response);

		if (xxlUser == null) {
			return "redirect:/login";
		} else {
			model.addAttribute("xxlUser", xxlUser);
			return "index";
		}
	}

	/**
	 * Login page
	 *
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(Conf.SSO_LOGIN)
	public String login(Model model, HttpServletRequest request, HttpServletResponse response) {

		// login check
		XxlSsoUser xxlUser = SsoWebLoginHelper.loginCheck(request, response);

		if (xxlUser != null) {

			// success redirect
			String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
			if (redirectUrl != null && redirectUrl.trim().length() > 0) {

				String sessionId = SsoWebLoginHelper.getSessionIdByCookie(request);
				String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSIONID + "=" + sessionId;


				return "redirect:" + redirectUrlFinal;
			} else {
				return "redirect:/";
			}
		}

		model.addAttribute("errorMsg", request.getParameter("errorMsg"));
		model.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
		return "login";
	}

	/**
	 * Login
	 *
	 * @param request
	 * @param redirectAttributes
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping("/doLogin")
	public String doLogin(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes, String mobile, String password, String ifRemember) {
        System.out.println(request.getContextPath());
        System.out.println(request.getRequestURI());

		boolean ifRem = (ifRemember != null && "on".equals(ifRemember)) ? true : false;

		// valid login 调用会员服务进行验证
		// ReturnT<UserInfo> result = userService.findUser(username, password);
		// if (result.getCode() != ReturnT.SUCCESS_CODE) {
		// redirectAttributes.addAttribute("errorMsg", result.getMsg());
		//
		// redirectAttributes.addAttribute(Conf.REDIRECT_URL,
		// request.getParameter(Conf.REDIRECT_URL));
		// return "redirect:/login";
		// }
		// >>>>>>>认证授权中心调用会员服务接口进行验证
		UserLoginInpDTO userLoginInpDTO = new UserLoginInpDTO();
		userLoginInpDTO.setLoginType(Constants.MEMBER_LOGIN_TYPE_PC);
		userLoginInpDTO.setMobile(mobile);
		userLoginInpDTO.setPassword(password);
		String info = webBrowserInfo(request);
		userLoginInpDTO.setDeviceInfor(info);
		if (userLoginInpDTO==null){
            System.out.println("null");
        }
		BaseResponse<UserOutDTO> ssoLogin = memberServiceFeign.ssoLogin(userLoginInpDTO);
		if (!isSuccess(ssoLogin)) {
			redirectAttributes.addAttribute("errorMsg", ssoLogin.getMsg());
			redirectAttributes.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
			return "redirect:/login";
		}
		UserOutDTO data = ssoLogin.getData();
		if (data == null) {
			redirectAttributes.addAttribute("errorMsg", "没有获取用户信息");
			redirectAttributes.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
			return "redirect:/login";
		}
		XxlSsoUser xxlUser = new XxlSsoUser();
		xxlUser.setUserid(String.valueOf(data.getUserId()));
		xxlUser.setUsername(data.getUserName());
		xxlUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
		xxlUser.setExpireMinite(SsoLoginStore.getRedisExpireMinite());
		xxlUser.setExpireFreshTime(System.currentTimeMillis());
		// 2、make session id
		String sessionId = SsoSessionIdHelper.makeSessionId(xxlUser);
        System.out.println("**********************"+sessionId+"*****************************");
       TransactionStatus transactionStatus=null;
        try {
            transactionStatus=redisDataSoureceTransaction.begin();
            //用户iD
            Long userId=data.getUserId();
            //登录类型
            String loginType=userLoginInpDTO.getLoginType();
            //根据用户id和登录类型查询
            UserTokenDo userTokenDo=userTokenMapper.selectByUserIdAndLoginType(userId,loginType);
            if (userTokenDo!=null){
                //获取sessionId
                String  redisKey= userTokenDo.getToken();
                //删除redis中token
                Boolean isremoveToken=generateToken.removeToken(redisKey);
               //获取token
                String token =userTokenDo.getToken();
                int updateTokenAvailability=userTokenMapper.updateTokenAvailability(token);
                if (updateTokenAvailability<0){
                    redirectAttributes.addAttribute("errorMsg", "没有获取用户信息");
                }

            }
            //插入新的token
            UserTokenDo newUserTokenDo=new UserTokenDo();
            newUserTokenDo.setUserId(userId);
            newUserTokenDo.setLoginType(loginType);
            newUserTokenDo.setToken(sessionId);
            String deviceInfor=userLoginInpDTO.getDeviceInfor();
            newUserTokenDo.setDeviceInfor(deviceInfor);
            int result=userTokenMapper.insertUserToken(newUserTokenDo);
            if (result<0){
                //回滚事务
                redisDataSoureceTransaction.rollback(transactionStatus);
                //回滚事务
                redirectAttributes.addAttribute("errorMsg", "系统错误");
            }
            redisDataSoureceTransaction.commit(transactionStatus);
        }catch (Exception e){
            try {
                //遇到运行时异常回滚事务
                redisDataSoureceTransaction.rollback(transactionStatus);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            redirectAttributes.addAttribute("errorMsg", "系统错误");

        }
        // 3、login, store storeKey + cookie sessionId
		SsoWebLoginHelper.login(response, sessionId, xxlUser, ifRem);




		// 4、return, redirect sessionId
		String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
		if (redirectUrl != null && redirectUrl.trim().length() > 0) {
			String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSIONID + "=" + sessionId;
			return "redirect:" + redirectUrlFinal;
		} else {
			return "redirect:/";
		}

	}

	/**
	 * Logout
	 *
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(Conf.SSO_LOGOUT)
	public String logout(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {

		// logout
		SsoWebLoginHelper.logout(request, response);

		redirectAttributes.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
		return "redirect:/login";
	}

}