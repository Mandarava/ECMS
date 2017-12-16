package com.finance.service;

import com.finance.model.pojo.ProfitDO;
import com.finance.model.vo.FundProfitSumVO;

import java.util.List;

/**
 * Created by zt on 2016/12/10.
 */
public interface FundProfitService {

    void insertFundProfit(ProfitDO profit);

    List<FundProfitSumVO> findSumProfit();
}
