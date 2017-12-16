package com.finance.service.serviceImpl;

import com.finance.dao.ProfitDao;
import com.finance.exception.BusinessException;
import com.finance.model.pojo.ProfitDO;
import com.finance.model.vo.FundProfitSumVO;
import com.finance.service.FundProfitService;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

/**
 * Created by zt on 2016/12/10.
 */
@Service
public class FundProfitServiceImpl implements FundProfitService {

    private static final Logger logger = LoggerFactory.getLogger(FundProfitServiceImpl.class);

    @Resource
    private ProfitDao profitDao;

    @Override
    public void insertFundProfit(ProfitDO profit) {
        int i = profitDao.insertDailyProfit(profit);
        if (i == 0) {
            throw new BusinessException("insertFundProfit failed!");
        }
    }

    @Override
    public List<FundProfitSumVO> findSumProfit() {
        List<FundProfitSumVO> result = profitDao.findSumProfit();
        if (CollectionUtils.isNotEmpty(result)) {
            for (FundProfitSumVO fundProfitSumVO : result) {
                Collections.sort(fundProfitSumVO.getProfits(), Comparator.comparing(ProfitDO::getTime));
            }
        }
        return result;
    }

}
