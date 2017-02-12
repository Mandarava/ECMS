package com.finance.bind;

import com.finance.model.dto.PageDTO;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by chenkaihua on 15-8-24.
 */


public class RequestPageInfoMethodArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(RequestPageInfo.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        RequestPageInfo requestPageInfo = parameter.getParameterAnnotation(RequestPageInfo.class);

        String strPage = webRequest.getHeader("page");
        String strLimit = webRequest.getHeader("limit");
        PageDTO pageDTO = new PageDTO();
        if (strPage != null && strPage.matches("\\d+")) {
            pageDTO.setPageNo(Integer.parseInt(strPage));
        } else {
            pageDTO.setPageNo(requestPageInfo.pageNo());
        }
        if (strLimit != null && strLimit.matches("\\d+")) {
            pageDTO.setLimit(Integer.parseInt(strLimit));
        } else {
            pageDTO.setLimit(requestPageInfo.limit());
        }
        return pageDTO;
    }
}
