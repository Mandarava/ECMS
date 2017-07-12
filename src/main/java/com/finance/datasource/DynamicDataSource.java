package com.finance.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.finance.model.pojo.DataSourceDO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by zt on 2016/12/11.
 */
public class DynamicDataSource extends AbstractRoutingDataSource implements ApplicationContextAware {

    private static final String DEFAULT_TARGET_DATA_SOURCE = CustomerType.MASTER.getName();
    private static final Logger log = LoggerFactory.getLogger(DynamicDataSource.class);
    private ApplicationContext ac;
    private Map<Object, Object> _targetDataSources;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }

    /**
     * @describe 数据源为空或者为master时，自动切换至默认数据源，即在配置文件中定义的dataSource数据源
     * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceName = DynamicDataSourceContextHolder.getCustomerType();
        if (dataSourceName == null) {
            dataSourceName = DEFAULT_TARGET_DATA_SOURCE;
        } else {
            this.selectDataSource(dataSourceName);
            if (dataSourceName.equals(DEFAULT_TARGET_DATA_SOURCE)) {
                dataSourceName = DEFAULT_TARGET_DATA_SOURCE;
            }
        }
        log.debug("--------> use datasource " + dataSourceName);
        return dataSourceName;
    }

    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        this._targetDataSources = targetDataSources;
        super.setTargetDataSources(this._targetDataSources);
        super.afterPropertiesSet();
    }

    /**
     * @describe 数据源存在时不做处理，不存在时创建新的数据源链接，并将新数据链接添加至缓存
     */
    public void selectDataSource(String serverName) {
        Object sid = DynamicDataSourceContextHolder.getCustomerType();
        if (DEFAULT_TARGET_DATA_SOURCE.equals(serverName)) {
            DynamicDataSourceContextHolder.setCustomerType(DEFAULT_TARGET_DATA_SOURCE);
            return;
        }
        Object obj = this._targetDataSources.get(serverName);
        if (obj != null && sid.equals(serverName)) {
            logger.info(String.format("已找到[SourceName=%s]对应的数据源!", serverName));
            return;
        } else {
            logger.info(String.format("未找到[SourceName=%s]对应的数据源，从数据库重新获取...", serverName));
            DruidDataSource druidDataSource = this.getDataSource(serverName);
            if (null != druidDataSource) {
                this.setDataSource(serverName, druidDataSource);
            } else {
                throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + serverName + "]");
            }
        }
    }

    /**
     * @describe 查询serverName对应的数据源记录
     */
    public DruidDataSource getDataSource(String serverName) {
        this.selectDataSource(DEFAULT_TARGET_DATA_SOURCE);
        this.determineCurrentLookupKey();
        DataSourceDO dataSources = this.findDataSources(serverName);
        if (null == dataSources) {
            logger.error(String.format("从数据库重新获取，未找到[SourceName=%s]对应的数据源", serverName));
        } else if (!dataSources.getState().equals("1")) {
            logger.error(String.format("[SourceName=%s]对应的数据源状态未开启", serverName));
        } else {
            DruidDataSource dataSource = this.createDataSource(dataSources);
            return dataSource;
        }
        return null;
    }

    public DataSourceDO findDataSources(String serverName) {
        Connection conn = null;
        DataSourceDO result = null;
        PreparedStatement ps = null;
        try {
            conn = this.getConnection();
            String sql = "SELECT * FROM data_source WHERE NAME = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, serverName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DataSourceDO dataSourceDO = new DataSourceDO();
                dataSourceDO.setDriverClassName(rs.getString("DRIVER_CLASS_NAME"));
                dataSourceDO.setId(rs.getInt("ID"));
                dataSourceDO.setState(rs.getString("STATE"));
                dataSourceDO.setSourceType(rs.getString("SOURCE_TYPE"));
                dataSourceDO.setName(rs.getString("NAME"));
                dataSourceDO.setUrl(rs.getString("URL"));
                dataSourceDO.setUserName(rs.getString("USERNAME"));
                dataSourceDO.setPassword(rs.getString("PASSWORD"));
                dataSourceDO.setMaxActive(rs.getInt("MAX_ACTIVE"));
                dataSourceDO.setInitialSize(rs.getInt("INITIAL_SIZE"));
                dataSourceDO.setMaxWait(rs.getInt("MAX_WAIT"));
                dataSourceDO.setMinIdle(rs.getInt("MIN_IDLE"));
                dataSourceDO.setTimeBetweenEvictionRunMills(rs.getInt("TIME_BETWEEN_EVICTION_RUN_MILLS"));
                dataSourceDO.setMinEvictableIdleTimeMills(rs.getInt("MIN_EVICTABLE_IDLE_TIME_MILLS"));
                dataSourceDO.setTestOnBorrow(rs.getString("TEST_ON_BORROW"));
                dataSourceDO.setTestOnReturn(rs.getString("TEST_ON_RETURN"));
                dataSourceDO.setTestWhileIdle(rs.getString("TEST_WHILE_IDLE"));
                dataSourceDO.setValidationQuery(rs.getString("VALIDATION_QUERY"));
                dataSourceDO.setPoolPreparedStatements(rs.getString("POOL_PREPARED_STATEMENTS"));
                dataSourceDO.setMaxOpenPreparedStatements(rs.getInt("MAX_OPEN_PREPARED_STATEMENTS"));
                dataSourceDO.setLogAbandoned(rs.getString("LOG_ABANDONED"));
                dataSourceDO.setRemoveAbandoned(rs.getString("REMOVE_ABANDONED"));
                dataSourceDO.setRemoveAbandonedTimeout(rs.getInt("REMOVE_ABANDONED_TIMEOUT"));
                result = dataSourceDO;
            }
            rs.close();
        } catch (SQLException e) {
            logger.debug(e.getMessage(), e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                logger.debug(e.getMessage(), e);
            }
        }
        return result;
    }

    private DruidDataSource createDataSource(DataSourceDO dataSource) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(dataSource.getDriverClassName());
        druidDataSource.setUrl(dataSource.getUrl());
        druidDataSource.setUsername(dataSource.getUserName());
        druidDataSource.setPassword(dataSource.getPassword());
        druidDataSource.setInitialSize(dataSource.getInitialSize());
        druidDataSource.setMaxActive(dataSource.getMaxActive());
        druidDataSource.setMinIdle(dataSource.getMinIdle());
        druidDataSource.setMaxWait(dataSource.getMaxWait());
        druidDataSource.setTestOnBorrow(dataSource.getTestOnBorrow().trim().equals("true"));
        druidDataSource.setTestOnReturn(dataSource.getTestOnReturn().trim().equals("true"));
        druidDataSource.setTestWhileIdle(dataSource.getTestWhileIdle().trim().equals("true"));
        druidDataSource.setTimeBetweenEvictionRunsMillis(dataSource.getTimeBetweenEvictionRunMills());
        druidDataSource.setMinEvictableIdleTimeMillis(dataSource.getMinEvictableIdleTimeMills());
        druidDataSource.setRemoveAbandoned(dataSource.getRemoveAbandoned().trim().equals("true"));
        druidDataSource.setRemoveAbandonedTimeout(dataSource.getRemoveAbandonedTimeout());
        druidDataSource.setLogAbandoned(dataSource.getLogAbandoned().trim().equals("true"));

        return druidDataSource;
    }

    /**
     * @param serverName
     * @param dataSource
     */
    public void setDataSource(String serverName, DruidDataSource dataSource) {
        DefaultListableBeanFactory acf = (DefaultListableBeanFactory) ac.getAutowireCapableBeanFactory();
        acf.registerSingleton(serverName, dataSource);

        this._targetDataSources.put(serverName, dataSource);
        this.setTargetDataSources(this._targetDataSources);
        DynamicDataSourceContextHolder.setCustomerType(serverName);
    }

}