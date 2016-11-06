package com.finance.model.dto;

import com.finance.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by zt on 2016/10/3.
 */
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private HttpServletRequest request;

    /**
     * 用户信息
     */
    private User user = (User) request.getSession().getAttribute("user");

    /**
     * 分页信息
     */
    private Page page = new Page();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
