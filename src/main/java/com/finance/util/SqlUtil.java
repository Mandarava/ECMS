package com.finance.util;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MethodInvoker;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zt 2018/9/8 17:02
 */
public class SqlUtil {

    private static final Logger logger = LoggerFactory.getLogger(SqlUtil.class);

    private static final int BATCH_INSERT_PER_PAGE_SIZE = 1000;

    /**
     * @param mapperBean mapper
     * @param sqlMethod  sql method name
     * @param paramList  list parameter
     * @param params     other parameters
     */
    private void insertBatch(Object mapperBean, String sqlMethod, List<?> paramList, Object... params) {
        if (CollectionUtils.isEmpty(paramList)) {
            return;
        }
        int pages = (paramList.size() - 1) / BATCH_INSERT_PER_PAGE_SIZE + 1;
        for (int i = 0; i < pages; i++) {
            int fromIndex = i * BATCH_INSERT_PER_PAGE_SIZE;
            int toIndex = fromIndex + BATCH_INSERT_PER_PAGE_SIZE;
            List<?> subList = new ArrayList<>(paramList.subList(fromIndex, paramList.size() > toIndex ? toIndex : paramList.size()));
            Object[] arguments;
            if (params != null) {
                arguments = new Object[params.length + 1];
                arguments[0] = subList;
                System.arraycopy(params, 0, arguments, 1, params.length);
            } else {
                arguments = new Object[1];
                arguments[0] = subList;
            }
            this.invokeMethod(mapperBean, sqlMethod, arguments);
        }
    }

    private void invokeMethod(Object mapperBean, String sqlMethod, Object... arguments) {
        MethodInvoker beanMethod = new MethodInvoker();
        beanMethod.setTargetObject(mapperBean);
        beanMethod.setTargetMethod(sqlMethod);
        beanMethod.setArguments(arguments);
        try {
            beanMethod.prepare();
            beanMethod.invoke();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
