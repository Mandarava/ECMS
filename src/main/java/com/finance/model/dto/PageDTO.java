package com.finance.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zt on 2016/10/3.
 */
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

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public int getStart() {
        return (pageNo - 1) * pageSize;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

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

    @Override
    public String toString() {
        return "PageDTO{" +
                "pageNo=" + pageNo +
                ", offset=" + offset +
                ", limit=" + limit +
                ", start=" + start +
                ", pageSize=" + pageSize +
                ", total=" + total +
                ", sortKey='" + sortKey + '\'' +
                ", results=" + results +
                '}';
    }
}
