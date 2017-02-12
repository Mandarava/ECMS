package com.finance.service;

import com.finance.exception.BusinessException;
import com.finance.model.pojo.FundOrderDO;

/**
 * Created by zt on 2017/2/7.
 */
public interface FundOrderService {

    void insertFundOrder(FundOrderDO fundOrder) throws BusinessException;
}
