package com.mayikt.zuul.build.impl;

import com.mayikt.sign.SignUtil;
import com.mayikt.zuul.build.GatewayBuild;
import com.mayikt.zuul.mapper.BlacklistMapper;
import com.mayikt.zuul.mapper.entity.MeiteBlacklist;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.zuul.build.impl
 * @ClassName: VerificationBulid
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/27 21:22
 * @Version: 1.0
 */
@Slf4j
@Component
public class VerificationBuild implements GatewayBuild{
    @Autowired
    private BlacklistMapper blacklistMapper;
    /**
     * 黑名单拦截
     *
     * @param requestContext
     * @param ipAddres
     * @param response
     */
    @Override
    public Boolean blackBlock(RequestContext requestContext, String ipAddres, HttpServletResponse response) {
        //查询数据库黑名单
        MeiteBlacklist meiteBlacklist=blacklistMapper.findBlacklist(ipAddres);
        if (meiteBlacklist!=null){
            resultError(requestContext, "ip:" + ipAddres + ",Insufficient access rights");
            return false;
        }
        log.info(">>>>>>>ip:{},验证通过>>>>>>>",ipAddres);
        //将ip发送到服务器中
        response.addHeader("ipAddres",ipAddres);
        log.info(">>>>>通过");
        return true;
    }

    /**
     * 参数验证
     *
     * @param requestContext
     * @param ipAddr
     * @param request
     */
    @Override
    public Boolean toVerifyMap(RequestContext requestContext, String ipAddr, HttpServletRequest request) {
        //网络传替参数验证
        Map<String,String> verifvMap= SignUtil.toVerifyMap(request.getParameterMap(), false);
        if (!SignUtil.verify(verifvMap)) {
            resultError(requestContext, "ip:" + ipAddr + ",Sign fail");
            return false;
        }
        return true;
    }

    private void resultError(RequestContext ctx, String errorMsg) {
        ctx.setResponseStatusCode(401);
        ctx.setSendZuulResponse(false);
        ctx.setResponseBody(errorMsg);

    }
}
