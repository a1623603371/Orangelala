package com.mayikt.zuul.wrapper;


import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @ProjectName: 防止xss攻击
 * @Package: com.mayikt.zuul.wrapper
 * @ClassName: XxsAndSqlHttpServletRequestWrapper
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/1 16:05
 * @Version: 1.0
 */
@Component
public class XxsAndSqlHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XxsAndSqlHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (!StringUtils.isEmpty(value)) {
            value = StringEscapeUtils.escapeJava(value);
        }
        return value;
    }
}
