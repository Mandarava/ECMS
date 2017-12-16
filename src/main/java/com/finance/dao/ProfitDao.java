package com.finance.dao;

import com.finance.model.pojo.FundDO;
import com.finance.model.pojo.ProfitDO;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfitDao {

    int insertDailyProfit(ProfitDO profit);

    List<FundDO> findFundCodes();

    List<ProfitDO> findSumProfitByDay(String code);
}
