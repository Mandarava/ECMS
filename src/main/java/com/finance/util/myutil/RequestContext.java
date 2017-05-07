package com.finance.util.myutil;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Created by zt
 * 2017/4/27 22:52
 */
public class RequestContext {

    private static final ThreadLocal<String> localUserId = new ThreadLocal<>();

    public static String getCurrentUserId() {
//        return localUserId.get();
        return (String) RequestContextHolder.getRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_SESSION);
    }

    public static void setCurrentUserId(String userId) {
        localUserId.set(userId);
    }

}
