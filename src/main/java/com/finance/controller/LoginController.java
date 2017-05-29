package com.finance.controller;

import com.google.gson.Gson;

import com.finance.model.dto.ReCaptchaResponse;
import com.finance.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by zt on 2017/2/3.
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final String RECAPTCHA_SECRET_KEY = "6Le_SiMUAAAAAOVSFY5u6ld6yCJ2YX0Bo_FMIaC4"; // for test
    private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Resource
    private UserService userService;

    @PostMapping(value = "/login")
    public String login(@RequestParam String userId,
                        @RequestParam String password,
                        @RequestParam(value = "captcha", required = false) String captcha,
                        @RequestParam(value = "imgCode", required = false) String captchaImageCode,
                        @RequestParam(value = "g-recaptcha-response", required = false) String gRecaptchaResponse,
                        HttpSession httpSession,
                        HttpServletRequest request) {
        // Google reCaptcha
        try {
            validateReCaptcha(gRecaptchaResponse, request.getRemoteAddr());
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
        }
        // common captcha
        String captchaText = (String) httpSession.getAttribute(captchaImageCode);
        if (captchaText.equalsIgnoreCase(captcha)) {
            logger.debug("captcha input valid");
        } else {
            logger.debug("captcha input invalid");
        }

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

    private boolean validateReCaptcha(String reCaptcha, String remoteAddress) throws IOException {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));
        String uri = String.format("%s?secret=%s&response=%s&remoteip=%s", RECAPTCHA_URL, RECAPTCHA_SECRET_KEY, reCaptcha, remoteAddress);
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("charset", "UTF-8");
        InputStream in = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        in.close();
        ReCaptchaResponse reCaptchaResponse = new Gson().fromJson(sb.toString(), ReCaptchaResponse.class);
        return reCaptchaResponse.isSuccess();
    }

}
