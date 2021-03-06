package com.finance.controller;

import com.finance.service.FundNetService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by zt on 2016/12/10.
 */
@Controller
@RequestMapping(value = "/fund/net")
@Slf4j
public class FundNetController {

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
            log.error(e.getMessage(), e);
            map.put("flag", 0);
        }
        return map;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public void test() {
        fundNetService.test();
    }

    @RequestMapping(value = "/retry", method = RequestMethod.GET)
    @ResponseBody
    public void retry() {
        fundNetService.retry();
    }

    @RequestMapping(value = "/upload")
    @ResponseBody
    public void upload(@RequestParam(value = "file") MultipartFile multipartFile, HttpServletRequest httpServletRequest) {
        try {
            System.out.println(multipartFile.getOriginalFilename());
            MultipartUtility multipart = new MultipartUtility("http://localhost:8089/xxx");
            multipart.addFilePart("file", System.currentTimeMillis() + ".txt", multipartFile.getInputStream());
            multipart.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
