package com.finance.model.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by zt on 2016/11/6.
 */
@Component
@ToString
public class Config {

    @Getter
    private static String filePath;

    @Value("${file.downloadPath}")
    private String filePathTemp;

    @PostConstruct
    public void init() {
        filePath = filePathTemp;
    }

}
