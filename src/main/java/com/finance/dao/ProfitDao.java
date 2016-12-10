package com.finance.dao;

import com.finance.model.pojo.Profit;

import org.springframework.stereotype.Repository;

@Repository
public interface ProfitDao {

    int insertDailyProfit(Profit profit);
}
