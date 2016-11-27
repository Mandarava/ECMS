package com.finance.common;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by zt on 2016/11/27.
 */
@Component
@Aspect
public class DynamicDataSourceAspect {

    @Pointcut("execution (* com.finance.dao.*.select*(..)) || execution (* com.finance.dao.*.find*(..)) " +
            "|| execution (* com.finance.dao.*.count*(..)) || execution (* com.finance.dao.*.get*(..))")
    public void readMethodPointcut() {

    }

    @Pointcut("execution (* com.finance.dao.*.insert*(..)) || execution (* com.finance.dao.*.update*(..)) " +
            "|| execution (* com.finance.dao.*.delete*(..)) || execution (* com.finance.dao.*.add*(..)) " +
            "|| execution (* com.finance.dao.*.remove*(..)) || execution (* com.finance.dao.*.modify*(..)) " +
            "|| execution (* com.finance.dao.*.save*(..)) || execution (* com.finance.dao.*.batchInsert*(..))")
    public void writeMethodPointcut() {

    }

    @Before("readMethodPointcut()")
    public void switchReadDataSource() {
        // System.out.println("============切换到从读数据源===========");
        DataSourceContextHolder.setDataSourceType(DataSourceContextHolder.SLAVE_DATA_SOURCE);
    }

    @Before("writeMethodPointcut()")
    public void switchWriteDataSource() {
        // System.out.println("=============切换主写数据源==========");
        DataSourceContextHolder.setDataSourceType(DataSourceContextHolder.MASTER_DATA_SOURCE);
    }

}
