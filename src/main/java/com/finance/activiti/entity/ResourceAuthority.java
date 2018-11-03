package com.finance.activiti.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 页面资源权限
 */
public class ResourceAuthority implements Serializable {

    private static final long serialVersionUID = 4091796476024596124L;

    private String id;

    /**
     * 0=可见可编辑 1=可见不可编辑 2=不可见不可编辑
     */
    private int status;

    @JSONField(serialize = false)
    private String name;

    @JSONField(name = "resource_id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
