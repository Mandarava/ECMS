package com.finance.common;

import com.finance.annotation.DataSource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;


public class DataSourceAspect {

    //方法执行前 注解
    public void before(JoinPoint point) {
        Object target = point.getTarget();
        String method = point.getSignature().getName();
        Class<?>[] classz = target.getClass().getInterfaces();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Class<?>[] parameterTypes = methodSignature.getMethod().getParameterTypes();
        try {
            Method m = classz[0].getMethod(method, parameterTypes);
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                DataSource data = m.getAnnotation(DataSource.class);
                DataSourceContextHolder.clearDataSourceType();
                DataSourceContextHolder.setDataSourceType(data.value().getName());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
