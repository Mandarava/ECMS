package com.finance.datasource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by zt on 2016/11/27.
 */
@Component
@Order(-1)// 保证该AOP在@Transactional之前执行
@Aspect
public class DynamicDataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    /*@Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, TargetDataSource ds) throws Throwable {
        String dsId = ds.name();
        if (!DynamicDataSourceContextHolder.containsDataSource(dsId)) {
            logger.error("数据源[{}]不存在，使用默认数据源 > {}", ds.name(), point.getSignature());
        } else {
            logger.debug("Use DataSource : {} > {}", ds.name(), point.getSignature());
            DynamicDataSourceContextHolder.setCustomerType(ds.name());
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
        logger.debug("Revert DataSource : {} > {}", ds.name(), point.getSignature());
        DynamicDataSourceContextHolder.clearCustomerType();
    }*/

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
        DynamicDataSourceContextHolder.setCustomerType(CustomerType.SLAVE.getName());
    }

    @Before("writeMethodPointcut()")
    public void switchWriteDataSource() {
        DynamicDataSourceContextHolder.setCustomerType(CustomerType.MASTER.getName());
    }

}
