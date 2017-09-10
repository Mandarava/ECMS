package com.finance.service;

import com.finance.datasource.TargetDataSource;
import com.finance.model.pojo.FundNetDO;

import java.util.List;

/**
 * Created by zt on 2016/12/10.
 */
public interface FundNetService {

    void insertOrUpdateFundNetData();

    @TargetDataSource
    void batchInsertFundNetData(List<FundNetDO> fundNetList);

    void test();

    void retry();
}
