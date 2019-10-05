package com.mayikt.zuul.handler.impl;

import com.mayikt.sign.SignUtil;
import com.mayikt.zuul.handler.BaseHandler;
import com.mayikt.zuul.handler.GatewayHandler;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.zuul.handler.impl
 * @ClassName: ToVeifyMapHandler
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/1 16:47
 * @Version: 1.0
 */
@Slf4j
@Component
public class ToVeifyMapHandler extends BaseHandler implements GatewayHandler {
    /**
     * 参数验证
     *
     * @param ctx
     * @param ipAddres
     * @param request
     * @param response
     */
    @Override
    public Boolean service(RequestContext ctx, String ipAddres, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>>>>拦截3 参数验证<<<<<<<<<<<<<<<<<<<<<<<<");
        Map<String,String> verifyMap= SignUtil.toVerifyMap(request.getParameterMap(),false);
        if (!SignUtil.verify(verifyMap)) {
            resultError(ctx, "ip:" + ipAddres + ",Sign fail");
            return Boolean.FALSE;

        }
        return Boolean.TRUE;
    }
}
