package com.mayikt.pay.callback.template.unionpay;

import com.mayikt.pay.callback.template.AbstractPayCallbackTemplate;
import com.mayikt.pay.constant.PayConstant;
import com.mayikt.pay.mapper.PaymentTransactionMapper;
import com.mayikt.pay.mapper.entity.PaymentTransactionEntity;
import com.unionpay.acp.sdk.AcpService;
import com.unionpay.acp.sdk.LogUtil;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.UnionPayBase;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.pay.callback.template.unionpay
 * @ClassName: UnionPayCallbackTelmplate
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/19 20:45
 * @Version: 1.0
 */
@Component
public class UnionPayCallbackTelmplate extends AbstractPayCallbackTemplate {
    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    /**
     * 获取所有请求的参数，封装成map集合 并且验证是否被篡改
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public Map<String, String> verifySignature(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.writeLog("BackRcvResponse接收后台通知开始");
        String encoding = request.getParameter(SDKConstants.param_encoding);
        //获取银联通知服务器发送的后台通知参数
        Map<String, String> reqParm = getAllRequestParm(request);
        LogUtil.printRequestLog(reqParm);
        //重要！验证签名前不要修改reqParm中的键值对的内容，否则会验证不通过
        if (!AcpService.validate(reqParm, encoding)) {
            LogUtil.writeLog("验证签名结果【失败】");
            reqParm.put(PayConstant.RESULT_NAME, PayConstant.RESULT_PAYCODE_201);
        } else {
            LogUtil.writeLog("验证签名结果【成功】");
            //【注：为了安全验证签名成功才应该 写商户的成功处理逻辑】交易成功，更新商户订单状态
            String orderId = reqParm.get("orderId");
            reqParm.put("paymentId", orderId);
            reqParm.put(PayConstant.RESULT_NAME, PayConstant.RESULT_PAYCODE_200);
        }
        LogUtil.writeLog("BackRcvResponse接收后台通知结束");
        return reqParm;
    }
    /**
     * 异步回调中 网络尝试延时，导致异步回调重复操作，可能存在的幂等性问题
     * @param request
     * @return
     */
    @Override
    public String asyncService(Map<String, String> verifySignture) {
        String orderId=verifySignture.get("orderId");//获取后台通知数据，其他字段也可用类似方式获取
        String respCode=verifySignture.get("respCode");
        //判断respCode==00，A6后，对涉及资金的交易，请在发起查询接口查询，确认交易成功后更新数据库
        if (respCode.equals("00")|| respCode.equals("A6")){
            return failResult();
        }
        //根据日志手动补偿，使用支付id调用第三方支付接口
        PaymentTransactionEntity paymentTransaction=paymentTransactionMapper.selectByPaymentId(orderId);
        if (paymentTransaction.getPaymentStatus().equals(PayConstant.PAY_STATUS_SUCCESS)){
            //网络重试中，之前支付过
            return  successResult();

        }
        //2.将状态改为已经支付成功
        paymentTransactionMapper.updatePaymentStatus(PayConstant.PAY_STATUS_SUCCESS+"",orderId,"yinlian_pay");
        //3.调用积分服务接口增加积分（处理幂等性问题）
        return null;
    }

    @Override
    public String failResult() {
        return PayConstant.YINLIAN_RESULT_FAIL;
    }

    @Override
    public String successResult() {
        return PayConstant.YINLIAN_RESULT_SUCCESS;
    }

    /**
     * 获取请求参数中的所有的信息，当商户上送frontUrl或boackUrl地址中带有参数信息的时候，
     * 这种方式会将url地址中的参数读到map中，会导出来这些信息从而芝验签失败
     * 这个时候可以自行修改过滤掉url中的参数或者使用getAllRequestParamStream方法。
     * @param request
     * @return
     */
    private Map<String,String> getAllRequestParm(final HttpServletRequest request) {
        Map<String,String> res=new HashMap<>();
        Enumeration<?> temp=request.getParameterNames();
        if (null!=temp){
            while (temp.hasMoreElements()) {
                String en= (String) temp.nextElement();
                String value=request.getParameter(en);
                res.put(en,value);
                //在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有的 参数数据时，判断若值为空则删除这个字段
                if (res.get(en)==null||"".equals(res.get(en))){
                    res.remove(en);
                }

            }

        }
        return res;
    }
    /**
     * 获取请求参数中所有的信息。
     * 非struts可以改用此方法获取，好处是可以过滤掉request.getParameter方法过滤不掉的url中的参数。
     * struts可能对某些content-type会提前读取参数导致从inputstream读不到信息，所以可能用不了这个方法。
     * 理论应该可以调整struts配置使不影响，但请自己去研究。
     * 调用本方法之前不能调用req.getParameter("key");这种方法，否则会导致request取不到输入流。
     *
     * @param request
     * @return
     */
    public static Map<String, String> getAllRequestParamStream(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        try {
            String notifyStr = new String(IOUtils.toByteArray(request.getInputStream()), UnionPayBase.encoding);
            LogUtil.writeLog("收到通知报文：" + notifyStr);
            String[] kvs = notifyStr.split("&");
            for (String kv : kvs) {
                String[] tmp = kv.split("=");
                if (tmp.length >= 2) {
                    String key = tmp[0];
                    String value = URLDecoder.decode(tmp[1], UnionPayBase.encoding);
                    res.put(key, value);
                }
            }
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeLog("getAllRequestParamStream.UnsupportedEncodingException error: " + e.getClass() + ":"
                    + e.getMessage());
        } catch (IOException e) {
            LogUtil.writeLog("getAllRequestParamStream.IOException error: " + e.getClass() + ":" + e.getMessage());
        }
        return res;
    }
    /**
     * 回调机制 必须遵循规范 重试机制都是采用间隔新 错开的话 必须
     */
}
