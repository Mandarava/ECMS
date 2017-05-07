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
        String returnPage;
        boolean isSuccess = userService.userLogin(userId, password);
        if (isSuccess) {
            httpSession.setAttribute("userId", userId);
            returnPage = "redirect:/index";
        } else {
            returnPage = "login";
        }
        return returnPage;
    }

    @PostMapping(value = "logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @RequestMapping(value = "/index")
    public String showHomePage() {
        return "index";
    }

}
