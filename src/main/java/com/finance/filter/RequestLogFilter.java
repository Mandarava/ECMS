package com.finance.filter;

import org.apache.commons.lang3.StringUtils;
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

import lombok.Getter;
import lombok.Setter;

@WebFilter(filterName = "requestLogFilter", urlPatterns = "/*", asyncSupported = true)
public class RequestLogFilter extends AbstractFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLogFilter.class);

    public void init(FilterConfig config) {

    }

    public void destroy() {

    }

    @Override
    public void doFilter(HttpServletRequest request,
                         HttpServletResponse response, FilterChain chain,
                         HttpSession session, String menthod, String url)
            throws IOException, ServletException {
        logger.info("拦截到请求:{} - {} : {}{}  {} Accept : {}   User-Agent :{}",
                getClientIpAddr(request).getTrueIp() + "/" + getClientIpAddr(request).getProxyIps(),
                menthod,
                url,
                getParamsString(request.getParameterMap()),
                response.getStatus(),
                request.getHeader("Accept"),
                request.getHeader("User-Agent"));
        chain.doFilter(request, response);
    }

    private String getParamsString(Map<String, String[]> params) {
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

    private ClientIps getClientIpAddr(HttpServletRequest request) {
        // 获取真实ip
        // 在反向代理(Nginx)上配置，增加Real-IP字段：location /{ ... proxy_set_header Real-IP $remote_addr; ...}
        String ip = request.getHeader("real-ip");
        if (StringUtils.isBlank(ip) || ("unknown".equalsIgnoreCase(ip.trim()))) {
            ip = request.getHeader("remote-host");
        }
        if (StringUtils.isBlank(ip) || ("unknown".equalsIgnoreCase(ip.trim()))) {
            ip = request.getRemoteAddr();
        }
        ClientIps clientIps = new ClientIps();
        clientIps.setTrueIp(StringUtils.trimToEmpty(ip));
        // 获取代理ip
        ip = request.getHeader("x-forwarded-for");
        StringBuilder proxyIps = new StringBuilder();

        if (StringUtils.isNotBlank(ip) && (StringUtils.contains(ip, ","))) {
            String temp = StringUtils.substringBeforeLast(ip, ",");
            if (StringUtils.isNotBlank(temp)) {
                proxyIps.append("x-forwarded-for:");
                proxyIps.append(temp);
                proxyIps.append("\n");
                clientIps.setProxyIps(proxyIps.toString());
            }
        }
        return clientIps;
    }
}

@Getter
@Setter
class ClientIps {
    private String trueIp;
    private String proxyIps;
}