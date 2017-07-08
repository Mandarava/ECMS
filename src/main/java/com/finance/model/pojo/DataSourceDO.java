package com.finance.model.pojo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zt on 2016/12/11.
 */
@ToString
@Setter
@Getter
public class DataSourceDO implements Serializable {

    private int id;

    private String name;

    private String state;

    private String sourceType;

    private String driverClassName;

    private String url;

    private String userName;

    private String password;

    private int maxActive;

    private int initialSize;

    private int maxWait;

    private int minIdle;

    private int timeBetweenEvictionRunMills;

    private int minEvictableIdleTimeMills;

    private String testOnBorrow;

    private String testOnReturn;

    private String testWhileIdle;

    private String validationQuery;

    private String poolPreparedStatements;

    private int maxOpenPreparedStatements;

    private String logAbandoned;

    private String removeAbandoned;

    private int removeAbandonedTimeout;

}
