package com.mayikt.weixin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayikt.constants.Constants;
import com.mayikt.core.utils.RedisUtil;
import com.mayikt.weixin.service.VerificaCodeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: 验证验证码接口
 * @Package: com.mayitk.weixin.service.impl
 * @ClassName: VerificaCodeServiceImpl
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/21 17:34
 * @Version: 1.0
 */
@RestController
public class VerificaCodeServiceImpl extends BaseApiService<JSONObject> implements VerificaCodeService {
    /**
     * 功能说明:根据手机号码验证码token是否正确
     *
     * @param phone
     * @param weixinCode
     * @return
     */
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public BaseResponse<JSONObject> verificaWeixinCode(String phone, String weixinCode) {
        //1.验证码和手机号是否为空
        if(StringUtils.isEmpty(phone)){
            return setResultError("手机不能为空");
        }
        if (StringUtils.isEmpty(weixinCode)){
            return  setResultError("验证码不能为空");
        }
        //2.根据手机号查询redis中返回对应的验证码
        String weixinCokeKey =Constants.WEIXINCODE_KEY +phone;
        String redisCode =redisUtil.getString(weixinCokeKey);
        if (StringUtils.isEmpty(redisCode)){
            return setResultError("验证码已过期");
        }
        //3.判断reids中的验证码和传统过来的参数是否相同
        if (!weixinCode.equals(redisCode)){
            return setResultError("验证码不正确");

        }
        //移除redis中的验证码
        redisUtil.delKey(weixinCokeKey);
        return setResultSuccess("验证码正确");
    }
}
