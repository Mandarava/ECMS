package com.finance.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "requestLogFilter", urlPatterns = "/*", asyncSupported = true)
public class RequestLogFilter extends AbstractFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLogFilter.class);

    public static String getParamsString(Map<String, String[]> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        for (String key : params.keySet()) {
            builder.append(key).append("=").append(params.get(key)[0]).append("&");
        }
        builder.deleteCharAt(builder.lastIndexOf("&"));
        return builder.toString();
    }

    public void init(FilterConfig config) {

    }

    public void destroy() {

    }

    @Override
    public void doFilter(HttpServletRequest request,
                         HttpServletResponse response, FilterChain chain,
                         HttpSession session, String menthod, String url)
            throws IOException, ServletException {
        long before = System.currentTimeMillis();
        logger.info("Accept:{}", request.getHeader("Accept"));
        logger.info("Content-Type:{}", request.getHeader("Content-Type"));
        logger.info("User-Agent:{}", request.getHeader("User-Agent"));
        logger.info("拦截到请求:{} - {} : {}{}  {}", request.getRemoteAddr(), menthod, url, getParamsString(request.getParameterMap()), response.getStatus());

        chain.doFilter(request, response);
        long after = System.currentTimeMillis();
        logger.info("花费时间：{} ms\n", after - before);
    }

}