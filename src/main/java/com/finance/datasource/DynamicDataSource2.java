package com.finance.datasource;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource2 extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getCustomerType();
    }

}
