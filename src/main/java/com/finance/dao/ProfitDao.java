package com.finance.dao;

import com.finance.model.pojo.ProfitDO;
import com.finance.model.vo.FundProfitSumVO;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfitDao {

    int insertDailyProfit(ProfitDO profit);

    List<FundProfitSumVO> findSumProfit();
}
