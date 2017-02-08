package com.finance.service;

import com.finance.exception.BusinessException;
import com.finance.model.pojo.FundOrder;

/**
 * Created by zt on 2017/2/7.
 */
public interface FundOrderService {

    void insertFundOrder(FundOrder fundOrder) throws BusinessException;
}
