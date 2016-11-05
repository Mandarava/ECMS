package com.finance.model.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zt on 2016/10/3.
 */
public class User implements Serializable {

    private String userId;

    private String userName;

    private String password;

    private String mailAddress;

    private String deleteflg;

    private String updaterId;

    private Date updatetime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getDeleteflg() {
        return deleteflg;
    }

    public void setDeleteflg(String deleteflg) {
        this.deleteflg = deleteflg;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", mailAddress='" + mailAddress + '\'' +
                ", deleteflg='" + deleteflg + '\'' +
                ", updaterId='" + updaterId + '\'' +
                ", updatetime=" + updatetime +
                '}';
    }
}
