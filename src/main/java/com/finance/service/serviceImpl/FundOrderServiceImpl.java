package com.finance.service.serviceImpl;

import com.finance.dao.FundOrderDao;
import com.finance.exception.BusinessException;
import com.finance.model.pojo.FundOrderDO;
import com.finance.service.FundOrderService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zt on 2017/2/7.
 */
@Service
public class FundOrderServiceImpl implements FundOrderService {

    @Resource
    private FundOrderDao fundOrderDao;

    @Override
    public void insertFundOrder(FundOrderDO fundOrder) {
        int i = fundOrderDao.insertFundOrder(fundOrder);
        if (i == 0) {
            throw new BusinessException("insertFundOrder failed!");
        }
    }
}
