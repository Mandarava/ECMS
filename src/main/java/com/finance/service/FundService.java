package com.finance.service;

import com.finance.annotation.DataSource;
import com.finance.common.DataSourceType;
import com.finance.exception.BusinessException;
import com.finance.model.pojo.FundNet;
import com.finance.model.pojo.Profit;

import java.util.List;

public interface FundService {

    void insertFundProfit(Profit profit) throws Exception;

    void insertOrUpdateFundNetData() throws Exception;

    @DataSource(DataSourceType.Master)
    void insertOrUpdateFundData() throws Exception;

    @DataSource
    void batchInsertFundNetData(List<FundNet> fundNetList) throws BusinessException;

    void test();

}
