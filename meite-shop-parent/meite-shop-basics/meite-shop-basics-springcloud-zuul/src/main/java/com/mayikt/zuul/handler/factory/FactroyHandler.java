package com.mayikt.zuul.handler.factory;

import com.mayikt.core.utils.SpringContextUtil;
import com.mayikt.zuul.handler.GatewayHandler;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.zuul.handler.factory
 * @ClassName: FactroyHandler
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/1 16:52
 * @Version: 1.0
 */

public class FactroyHandler {
    public static GatewayHandler getHandler(){
// 1.黑名单拦截
        GatewayHandler handler1 = (GatewayHandler) SpringContextUtil.getBean("blacklistHandler");
        // 2.验证accessToken
        GatewayHandler handler2 = (GatewayHandler) SpringContextUtil.getBean("apiAuthorityHandler");
        handler1.setNextHandler(handler2);
        // 3.API接口参数接口验签
        GatewayHandler handler3 = (GatewayHandler) SpringContextUtil.getBean("toVerifyMapHandler");
        handler2.setNextHandler(handler3);
        return handler1;
    }
}
