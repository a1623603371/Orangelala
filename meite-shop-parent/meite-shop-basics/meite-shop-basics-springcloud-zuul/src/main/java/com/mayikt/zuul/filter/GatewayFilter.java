package com.mayikt.zuul.filter;

import com.mayikt.zuul.build.GatewayDirector;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: 网关拦截
 * @Package: com.mayikt.zuul.filter
 * @ClassName: GatewayFilter
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/28 19:30
 * @Version: 1.0
 */
@Slf4j
@Component
public class GatewayFilter extends ZuulFilter {
    @Autowired
    private GatewayDirector gatewayDirector;

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext=RequestContext.getCurrentContext();
        HttpServletRequest request=requestContext.getRequest();;
        HttpServletResponse response=requestContext.getResponse();
        response.setContentType("UTF-8");
        //获取ip地址
        String ipAddres=getIpAddres(request);
        if (StringUtils.isEmpty(ipAddres)){
          resultError(requestContext,"未能够获取到ip地址");
        }
        gatewayDirector.director(requestContext,ipAddres,response,request);

        return null ;
    }
    private void resultError(RequestContext ctx, String errorMsg) {
        ctx.setResponseStatusCode(401);
        ctx.setSendZuulResponse(false);
        ctx.setResponseBody(errorMsg);
    }

    /**
     * 在方法拦截之前
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    public String getIpAddres(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
