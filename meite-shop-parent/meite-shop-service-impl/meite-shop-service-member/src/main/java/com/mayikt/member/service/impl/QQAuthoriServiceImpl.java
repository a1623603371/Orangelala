package com.mayikt.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayikt.bese.GenerateToken;
import com.mayikt.constants.Constants;
import com.mayikt.member.input.dto.UserInpDTO;
import com.mayikt.member.mapper.UserMapper;
import com.mayikt.member.mapper.entity.UserDO;
import com.mayikt.member.service.QQAuthoriService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.member.service.impl
 * @ClassName: QQAuthoriServiceImpl
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/30 0:33
 * @Version: 1.0
 */
@RestController
public class QQAuthoriServiceImpl extends BaseApiService<JSONObject> implements QQAuthoriService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GenerateToken generateToken;
    /**
     * 根据 openid查询是否已经绑定,如果已经绑定，则直接实现自动登陆
     *
     * @param qqOpenId
     * @return
     */
    @Override
    public BaseResponse<JSONObject> findByOpenId(String qqOpenId) {
        //1.检测传入的openId是否为空
        if (StringUtils.isEmpty(qqOpenId)){
            return setResultError("qqOpenId不能为空");
        }
        //2.根据openId查询是否光联
        UserDO userDO=userMapper.findByOpenId(qqOpenId);
        if (userDO==null){
            return  setResultError(Constants.HTTP_RES_CODE_NOTUSER_203,"未找到关联QQ账号");
        }
        //3.查询到用户信息，返回对应的token信息
            String keyPrefix=Constants.LOGIN_QQ_OPENID+"QQ_LOGIN";
            Long userId=userDO.getUserId();
            String userToekn=generateToken.createToken(keyPrefix,userId+"");
             System.out.println(userToekn);
            JSONObject data=new JSONObject();
            data.put("token",userToekn);
        return setResultSuccess(data);
    }
}
