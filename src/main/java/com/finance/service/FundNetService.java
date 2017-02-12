package com.finance.service;

import com.finance.datasource.TargetDataSource;
import com.finance.exception.BusinessException;
import com.finance.model.pojo.FundNetDO;

import java.util.List;

/**
 * Created by zt on 2016/12/10.
 */
public interface FundNetService {

    void insertOrUpdateFundNetData() throws Exception;

    @TargetDataSource
    void batchInsertFundNetData(List<FundNetDO> fundNetList) throws BusinessException;

    void test();
}
