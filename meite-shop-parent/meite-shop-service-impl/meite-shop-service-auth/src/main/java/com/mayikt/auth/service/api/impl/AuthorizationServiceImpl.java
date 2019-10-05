package com.mayikt.auth.service.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.auth.mapper.AppInfoMapper;
import com.mayikt.auth.mapper.entity.MeiteAppInfo;
import com.mayikt.auth.serivce.api.AuthorizationService;
import com.mayikt.auth.utils.Guid;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayikt.bese.GenerateToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.auth.service.api.impl
 * @ClassName: AuthorizationServiceImpl
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/1 15:06
 * @Version: 1.0
 */
@RestController
public class AuthorizationServiceImpl extends BaseApiService<JSONObject> implements AuthorizationService{
    @Autowired

    private AppInfoMapper appInfoMapper;
    @Autowired
    private GenerateToken generateToken;
    /**
     * 机构申请appid，appsecret
     *
     * @param appName
     */
    @Override
    public BaseResponse<JSONObject> applyAppInfo(String appName) {
        //验证参数
        if (StringUtils.isEmpty(appName)){
            return setResultError("机构名称不能为空!");
        }
        //生成appid，和appsecret
        Guid guid=new Guid();
        String appid=guid.getAppId();
        String appsecret=guid.getAppScrect();
        //添加到数据库
        MeiteAppInfo meiteAppInfo=new MeiteAppInfo(appName,appid,appsecret);
        int insertAppInfo=appInfoMapper.insertAppInfo(meiteAppInfo);
        if (insertAppInfo<0){
            return setResultError("申请失败");
        }
        //返回客户端
        JSONObject data=new JSONObject();
        data.put("appid",appid);
        data.put("appSecret",appsecret);
        return setResultSuccess(data);
    }

    /**
     * 使用appid于appsectet获取AccessToken
     *
     * @param appid
     * @param appsecret
     */
    @Override
    public BaseResponse<JSONObject> getAccessToken(String appid, String appsecret) {
        if (StringUtils.isEmpty(appid)){
            return setResultError("appid不能为空");
        }
        if (StringUtils.isEmpty(appsecret)){
            return setResultError("appsecret不能为空");
        }
        //使用appid和APPsectet查询数据库
        MeiteAppInfo meiteAppInfo=appInfoMapper.selectByAppInfo(appid,appsecret);
        if (meiteAppInfo==null){
            return setResultError("appid或秘钥错误");
        }
        //获取应用机构信息，生成accessToken
        String dbAppId=meiteAppInfo.getAppId();
        String accessToken=generateToken.createToken("auth",dbAppId);
        JSONObject data=new JSONObject();
        data.put("accessTken",accessToken);
        return setResultSuccess1(data);
    }

    /**
     * 验证token是否失效
     *
     * @param accessToken
     */
    @Override
    public BaseResponse<JSONObject> getAppInfo(String accessToken) {
        if (StringUtils.isEmpty(accessToken)){
            return setResultError("AccessToken cannot be empty ");
        }
        //从redis中查询
        String appid=generateToken.getToken(accessToken);
        if (StringUtils.isEmpty(appid)){
            return setResultError("accessToken  invalid");
        }
        //从数据库查询
        MeiteAppInfo meiteAppInfo=appInfoMapper.findByAppInfo(appid);
        if (meiteAppInfo==null){
            return setResultError("accessToken  invalid");
        }
        JSONObject data=new JSONObject();
        data.put("appInfo",meiteAppInfo);
        return setResultSuccess(data);
    }
}
