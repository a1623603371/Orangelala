package com.mayikt.member.controller;

import com.mayikt.member.feign.WXAuthoriServiceFeign;
import com.qq.connect.QQConnectException;
import com.qq.connect.oauth.Oauth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.member.controller
 * @ClassName: WeixinAuthoriController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/1 17:47
 * @Version: 1.0
 */
@Controller
@Slf4j
public class WeixinAuthoriController {
    @Autowired
    private WXAuthoriServiceFeign wxAuthoriServiceFeign;

 /*   @RequestMapping("/WeixinAuth")
    public String Weixinauth(HttpServletRequest request){
    }*/


}
