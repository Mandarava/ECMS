package com.finance.service.serviceImpl;

import com.finance.dao.ProfitDao;
import com.finance.exception.BusinessException;
import com.finance.model.pojo.FundDO;
import com.finance.model.pojo.ProfitDO;
import com.finance.model.vo.FundProfitSumVO;
import com.finance.service.FundProfitService;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<FundDO> funds = profitDao.findFundCodes();
        List<FundProfitSumVO> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(funds)) {
            for (FundDO fund : funds) {
                List<ProfitDO> sumProfitByDay = profitDao.findSumProfitByDay(fund.getCode());
                FundProfitSumVO fundProfitSumVO = new FundProfitSumVO();
                fundProfitSumVO.setCode(fund.getCode());
                fundProfitSumVO.setName(fund.getName());
                fundProfitSumVO.setProfits(sumProfitByDay);
                result.add(fundProfitSumVO);
            }
        }
        return result;
    }

}
