package com.coderman.service.xss;

import com.coderman.service.util.JsoupUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.stream.IntStream;

/**
 * @author coderman
 * @date 2022/8/7 19:35
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final HttpServletRequest orgRequest;
    private final boolean isIncludeRichText;

    XssHttpServletRequestWrapper(HttpServletRequest request, boolean isIncludeRichText) {
        super(request);
        orgRequest = request;
        this.isIncludeRichText = isIncludeRichText;
    }

    /**
     * 获取原始的 request的静态方法
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) req).getOrgRequest();
        }
        return req;
    }

    /**
     * 覆盖 getParameter方法，将参数名和参数值都做xss过滤
     * 如果需要获得原始的值，则通过 super.getParameterValues(name)来获取
     * getParameterNames,getParameterValues和 getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String name) {
        if (("content".equals(name) || name.endsWith("WithHtml")) && !isIncludeRichText) {
            return super.getParameter(name);
        }
        name = JsoupUtil.clean(name);
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            value = JsoupUtil.clean(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] arr = super.getParameterValues(name);
        if (arr != null) {
            IntStream.range(0, arr.length).forEach(i -> arr[i] = JsoupUtil.clean(arr[i]));
        }
        return arr;
    }

    /**
     * 覆盖getHeader方法，将参数名和参数值都做 xss过滤
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {
        name = JsoupUtil.clean(name);
        String value = super.getHeader(name);
        if (StringUtils.isNotBlank(value)) {
            value = JsoupUtil.clean(value);
        }
        return value;
    }

    /**
     * 获取原始的 request
     */
    private HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

}
