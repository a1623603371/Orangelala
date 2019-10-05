package com.mayikt.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.core.bean.MiteBeanUtils;
import com.mayikt.member.mapper.entity.UserDO;
import com.mayikt.member.mapper.UserMapper;
import com.mayikt.member.feign.VerificaCodeServiceFeign;
import com.mayikt.member.input.dto.UserInpDTO;
import com.mayikt.member.service.MemberRegisterService;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayikt.constants.Constants;
import com.mayikt.core.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * @ProjectName: 会员注册接口
 * @Package: com.mayikt.member.service.impl
 * @ClassName: MemberRegisterServiceImpl
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/24 17:16
 * @Version: 1.0
 *
 */
@RestController
public class MemberRegisterServiceImpl extends BaseApiService<JSONObject> implements MemberRegisterService {
        @Autowired
        private UserMapper userMapper;
        @Autowired
        private VerificaCodeServiceFeign verificaCodeServiceFeign;
    /**
     * 用户注册接口
     *
     * @param userEntity
     * @param registCode
     * @return
     */
    @Override
    @Transactional
    public BaseResponse<JSONObject> register(@RequestBody UserInpDTO userInpDTO, String registCode) {
        //1.验证参数
        //2.调用接口查看验证码是否正确
        String phone=userInpDTO.getMobile();
      BaseResponse<JSONObject>  resultVerificaWeixinCode=verificaCodeServiceFeign.verificaWeixinCode(phone,registCode);
      //2验证码是否正确，会员调用微信实现验证码注册
      if (!resultVerificaWeixinCode.getCode().equals(Constants.HTTP_RES_CODE_200)){
                    return setResultError(resultVerificaWeixinCode.getMsg()) ;
      }
      //4.将密码用md5加密
        String oldPassword=userInpDTO.getPassword();
        String newPassord= MD5Util.MD5(oldPassword);
        userInpDTO.setPassword(newPassord);
        UserDO userDO=MiteBeanUtils.dtoToDo(userInpDTO,UserDO.class);
        //保存到数据库
        return userMapper.register( userDO)>0?setResultSuccess("注册成功"):setResultError("注册失败");
    }
}
