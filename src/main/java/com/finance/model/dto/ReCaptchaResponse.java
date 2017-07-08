package com.finance.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zt
 * 2017/5/29 19:28
 */
@Setter
@Getter
public class ReCaptchaResponse implements Serializable {

    private boolean success;

    @SerializedName("challenge_ts")
    private Date challengeTs;

    private String hostname;

    @SerializedName("error-codes")
    private String[] errorCodes;

}
