package com.finance.util.myutil;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class DateConverter implements Converter {

    @SuppressWarnings("unchecked")
    @Override
    public Object convert(@SuppressWarnings("rawtypes") Class type, Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return value;
        }
        if (value instanceof Long) {
            Long longValue = (Long) value;
            return new Date(longValue.longValue());
        }
        if (value instanceof String) {
            Date endTime = null;
            try {
                // issue dateFormat毫秒补零BUG 手动补0
                if (((String) value)
                        .matches("^\\d{4}[-/]?\\d{2}[-/]?\\d{2} \\d{2}[:]?\\d{2}[:]?\\d{2}[.]{1}\\d{1,3}$")) {
                    int index = ((String) value).lastIndexOf(".");
                    if (index != -1) {
                        String dateTime = ((String) value).substring(0, index);
                        String millisecond = ((String) value).substring(index);
                        String zero = "000" ;
                        String rightzero = zero.substring(0, 4 - millisecond.length());
                        millisecond = millisecond + rightzero;
                        value = dateTime + millisecond;
                    }
                }
                endTime = DateUtils.parseDate(value.toString().trim(), "yyyy-MM-dd HH:mm:ss.SSS", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return endTime;
        }
        return null;
    }
}
