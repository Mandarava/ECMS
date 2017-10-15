package com.finance.model.dto;

import java.io.Serializable;

/**
 * Created by zt 2017/10/7 14:10
 */
public class Menu implements Serializable {

    private int pid;

    private int id;

    private String name;

    private boolean isParent;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

}
