package com.mayikt.zuul.handler;

import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: 网关处理接口
 * @Package: com.mayikt.zuul.handler
 * @ClassName: GatewayHandler
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/1 16:10
 * @Version: 1.0
 */
public interface GatewayHandler {
    /**
     * 网关拦截处理请求
     */
    Boolean service(RequestContext ctx, String ipAddres, HttpServletRequest request, HttpServletResponse response);

    /**
     * 设置下一个
     */
    void setNextHandler(GatewayHandler gatewayHandler);

    /**
     * 获取下一个Handler
     *
     * @return
     */
    public GatewayHandler getNextHandler();
}
