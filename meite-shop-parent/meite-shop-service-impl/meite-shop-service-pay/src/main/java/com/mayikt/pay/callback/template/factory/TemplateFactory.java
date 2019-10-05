package com.mayikt.pay.callback.template.factory;

import com.mayikt.core.utils.SpringContextUtil;
import com.mayikt.pay.callback.template.AbstractPayCallbackTemplate;

/**
 * @ProjectName: 获取具体实现的模板方法
 * @Package: com.mayikt.pay.callback.template.factory
 * @ClassName: TemplateFactory
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/19 20:36
 * @Version: 1.0
 */

public class TemplateFactory {
    public static AbstractPayCallbackTemplate getPayCallbackTemplate(String beanId){
        return (AbstractPayCallbackTemplate) SpringContextUtil.getBean(beanId);
    }
}
