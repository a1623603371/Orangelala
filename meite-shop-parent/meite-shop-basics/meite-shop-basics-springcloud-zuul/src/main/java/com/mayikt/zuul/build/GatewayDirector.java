package com.mayikt.zuul.build;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.zuul.build
 * @ClassName: GatewayDirector
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/28 19:23
 * @Version: 1.0
 */
@Component
public class GatewayDirector {
    @Resource(name = "verificationBuild")
    private GatewayBuild gatewayBuild;

    public  void  director(RequestContext ctx, String ipAddres, HttpServletResponse response, HttpServletRequest request){
        /**
         * 黑名单拦截
         */
      Boolean boackBlock=  gatewayBuild.blackBlock(ctx,ipAddres,response);
        if (!boackBlock){
            return;
        }
        //参数验证
         Boolean verifyMap=gatewayBuild.toVerifyMap(ctx,ipAddres,request);
        if (!verifyMap){
            return;
        }

    }
}
