package com.mayikt.zuul.handler.impl;

import com.mayikt.zuul.handler.BaseHandler;
import com.mayikt.zuul.handler.GatewayHandler;
import com.mayikt.zuul.mapper.BlacklistMapper;
import com.mayikt.zuul.mapper.entity.MeiteBlacklist;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: 黑名单Handler
 * @Package: com.mayikt.zuul.handler.impl
 * @ClassName: BlacklistHandler
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/1 16:42
 * @Version: 1.0
 */
@Component
@Slf4j
public class BlacklistHandler extends BaseHandler implements GatewayHandler {
    @Autowired
    private BlacklistMapper blacklistMapper;
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
        // >>>>>>>>>>>>>黑名单拦截操作<<<<<<<<<<<<<<<<<<<
        log.info(">>>>>>>>>拦截1 黑名单拦截 ipAddres:{}<<<<<<<<<<<<<<<<<<<<<<<<<<", ipAddres);
        MeiteBlacklist meiteBlacklist=blacklistMapper.findBlacklist(ipAddres);
        if (meiteBlacklist!=null)
        {
            resultError(ctx, "ip:" + ipAddres + ",Insufficient access rights");
            return Boolean.FALSE;
        }
        //传替给下一个handler
        getNextHandler().service(ctx,ipAddres,request,response);

        return null;
    }
}
