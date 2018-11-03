package com.finance.activiti.entity;

import java.io.Serializable;

public class TurnBack implements Serializable {

    private static final long serialVersionUID = -3452167310205500083L;

    private String backTo;

    public String getBackTo() {
        return backTo;
    }

    public void setBackTo(String backTo) {
        this.backTo = backTo;
    }
}
