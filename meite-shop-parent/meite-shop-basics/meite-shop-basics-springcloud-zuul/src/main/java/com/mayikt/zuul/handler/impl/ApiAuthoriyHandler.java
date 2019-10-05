package com.mayikt.zuul.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseResponse;
import com.mayikt.zuul.feign.AuthorizationServiceFeign;
import com.mayikt.zuul.handler.BaseHandler;
import com.mayikt.zuul.handler.GatewayHandler;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.zuul.handler.impl
 * @ClassName: ApiAuthoriyHandler
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/1 16:20
 * @Version: 1.0
 */
@Slf4j
@Component
public class ApiAuthoriyHandler extends BaseHandler implements GatewayHandler {
    @Autowired
    private AuthorizationServiceFeign authorizationServiceFeign;
    /**
     * 网关拦截处理请求
     *
     * @param ctx
     * @param ipAddres
     * @param request
     * @param response
     */
    @Override
    public Boolean service(RequestContext ctx, String ipAddres, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>>>>拦截2 开放Api接口 Token验证 ipAddres:{}<<<<<<<<<<<<<<<<<<<<<<<<<<", ipAddres);
        String servletPath=request.getServletPath();
        if (!servletPath.substring(0,7).equals("public")) {
            //传提到下一个handler
            getNextHandler().service(ctx, ipAddres, request, response);
            return true;
        }
        String accessToken=request.getParameter("accessToken");
        log.info(">>>>>accessToken验证:" + accessToken);
        if (StringUtils.isEmpty(accessToken)){
            resultError(ctx, "AccessToken cannot be empty");
            return false;
        }
        //调用接口验证accessToken是否生效
        BaseResponse<JSONObject> appInfo=authorizationServiceFeign.getAppInfo(accessToken);
        log.info(">>>>>>data:" + appInfo.toString());
        if (!isSuccess(appInfo)){
            resultError(ctx,appInfo.getMsg());
            return Boolean.FALSE;
        }
        //传替到下一个
        getNextHandler().service(ctx, ipAddres, request, response);
        return Boolean.TRUE;

        }
    }

