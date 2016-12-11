package com.finance.common;

import com.alibaba.druid.pool.DruidDataSource;
import com.finance.model.pojo.DataSourceDTO;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zt on 2016/12/11.
 */
public class DynamicDataSource extends AbstractRoutingDataSource implements ApplicationContextAware {

    private static final String DEFAULT_TARGET_DATA_SOURCE = CustomerType.MASTER.getName();
    private Logger log = Logger.getLogger(this.getClass());
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
            if (dataSourceName.equals(DEFAULT_TARGET_DATA_SOURCE))
                dataSourceName = DEFAULT_TARGET_DATA_SOURCE;
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
            return;
        } else {
            DruidDataSource druidDataSource = this.getDataSource(serverName);
            if (null != druidDataSource)
                this.setDataSource(serverName, druidDataSource);
        }
    }

    /**
     * @describe 查询serverName对应的数据源记录
     */
    public DruidDataSource getDataSource(String serverName) {
        this.selectDataSource(DEFAULT_TARGET_DATA_SOURCE);
        this.determineCurrentLookupKey();
        List<DataSourceDTO> dataSources = this.findDataSources(serverName);
        if (null != dataSources && dataSources.size() > 0) {
            DruidDataSource dataSource = this.createDataSource(dataSources.get(0));
            return dataSource;
        }
        return null;
    }

    public List<DataSourceDTO> findDataSources(String serverName) {
        Connection conn = null;
        List<DataSourceDTO> result = new ArrayList<>();
        try {
            conn = this.getConnection();
            PreparedStatement ps = conn
                    .prepareStatement("SELECT * FROM data_source WHERE NAME = ?");
            ps.setString(1, serverName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DataSourceDTO dataSourceDTO = new DataSourceDTO();
                dataSourceDTO.setDriverClassName(rs.getString("DRIVER_CLASS_NAME"));
                dataSourceDTO.setId(rs.getInt("ID"));
                dataSourceDTO.setName(rs.getString("NAME"));
                dataSourceDTO.setUrl(rs.getString("URL"));
                dataSourceDTO.setUserName(rs.getString("USERNAME"));
                dataSourceDTO.setPassword(rs.getString("PASSWORD"));
                dataSourceDTO.setMaxActive(rs.getInt("MAX_ACTIVE"));
                dataSourceDTO.setInitialSize(rs.getInt("INITIAL_SIZE"));
                dataSourceDTO.setMaxWait(rs.getInt("MAX_WAIT"));
                dataSourceDTO.setMinIdle(rs.getInt("MIN_IDLE"));
                dataSourceDTO.setTimeBetweenEvictionRunMills(rs.getInt("TIME_BETWEEN_EVICTION_RUN_MILLS"));
                dataSourceDTO.setMinEvictableIdleTimeMills(rs.getInt("MIN_EVICTABLE_IDLE_TIME_MILLS"));
                dataSourceDTO.setTestOnBorrow(rs.getString("TEST_ON_BORROW"));
                dataSourceDTO.setTestOnReturn(rs.getString("TEST_ON_RETURN"));
                dataSourceDTO.setTestWhileIdle(rs.getString("TEST_WHILE_IDLE"));
                dataSourceDTO.setValidationQuery(rs.getString("VALIDATION_QUERY"));
                dataSourceDTO.setPoolPreparedStatements(rs.getString("POOL_PREPARED_STATEMENTS"));
                dataSourceDTO.setMaxOpenPreparedStatements(rs.getInt("MAX_OPEN_PREPARED_STATEMENTS"));
                dataSourceDTO.setLogAbandoned(rs.getString("LOG_ABANDONED"));
                dataSourceDTO.setRemoveAbandoned(rs.getString("REMOVE_ABANDONED"));
                dataSourceDTO.setRemoveAbandonedTimeout(rs.getInt("REMOVE_ABANDONED_TIMEOUT"));
                result.add(dataSourceDTO);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            logger.debug(e.getMessage(), e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.debug(e.getMessage(), e);
            }
        }
        return result;
    }

    private DruidDataSource createDataSource(DataSourceDTO dataSource) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(dataSource.getDriverClassName());
        druidDataSource.setUrl(dataSource.getUrl());
        druidDataSource.setUsername(dataSource.getUserName());
        druidDataSource.setPassword(dataSource.getPassword());
        druidDataSource.setInitialSize(dataSource.getInitialSize());
        druidDataSource.setMaxActive(dataSource.getMaxActive());
        druidDataSource.setMinIdle(dataSource.getMinIdle());
        druidDataSource.setMaxWait(dataSource.getMaxWait());
        druidDataSource.setTestOnBorrow(dataSource.getTestOnBorrow().trim().equals("true") ? true : false);
        druidDataSource.setTestOnReturn(dataSource.getTestOnReturn().trim().equals("true") ? true : false);
        druidDataSource.setTestWhileIdle(dataSource.getTestWhileIdle().trim().equals("true") ? true : false);
        druidDataSource.setTimeBetweenEvictionRunsMillis(dataSource.getTimeBetweenEvictionRunMills());
        druidDataSource.setMinEvictableIdleTimeMillis(dataSource.getMinEvictableIdleTimeMills());
        druidDataSource.setRemoveAbandoned(dataSource.getRemoveAbandoned().trim().equals("true") ? true : false);
        druidDataSource.setRemoveAbandonedTimeout(dataSource.getRemoveAbandonedTimeout());
        druidDataSource.setLogAbandoned(dataSource.getLogAbandoned().trim().equals("true") ? true : false);

        return druidDataSource;
    }

    /**
     * @param serverName
     * @param dataSource
     */
    public void setDataSource(String serverName, DruidDataSource dataSource) {
        this._targetDataSources.put(serverName, dataSource);
        DefaultListableBeanFactory acf = (DefaultListableBeanFactory) ac.getAutowireCapableBeanFactory();
        acf.registerSingleton(serverName, dataSource);
        this.setTargetDataSources(this._targetDataSources);
        DynamicDataSourceContextHolder.setCustomerType(serverName);
    }

}