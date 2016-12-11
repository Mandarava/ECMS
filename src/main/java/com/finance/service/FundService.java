package com.finance.service;

import com.finance.annotation.TargetDataSource;
import com.finance.model.pojo.Fund;

import java.util.List;

public interface FundService {

    @TargetDataSource
    void insertOrUpdateFundData() throws Exception;

    List<Fund> findFunds();

}
