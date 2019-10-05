package com.mayikt.zuul.build;

import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: 网关行为建造者
 * @Package: com.mayikt.zuul.build
 * @ClassName: GatewayBuild
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/27 21:06
 * @Version: 1.0
 */
public interface GatewayBuild {
    /**
     * 黑名单拦截
     */
    Boolean blackBlock(RequestContext requestContext,String ipAddr, HttpServletResponse response);
    /**
     * 参数验证
     */
    Boolean toVerifyMap(RequestContext requestContext, String ipAddr, HttpServletRequest request);
    /**
     *
     */
}
