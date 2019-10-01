package com.finance.dao;

import com.finance.model.pojo.FundDO;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zt on 2016/10/2.
 */
@Repository
public interface FundDao {

    /**
     * 查找所有的基金
     */
    List<FundDO> findFunds();

    int batchInsertOrUpdateFundData(List<FundDO> fund);

    int insertOrUpdateFundData(FundDO fund);

    int updateFund(FundDO fund);

    int insertFund(FundDO fund);

    int batchInsertFund(List<FundDO> funds);

    int batchUpdateFund(List<FundDO> funds);
}
