package com.finance.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zt on 2016/10/3.
 */
@Setter
@Getter
@ToString
public class PageDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前第几页
     */
    private int pageNo;

    /**
     * 数据库中limit的参数，从第几条开始取
     */
    private int offset;

    /**
     * 数据库中limit的参数，一共取多少条
     */
    private int limit;

    /**
     * 起始页
     **/
    private int start;

    /**
     * 每页多少条记录
     **/
    private int pageSize;

    /**
     * 总件数
     */
    private int total;

    /**
     * 排序语句
     */
    private String sortKey;

    /**
     * 存放集合
     **/
    private List<T> results = new ArrayList<T>();

    // 计算总页数
    public int getTotalPages() {
        int totalPages;
        if (total % limit == 0) {
            totalPages = total / limit;
        } else {
            totalPages = (total / limit) + 1;
        }
        return totalPages;
    }

    public int getOffsets() {
        return (pageNo - 1) * limit;
    }

    public int getEndIndex() {
        if (getOffsets() + limit > total) {
            return total;
        } else {
            return getOffsets() + limit;
        }
    }

}
