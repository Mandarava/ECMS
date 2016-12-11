package com.finance.model.pojo;

import java.io.Serializable;

/**
 * Created by zt on 2016/12/11.
 */
public class DataSourceDTO implements Serializable {

    private int id;

    private String name;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getTimeBetweenEvictionRunMills() {
        return timeBetweenEvictionRunMills;
    }

    public void setTimeBetweenEvictionRunMills(int timeBetweenEvictionRunMills) {
        this.timeBetweenEvictionRunMills = timeBetweenEvictionRunMills;
    }

    public int getMinEvictableIdleTimeMills() {
        return minEvictableIdleTimeMills;
    }

    public void setMinEvictableIdleTimeMills(int minEvictableIdleTimeMills) {
        this.minEvictableIdleTimeMills = minEvictableIdleTimeMills;
    }

    public String getTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(String testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public String getTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(String testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public String getTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(String testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public String getPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(String poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public int getMaxOpenPreparedStatements() {
        return maxOpenPreparedStatements;
    }

    public void setMaxOpenPreparedStatements(int maxOpenPreparedStatements) {
        this.maxOpenPreparedStatements = maxOpenPreparedStatements;
    }

    public String getLogAbandoned() {
        return logAbandoned;
    }

    public void setLogAbandoned(String logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

    public String getRemoveAbandoned() {
        return removeAbandoned;
    }

    public void setRemoveAbandoned(String removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    public int getRemoveAbandonedTimeout() {
        return removeAbandonedTimeout;
    }

    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }
}
