package com.finance.annotation;

import com.finance.common.DataSourceType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataSource {

    DataSourceType value() default DataSourceType.Master;

}  

