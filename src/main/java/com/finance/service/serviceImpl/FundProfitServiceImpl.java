package com.finance.service.serviceImpl;

import com.finance.dao.ProfitDao;
import com.finance.exception.BusinessException;
import com.finance.model.pojo.Profit;
import com.finance.service.FundProfitService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zt on 2016/12/10.
 */
@Service
public class FundProfitServiceImpl implements FundProfitService {

    private static Logger logger = LoggerFactory.getLogger(FundProfitServiceImpl.class);

    @Resource
    private ProfitDao profitDao;

    @Override
    public void insertFundProfit(Profit profit) throws BusinessException {
        int i = profitDao.insertDailyProfit(profit);
        if (i == 0) {
            throw new BusinessException("insertFundProfit failed!");
        }
    }

}
