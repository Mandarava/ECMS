package com.finance.service;

import com.finance.datasource.TargetDataSource;
import com.finance.model.pojo.FundDO;

import java.util.List;

public interface FundService {

    @TargetDataSource
    void insertOrUpdateFundData() throws Exception;

    List<FundDO> findFunds();

}
