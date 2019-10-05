package com.mayikt.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.base.BaseWebController;
import com.mayikt.bean.MeiteBeanUtils;
import com.mayikt.bese.BaseResponse;
import com.mayikt.member.controller.req.vo.LoginVo;
import com.mayikt.member.feign.MemberLoginServiceFeign;
import com.mayikt.member.feign.QQAuthoriServiceFeign;

import com.mayikt.member.input.dto.UserLoginInpDTO;
import com.mayikt.web.constants.Constants;
import com.mayikt.web.utils.CookieUtils;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.WeakHashMap;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.member.controller
 * @ClassName: QQAuthoriController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/30 0:24
 * @Version: 1.0
 */
@Controller
@Slf4j
public class QQAuthoriController extends BaseWebController {

    private static final String MB_QQ_QQLOGIN = "member/qqlogin";
    @Autowired
    private MemberLoginServiceFeign memberLoginServiceFeign;
    /**
     * 重定向到首页
     */
    private static final String REDIRECT_INDEX = "redirect:/";

    @Autowired
    private QQAuthoriServiceFeign qqAuthoriServiceFeign;

        /**
         * QQ授权回调地址
         * @param request
         * @return
         */
         @RequestMapping("/qqAuth")
        public String QQauth(HttpServletRequest request){
             try {
              String authrizeURL=new Oauth().getAuthorizeURL(request);
                log.info(authrizeURL);
                return "redirect:"+authrizeURL;
            } catch (QQConnectException e) {

             return  REDIRECT_INDEX;
            }
        }

    /**
     * qq自动登入
     * @param request
     * @param response
     * @param session
     * @return
     */
        @RequestMapping("/qqLoginBack")
        public String qqLoginBack(HttpServletRequest request,HttpServletResponse response,HttpSession session) {
            try {
                //使用A授权码获取AccessToken
                AccessToken accessTokenObj=new Oauth().getAccessTokenByRequest(request);
                System.out.println(accessTokenObj);
                if (accessTokenObj==null){
                    return ERROR_500_FTL;
                }
                String accsstoken=accessTokenObj.getAccessToken();
                System.out.println("****"+accsstoken+"*****");
                if (StringUtils.isEmpty(accsstoken)){
                    return ERROR_500_FTL;
                }
                //使用Accesstoken获取用openId
                OpenID openIDObj=new OpenID(accsstoken);
                String openId=openIDObj.getUserOpenID();
                if (StringUtils.isEmpty(openId)){
                    return ERROR_500_FTL;
                }
                //使用openid查询用户关联qq信息
                BaseResponse<JSONObject> findByOpenId=qqAuthoriServiceFeign.findByOpenId(openId);
                if (!isSuccess(findByOpenId)){
                    return ERROR_500_FTL;
                }
                //获取code
                Integer code= findByOpenId.getCode();
                //如果使用oenId没有查询到用户跳转到绑定页面
                if (code.equals(com.mayikt.constants.Constants.HTTP_RES_CODE_NOTUSER_203)){
                    //页面需要展示的QQ头像
                    UserInfo qzoneserInfo=new UserInfo(accsstoken,openId);
                    UserInfoBean userInfoBean=qzoneserInfo.getUserInfo();
                    if (userInfoBean== null){
                        return ERROR_500_FTL;
                    }
                    String avatrURL100=userInfoBean.getAvatar().getAvatarURL100();
                        //返回用户头像展示
                    request.setAttribute("avatrURL100",avatrURL100);
                    //将openid存入session中
                    session.setAttribute(com.mayikt.constants.Constants.LOGIN_QQ_OPENID,openId);
                    return MB_QQ_QQLOGIN;
                }
                //查询到用户信息直接登入
                //自动实现登录
                JSONObject data=findByOpenId.getData();
                String token=data.getString("token");
                CookieUtils.setCookie(request,response,Constants.LOGIN_TOKEN_COOKIENAME,token);
                return  REDIRECT_INDEX;


            } catch (QQConnectException e) {
                e.printStackTrace();
                return ERROR_500_FTL;
            }
        }


    /**回调接口无法获取accsstoken
     * QQ绑定
     * @param loginVo
     * @param model
     * @param request
     * @param response
     * @param session
     * @return
     */
        @RequestMapping("/qqJointLogin")
        public String qqJointLogin(@ModelAttribute("loginVo") LoginVo loginVo, Model model,
                                   HttpServletRequest request, HttpServletResponse response,HttpSession session){

            //获取openid
            String qqOpenId= (String) session.getAttribute(com.mayikt.constants.Constants.LOGIN_QQ_OPENID);
            if (StringUtils.isEmpty(qqOpenId)){
                return ERROR_500_FTL;
            }
            //将vo转成dto
            UserLoginInpDTO userLoginInpDTO= MeiteBeanUtils.voToDto(loginVo,UserLoginInpDTO.class);
            userLoginInpDTO.setQqOpenId(qqOpenId);
            userLoginInpDTO.setLoginType(com.mayikt.constants.Constants.MEMBER_LOGIN_TYPE_PC);
            String info =webBrowserInfo(request);
            userLoginInpDTO.setDeviceInfor(info);
            BaseResponse<JSONObject>login=memberLoginServiceFeign.login(userLoginInpDTO);
            if (!isSuccess(login)){
                setErrorMsg(model,login.getMsg());
                return  ERROR_500_FTL;
            }
           //3.登录成功后，将token存入cooike，首页读取cookice查询token显示到页面
            JSONObject data=login.getData();
            String token=data.getString("token");
            CookieUtils.setCookie(request,response,Constants.LOGIN_TOKEN_COOKIENAME,token);
            return REDIRECT_INDEX;
        }



}
