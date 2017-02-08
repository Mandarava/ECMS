package com.finance.dao;

import com.finance.model.pojo.FundOrder;

import org.springframework.stereotype.Repository;

/**
 * Created by zt on 2017/2/7.
 */
@Repository
public interface FundOrderDao {

    int insertFundOrder(FundOrder fundOrder);
}
