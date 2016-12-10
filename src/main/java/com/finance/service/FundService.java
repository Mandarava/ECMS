package com.finance.service;

import com.finance.annotation.DataSource;
import com.finance.common.DataSourceType;
import com.finance.model.pojo.Fund;

import java.util.List;

public interface FundService {

    @DataSource(DataSourceType.Master)
    void insertOrUpdateFundData() throws Exception;

    List<Fund> findFunds();

}
