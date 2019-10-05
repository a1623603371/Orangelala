package com.mayikt.pay.factory;

import com.mayikt.pay.strategy.PayStrategy;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.pay.factory
 * @ClassName: StrataegyFactory
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/13 21:38
 * @Version: 1.0
 */
public class StrataegyFactory {
    private static Map<String, PayStrategy> strataegyBean = new ConcurrentHashMap<>();

    public static PayStrategy getPayStrategy(String classAddres) {
        if (StringUtils.isEmpty(classAddres)) {
            return null;
        }
        PayStrategy beanPayStrategy = strataegyBean.get(classAddres);
        if (beanPayStrategy != null) {
            return beanPayStrategy;
        }
        //1.使用java反射机制初始化子类
        Class<?> formName = null;
        try {
            formName = Class.forName(classAddres);
            //2。使用反射机制初始化对象
            PayStrategy payStrategy = (PayStrategy) formName.newInstance();
            return payStrategy;
        } catch (Exception e) {
            return null;

        }
    }
}