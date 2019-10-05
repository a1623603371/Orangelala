package com.mayikt.pay.callback.service;

import com.mayikt.pay.callback.template.AbstractPayCallbackTemplate;
import com.mayikt.pay.callback.template.factory.TemplateFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName:
 * @Package: com.mayikt.pay.callback.service
 * @ClassName: PayAsynCallbackService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/19 16:43
 * @Version: 1.0
 */
@RestController
public class PayAsynCallbackService {
    private static final String UNIONPAYCALLBACK_TEMPLATE="unionPayCallBackTemplate";
    /***
     * 银联异步回调接口执行代码
     */@RequestMapping("/unionPayCallBack")
    public String unionPayCallBack(HttpServletRequest request, HttpServletResponse response){
        AbstractPayCallbackTemplate abstractPayCallbackTemplate= TemplateFactory.getPayCallbackTemplate(UNIONPAYCALLBACK_TEMPLATE);
            return abstractPayCallbackTemplate.asyncCallBack(request,response);
    }
}
