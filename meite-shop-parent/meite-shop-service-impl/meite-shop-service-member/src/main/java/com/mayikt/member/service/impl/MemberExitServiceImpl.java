package com.mayikt.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayikt.bese.GenerateToken;
import com.mayikt.core.transaction.RedisDataSoureceTransaction;
import com.mayikt.core.utils.RedisUtil;
import com.mayikt.member.input.dto.UserExitDTO;
import com.mayikt.member.mapper.UserMapper;
import com.mayikt.member.mapper.UserTokenMapper;
import com.mayikt.member.mapper.entity.UserDO;
import com.mayikt.member.mapper.entity.UserTokenDo;
import com.mayikt.member.service.MemberExitService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.member.service.impl
 * @ClassName: MemberExitServiceImpl
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/29 10:57
 * @Version: 1.0
 */
@RestController
public class MemberExitServiceImpl extends BaseApiService<JSONObject> implements MemberExitService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GenerateToken generateToken;
    @Autowired
    private UserTokenMapper userTokenMapper;
    @Autowired
    private RedisDataSoureceTransaction redisDataSoureceTransaction;

    /**
     * 用户退出接口
     *
     * @param userExitDTO
     * @return
     */
    @Override
    public BaseResponse<JSONObject> exit(@RequestBody UserExitDTO userExitDTO) {
        //验证参数
        String mobile = userExitDTO.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("获取不到手机号");
        }
        String loginType = userExitDTO.getLoginType();
        if (StringUtils.isEmpty(loginType)) {
            return setResultError("获取登入类型");
        }
        String deviceInfor = userExitDTO.getDeviceInfor();
        if (StringUtils.isEmpty(deviceInfor)) {
            return setResultError("获取到设备信息");
        }
        //获取用户id
        UserDO userDO = userMapper.existMobile(mobile);
        if (userDO == null) {
            return setResultError("找不到该用户信息");
        }
        TransactionStatus transactionStatus = null;
        try {
           Long userId = userDO.getUserId();
            UserTokenDo userTokenDo = userTokenMapper.selectByUserIdAndLoginType(userId, userExitDTO.getLoginType());
            //开启事务
            transactionStatus = redisDataSoureceTransaction.begin();
            if (userTokenDo == null) {
                return  setResultError("系统错误111");
            }
            //清除之前的token
            String token = userTokenDo.getToken();
            generateToken.removeToken(token);
            //将token的状态设置为1
            userTokenMapper.updateTokenAvailability(token);
            redisDataSoureceTransaction.commit(transactionStatus);
            return setResultSuccess("退出成功");
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

