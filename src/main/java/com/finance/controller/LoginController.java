package com.finance.controller;

import com.finance.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by zt on 2017/2/3.
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserService userService;

    @PostMapping(value = "/login")
    public String login(@RequestParam String userId,
                        @RequestParam String password,
                        HttpSession httpSession) {
        boolean isSuccess = userService.userLogin(userId, password);
        if (isSuccess) {
            httpSession.setAttribute("user", userId);
        }
        return "redirect:/index";
    }

    @RequestMapping(value = "/index")
    public String showHomePage() {
        return "index";
    }

}
