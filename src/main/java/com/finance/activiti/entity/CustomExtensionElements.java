package com.finance.activiti.entity;

import java.io.Serializable;

/**
 * activiti自定义扩展属性对象
 */
public class CustomExtensionElements implements Serializable {

    private static final long serialVersionUID = 5152389005224857880L;

    private Operations operations;

    private ResourceAuthorities resourceAuthorityList;

    public Operations getOperations() {
        return operations;
    }

    public void setOperations(Operations operations) {
        this.operations = operations;
    }

    public ResourceAuthorities getResourceAuthorityList() {
        return resourceAuthorityList;
    }

    public void setResourceAuthorityList(ResourceAuthorities resourceAuthorityList) {
        this.resourceAuthorityList = resourceAuthorityList;
    }
}