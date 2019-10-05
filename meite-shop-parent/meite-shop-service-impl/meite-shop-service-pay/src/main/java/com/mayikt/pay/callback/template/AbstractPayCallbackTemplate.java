package com.mayikt.pay.callback.template;

import com.mayikt.pay.constant.PayConstant;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ProjectName: 使用模板方法重构异步回调代码
 * @Package: com.mayikt.pay.callback.template
 * @ClassName: AbstractPayCallbackTemplate
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/19 16:56
 * @Version: 1.0
 */
@Slf4j
public abstract class AbstractPayCallbackTemplate {
    /**
     * 获取所有请求的参数，封装成map集合 并且验证是否被篡改
     * @return
     */
    public abstract Map<String,String> verifySignature(HttpServletRequest request, HttpServletResponse response);

    /**
     * 异步回调执行业务逻辑
     * @param verifySignture
     * @return
     */
    public abstract String asyncService(Map<String,String> verifySignture);

    public abstract String failResult();

    public abstract String successResult();

    /**
     *1.将报文数据放到es
     * 2.验证报文参数
     *
     * 3.将日志根据支付id存放到数据库中
     *4.执行异步回调业务逻辑
     * @param request
     * @param response
     * @return
     */
    public String  asyncCallBack(HttpServletRequest request, HttpServletResponse response){
        //1.验证报文参数 相同点 获取所有的请求参数封装成map集合 ，并且进行参数验证
        Map<String,String> verifySignature=verifySignature(request,response);
        //2.将日志根据支付id存储到数据库|
        String paymentId=verifySignature.get("paymentId");
        //3.采用异步形式的写入日志数据库 中
        payLong(paymentId,verifySignature);
        String result=verifySignature.get(PayConstant.RESULT_NAME);
        //4.201报文验签失败
        if (result.equals(PayConstant.RESULT_PAYCODE_201)){
            return failResult();
        }
        return  asyncService(verifySignature);
    }

    /**
     * 采用多线程或MQ
     * @param paymentId
     * @param verifySignature
     */
    private void payLong(String paymentId, Map<String, String> verifySignature) {
        log.info(">>paymentId:{paymentId},verifySignature:{}", verifySignature);
    }

}
