package com.finance.controller;

import com.finance.service.FundNetService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Created by zt on 2016/12/10.
 */
@Controller
@RequestMapping(value = "/fund/net")
public class FundNetController {

    private static final Logger logger = LoggerFactory.getLogger(FundNetController.class);

    @Resource
    private FundNetService fundNetService;

    @ResponseBody
    @RequestMapping(value = "/insertOrUpdateFundNetData", method = RequestMethod.POST)
    public Map insertOrUpdateFundNetData() {
        Map<String, Object> map = new HashMap<>();
        try {
            fundNetService.insertOrUpdateFundNetData();
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
        fundNetService.test();
    }

}
