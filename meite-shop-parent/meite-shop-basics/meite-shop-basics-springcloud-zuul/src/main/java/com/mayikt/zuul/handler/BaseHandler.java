package com.mayikt.zuul.handler;

import com.mayikt.bese.BaseResponse;
import com.mayikt.constants.Constants;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.zuul.handler
 * @ClassName: BaseHandler
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/1 16:12
 * @Version: 1.0
 */
public class BaseHandler {
    @Autowired
    private GatewayHandler gatewayHandler;

    public void setNextHandler(GatewayHandler gatewayHandler) {
        this.gatewayHandler = gatewayHandler;
    }

    public GatewayHandler getNextHandler() {
        return gatewayHandler;
    }

    protected void resultError(RequestContext ctx, String errorMsg) {
        ctx.setResponseStatusCode(401);
        // 网关响应为false 不会转发服务
        ctx.setSendZuulResponse(false);
        ctx.setResponseBody(errorMsg);
    }

    // 接口直接返回true 或者false
    public Boolean isSuccess(BaseResponse<?> baseResp) {
        if (baseResp == null) {
            return false;
        }
        if (!baseResp.getCode().equals(Constants.HTTP_RES_CODE_200)) {
            return false;
        }
        return true;
    }
}
