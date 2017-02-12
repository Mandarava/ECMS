package com.finance.dao;


import com.finance.model.pojo.DataSourceDO;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zt on 2016/12/11.
 */
@Repository
public interface DataSourceDao {

    List<DataSourceDO> findDataSources();

    DataSourceDO findDataSourceById(String id);

}
