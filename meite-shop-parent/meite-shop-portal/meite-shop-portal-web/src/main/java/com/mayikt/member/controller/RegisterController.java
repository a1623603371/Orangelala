package com.mayikt.member.controller;


import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseResponse;
import com.mayikt.member.controller.req.vo.RegisterVo;
import com.mayikt.member.feign.MemberRegisterServiceFeign;
import com.mayikt.member.input.dto.UserInpDTO;
import com.mayikt.base.BaseWebController;
import com.mayikt.bean.MeiteBeanUtils;
import com.mayikt.web.utils.RandomValidateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * @ProjectName: 注册请求控制器
 * @Package: com.mayikt.member.controller
 * @ClassName: RegisterController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/28 15:11
 * @Version: 1.0
 */
@Controller
public class RegisterController extends BaseWebController {
    /**
     * 跳转到注册页面
     */
    private static final String MB_REGISTER_FTL = "member/register";
    /**
     * 跳转到登陆页面页面
     */
    private static final String MB_LOGIN_FTL = "member/login";
    @Autowired
    private MemberRegisterServiceFeign memberRegisterServiceFeign;

    /**
     * 跳转到注册页面
     *
     * @return
     */
    @GetMapping("/register")
    public String getRegister() {
        return MB_REGISTER_FTL;
    }

    /**
     * 跳转到注册页面
     *
     * @return
     */
    @PostMapping("/register")
    public String postRegister(@ModelAttribute("registerVo") @Validated RegisterVo registerVo,
                               BindingResult bindingResult, Model model, HttpSession httpSession) {
        //1.接收表单参数，验证码创建对象接收 vo dto
        if (bindingResult.hasErrors()) {
            //如果参数有误
            //获取第1个错误
            String errrors = bindingResult.getFieldError().getDefaultMessage();
            setErrorMsg(model, errrors);
            return MB_REGISTER_FTL;
        }
        //验证图形验证码
        String graphicCode=registerVo.getGraphicCode();
        Boolean checkVerify=RandomValidateCodeUtil.checkVerify(graphicCode,httpSession);
        if (!checkVerify){
            setErrorMsg(model,"验证码错误");
            return MB_REGISTER_FTL;
        }
        //调用会员服务实现注册，将页面vo，转换成dto
        UserInpDTO userInpDTO=MeiteBeanUtils.voToDto(registerVo,UserInpDTO.class);
      BaseResponse<JSONObject> register=  memberRegisterServiceFeign.register(userInpDTO,registerVo.getRegistCode());
      if (!isSuccess(register)){
          setErrorMsg(model,register.getMsg());
          return MB_REGISTER_FTL;
      }


     return MB_LOGIN_FTL;
    }
}