package com.finance.interceptor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Authority {
    // 默认验证
    AuthorityType value() default AuthorityType.Validate;

}
