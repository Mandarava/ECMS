package com.finance.controller;

import com.finance.model.pojo.ProfitDO;
import com.finance.service.FundProfitService;
import com.finance.util.myutil.RequestContext;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by zt on 2016/12/10.
 */
@Controller
@RequestMapping(value = "/fund/profit")
@Slf4j
public class FundProfitController {

    @Resource
    private FundProfitService fundProfitService;

    @ResponseBody
    @RequestMapping(value = "/addProfit", method = RequestMethod.POST)
    public Map addProfit(ProfitDO profit) {
        Map<String, Object> map = new HashMap<>();
        try {
            profit.setUserId(RequestContext.getCurrentUserId());
            fundProfitService.insertFundProfit(profit);
            map.put("flag", 1);
            return map;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            map.put("flag", 0);
        }
        return map;
    }

}
