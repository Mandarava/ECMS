package com.finance.dao;

import com.finance.model.pojo.ProfitDO;

import org.springframework.stereotype.Repository;

@Repository
public interface ProfitDao {

    int insertDailyProfit(ProfitDO profit);
}
