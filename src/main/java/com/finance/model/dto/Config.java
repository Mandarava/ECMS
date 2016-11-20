package com.finance.model.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by zt on 2016/11/6.
 */
@Component
public class Config {

    private static String filePath;

    @Value("${file.downloadPath}")
    private String filePathTemp;

    public static String getFilePath() {
        return filePath;
    }

    @PostConstruct
    public void init() {
        filePath = filePathTemp;
    }

}
