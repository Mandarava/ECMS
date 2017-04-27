package com.finance.util.myutil;

/**
 * Created by zt
 * 2017/4/27 22:52
 */
public class RequestContext {

    private static final ThreadLocal<String> localUserId = new ThreadLocal<>();

    public static String getCurrentUserId() {
        return localUserId.get();
    }

    public static void setCurrentUserId(String userId) {
        localUserId.set(userId);
    }

}
