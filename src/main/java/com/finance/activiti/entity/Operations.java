package com.finance.activiti.entity;

import java.io.Serializable;

public class Operations implements Serializable {

    private static final long serialVersionUID = 7709460510259303688L;

    private TurnBack turnback;

    public TurnBack getTurnback() {
        return turnback;
    }

    public void setTurnback(TurnBack turnback) {
        this.turnback = turnback;
    }
}
