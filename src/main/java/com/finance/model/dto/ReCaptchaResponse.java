package com.finance.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zt
 * 2017/5/29 19:28
 */
public class ReCaptchaResponse implements Serializable {

    private boolean success;

    @SerializedName("challenge_ts")
    private Date challengeTs;

    private String hostname;

    @SerializedName("error-codes")
    private String errorCodes;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Date getChallengeTs() {
        return challengeTs;
    }

    public void setChallengeTs(Date challengeTs) {
        this.challengeTs = challengeTs;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(String errorCodes) {
        this.errorCodes = errorCodes;
    }
}
