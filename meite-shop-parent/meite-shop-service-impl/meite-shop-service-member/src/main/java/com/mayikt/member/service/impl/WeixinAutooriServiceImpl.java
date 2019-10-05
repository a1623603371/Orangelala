package com.mayikt.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayikt.bese.GenerateToken;
import com.mayikt.constants.Constants;
import com.mayikt.member.mapper.UserMapper;
import com.mayikt.member.mapper.entity.UserDO;
import com.mayikt.member.service.WeixinAuthoriService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.member.service.impl
 * @ClassName: WeixinAutooriServiceImpl
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/1 0:24
 * @Version: 1.0
 */
@RestController
public class WeixinAutooriServiceImpl extends BaseApiService<JSONObject> {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GenerateToken generateToken;

    /**
     * 根据微信的openId查询用户信息
     *
     * @param wxOpenId
     * @return
     */
 /*   @Override
    public BaseResponse<JSONObject> findByOpenId(String wxOpenId) {
       *//* if (StringUtils.isEmpty(wxOpenId)){
            return  setResultError("wxOpenId不能为空");
        }
        UserDO userDO=userMapper.findBywxOpenId(wxOpenId);
        if(userDO==null){
            return setResultError(Constants.HTTP_RES_CODE_NOTUSER_203,"未找到用户信息");
        }
        //3.查询到返回对应的toekn信息
        String keyPrefix=Constants.LOGIN_WX_OPENID+"WX_OPENID";
        Long userId=userDO.getUserId();
     String wxToken= generateToken.createToken(keyPrefix,userId+"");
        JSONObject data=new JSONObject();
        data.put("wxToken",wxToken);
        return setResultSuccess(data);*//*
    }*/
}
