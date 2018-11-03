package com.finance.activiti.entity;

/**
 * Activiti 扩展属性
 */
public class ExtendExtensionElements extends CustomExtensionElements {

    private static final long serialVersionUID = 15107862461181044L;

    private String caseStatus;

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }
}
