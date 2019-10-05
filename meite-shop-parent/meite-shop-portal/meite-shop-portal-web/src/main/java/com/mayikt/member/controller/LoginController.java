package com.mayikt.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseResponse;


import com.mayikt.constants.Constants;
import com.mayikt.member.controller.req.vo.LoginVo;
import com.mayikt.member.feign.MemberLoginServiceFeign;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import com.mayikt.member.service.MemberLoginService;
import com.mayikt.base.BaseWebController;
import com.mayikt.bean.MeiteBeanUtils;
import com.mayikt.web.utils.CookieUtils;
import com.mayikt.web.utils.RandomValidateCodeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ProjectName: 登入请求控制器
 * @Package: com.mayikt.member.controller
 * @ClassName: LoginController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/28 15:10
 * @Version: 1.0
 */
@Controller
public class LoginController extends BaseWebController{
    /**
     * 跳转到登陆页面页面
     */
    private static final String MB_LOGIN_FTL = "member/login";
    @Autowired
    private MemberLoginServiceFeign memberLoginServiceFeign;
    /**
     * 重定向到首页
     */
    private static final String REDIRECT_INDEX = "redirect:/";

    /**
     * 登入页面
     *
     * @return
     */
    @GetMapping("/login")
    public String getLogin() {
        return MB_LOGIN_FTL;
    }

    /**
     * 登录
     *
     * @return
     */
    @PostMapping("/login")
    public String postLogin(@ModelAttribute("loginVo")LoginVo loginVo, BindingResult bindingResult,
                            Model model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        //验证图形验证码
        String graphicCode =loginVo.getGraphicCode();
        if (!RandomValidateCodeUtil.checkVerify(graphicCode,httpSession)){
            setErrorMsg(model,"验证码不正确");
            return MB_LOGIN_FTL;
        }
        //将vo转成dto调用登入接口
        UserLoginInpDTO  userLoginInpDTO= MeiteBeanUtils.voToDto(loginVo,UserLoginInpDTO.class);
        String info=webBrowserInfo( request);
        userLoginInpDTO.setLoginType(Constants.MEMBER_LOGIN_TYPE_PC);
        userLoginInpDTO.setDeviceInfor(info);
        BaseResponse<JSONObject> longin=memberLoginServiceFeign.login(userLoginInpDTO);
        if (!isSuccess(longin)){
           setErrorMsg(model,longin.getMsg());
           return MB_LOGIN_FTL;
        }
        //登入成功后，保持会话，把token存入Cooke中
        JSONObject data=longin.getData();
        String token=data.getString("token");
        CookieUtils.setCookie(request,response, com.mayikt.web.constants.Constants.LOGIN_TOKEN_COOKIENAME,token);

        return REDIRECT_INDEX;
    }

}
