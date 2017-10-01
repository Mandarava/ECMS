package com.finance.util;

import com.finance.enums.ResponseCodeEnum;
import com.finance.model.vo.BaseResponse;

/**
 * Created by zt 2017/10/1 21:45
 */
public final class ResponseUtil {

    public static BaseResponse success(Object object) {
        BaseResponse result = new BaseResponse();
        result.setCode(ResponseCodeEnum.SUCCESS.getCode());
        result.setMessage(ResponseCodeEnum.SUCCESS.getMessage());
        result.setData(object);
        return result;
    }

    public static BaseResponse success() {
        return success(null);
    }

    public static BaseResponse error(Integer code, String message) {
        BaseResponse result = new BaseResponse();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static BaseResponse error(Integer code) {
        BaseResponse result = new BaseResponse();
        result.setCode(code);
        return result;
    }

    public static BaseResponse error(ResponseCodeEnum responseCodeEnum) {
        BaseResponse result = new BaseResponse();
        result.setCode(responseCodeEnum.getCode());
        result.setMessage(responseCodeEnum.getMessage());
        return result;
    }
}
