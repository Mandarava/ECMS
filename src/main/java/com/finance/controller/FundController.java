package com.finance.controller;

import com.finance.service.FundService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/fund")
@Slf4j
public class FundController {

    @Resource
    private FundService fundService;

    @ResponseBody
    @RequestMapping(value = "/insertOrUpdateFundData", method = RequestMethod.POST)
    public Map insertOrUpdateFundData() {
        Map<String, Object> map = new HashMap<>();
        try {
            fundService.insertOrUpdateFundData();
            map.put("flag", 1);
            return map;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            map.put("flag", 0);
        }
        return map;
    }

}
