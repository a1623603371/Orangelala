package com.mayikt.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayikt.bese.GenerateToken;
import com.mayikt.constants.Constants;
import com.mayikt.core.transaction.RedisDataSoureceTransaction;
import com.mayikt.core.utils.MD5Util;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import com.mayikt.member.mapper.UserMapper;
import com.mayikt.member.mapper.UserTokenMapper;
import com.mayikt.member.mapper.entity.UserDO;
import com.mayikt.member.mapper.entity.UserTokenDo;
import com.mayikt.member.service.MemberLoginService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @ProjectName: 登入实现类
 * @Package: com.mayikt.member.service.impl
 * @ClassName: MemberLoginServiceImpl
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/26 20:30
 * @Version: 1.0
 */
@RestController
public class MemberLoginServiceImpl extends BaseApiService<JSONObject> implements MemberLoginService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GenerateToken generateToken;
    @Autowired
    private UserTokenMapper userTokenMapper;
    @Autowired
    private RedisDataSoureceTransaction redisDataSoureceTransaction;

    /**
     * 用户登陆接口
     *
     * @param userLoginInpDTO
     * @return
     */
    @Override
    public BaseResponse<JSONObject> login(@RequestBody UserLoginInpDTO userLoginInpDTO) {
        //1.验证参数
        String mobile = userLoginInpDTO.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("手机号不能为空");
        }
        String password = userLoginInpDTO.getPassword();
        if (StringUtils.isEmpty(password)) {
            return setResultError("密码不能weikong");
        }
        //2.对密码进行加密
        String newPassword = MD5Util.MD5(password);
        //3.使用手机号和密码进行查询用户是否存在
        UserDO userDO = userMapper.login(mobile, newPassword);
        if (userDO == null) {
            return setResultError("用户不存在");
        }
        //判断登入类型
        String loginType = userLoginInpDTO.getLoginType();
        if (StringUtils.isEmpty(loginType)) {
            return setResultError("登陆类型不能为空!");
        }
        if (!(loginType.equals(Constants.MEMBER_LOGIN_TYPE_ANDROID)
                || loginType.equals(Constants.MEMBER_LOGIN_TYPE_IOS)
                || loginType.equals(Constants.MEMBER_LOGIN_TYPE_PC))) {

            return setResultError("登陆类型出现错误!");
        }
        // 设备信息
        String deviceInfor = userLoginInpDTO.getDeviceInfor();
        if (StringUtils.isEmpty(deviceInfor)) {
            return setResultError("设备信息不能为空!");
        }

        TransactionStatus transactionStatus = null;
        try {
            //获取userid
            Long userId = userDO.getUserId();
            //根据userid和登入类型检查是否登入
            UserTokenDo userTokenDo = userTokenMapper.selectByUserIdAndLoginType(userId, loginType);
            //开启事务
            transactionStatus = redisDataSoureceTransaction.begin();
            if (userTokenDo != null) {
                //清除之前的token
                String token = userTokenDo.getToken();
                Boolean isremoveToken = generateToken.removeToken(token);
                //在redis中存在token时，因为开启了事务isremoveToken redis中的数据么有删除 isremveToken会为fales
                // if (isremoveToken) {
                //把token的状态设置为1
           int    updateTokenAvailability=     userTokenMapper.updateTokenAvailability(token);
           if (!toDaoResult(updateTokenAvailability)){
               return  setResultError("系统错误");

           }
                // }
            }
            // openid关联用户账号信息
            String qqOpenId = userLoginInpDTO.getQqOpenId();
            if (!StringUtils.isEmpty(qqOpenId)) {
                userMapper.updateUserOpenId(qqOpenId, userId);
            }
            //插入新的token
            UserTokenDo newUserTokenDo = new UserTokenDo();
            newUserTokenDo.setUserId(userId);
            newUserTokenDo.setLoginType(userLoginInpDTO.getLoginType());
            //生成用户令牌存入redis
            String keyPrefix = Constants.MEMBER_TOKEN_KEYPREFIX + loginType;
            String userToen = generateToken.createToken(keyPrefix, userId + "");
            newUserTokenDo.setToken(userToen);
            newUserTokenDo.setDeviceInfor(deviceInfor);
            int result = userTokenMapper.insertUserToken(newUserTokenDo);
            //如果reslt大于0表示执行sql成功，反正执行失败，回滚事务
            if (!toDaoResult(result)) {
                //回滚事务
                redisDataSoureceTransaction.rollback(transactionStatus);
                return setResultError("系统错误");
            }
            JSONObject data = new JSONObject();
            data.put("token", userToen);
            redisDataSoureceTransaction.commit(transactionStatus);
            return setResultSuccess(data);
        } catch (Exception e) {
            try {
                //遇到运行时异常回滚事务
                redisDataSoureceTransaction.rollback(transactionStatus);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return setResultError("系统错误");

        }


    }
}
