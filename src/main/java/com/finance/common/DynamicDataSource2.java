package com.finance.common;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource2 extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getCustomerType();
    }

}
