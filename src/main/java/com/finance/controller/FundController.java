package com.finance.controller;

import com.finance.model.pojo.Profit;
import com.finance.service.FundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/fund")
public class FundController {

    private static Logger logger = LoggerFactory.getLogger(FundController.class);

    @Resource
    private FundService fundService;

    @ResponseBody
    @RequestMapping(value = "/addProfit", method = RequestMethod.POST)
    public Map addProfit(Profit profit) {
        Map<String, Object> map = new HashMap<>();
        try {
            fundService.insertFundProfit(profit);
            map.put("flag", 1);
            return map;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            map.put("flag", 0);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/insertOrUpdateFundNetData", method = RequestMethod.POST)
    public Map insertOrUpdateFundNetData() {
        Map<String, Object> map = new HashMap<>();
        try {
            fundService.insertOrUpdateFundNetData();
            map.put("flag", 1);
            return map;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            map.put("flag", 0);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/insertOrUpdateFundData", method = RequestMethod.POST)
    public Map insertOrUpdateFundData() {
        Map<String, Object> map = new HashMap<>();
        try {
            fundService.insertOrUpdateFundData();
            map.put("flag", 1);
            return map;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            map.put("flag", 0);
        }
        return map;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test() {
        fundService.test();
    }

}
