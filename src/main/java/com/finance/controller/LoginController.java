package com.finance.controller;

import com.google.gson.Gson;

import com.finance.model.dto.ReCaptchaResponse;
import com.finance.service.UserService;
import com.finance.util.myutil.RSAUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyPair;
import java.util.Base64;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by zt on 2017/2/3.
 */
@Controller
@Slf4j
public class LoginController {

    private static final String RECAPTCHA_SECRET_KEY = "6Le_SiMUAAAAAOVSFY5u6ld6yCJ2YX0Bo_FMIaC4"; // for test
    private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String RSA_KEY = "rsa_key";

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
        boolean isCaptchaValid = validateReCaptcha(gRecaptchaResponse, request.getRemoteAddr());
        if (!isCaptchaValid) {
            log.debug("input captcha is invalid");
        }
        // captcha by session
        String captchaText = (String) httpSession.getAttribute(captchaImageCode);
        if (StringUtils.isNotEmpty(captcha) && captcha.equalsIgnoreCase(captchaText)) {
            log.debug("captcha input valid");
        } else {
            log.debug("captcha input invalid");
        }
        // 及时销毁验证码
        httpSession.removeAttribute(captchaImageCode);

        KeyPair keyPair = (KeyPair) httpSession.getAttribute(RSA_KEY);
        String decryptedPassword = RSAUtil.decryptByPrivateKeySplit(Base64.getDecoder().decode(password), keyPair.getPrivate());

        String returnPage;
        boolean isSuccess = userService.userLogin(userId, decryptedPassword);
        if (isSuccess) {
            httpSession.setAttribute("userId", userId);
            returnPage = "redirect:/index";
        } else {
            returnPage = "login";
        }
        return returnPage;
    }

    @PostMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @RequestMapping(value = "/index")
    public String showHomePage() {
        return "index";
    }

    @GetMapping(value = "rsa/public_key")
    @ResponseBody
    public String getRsaPublicKey(HttpSession session) {
        KeyPair keyPair = RSAUtil.generateKeyPair();
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        session.setAttribute(RSA_KEY, keyPair);
        return publicKey;
    }

    private boolean validateReCaptcha(String reCaptcha, String remoteAddress) {
        try {
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
        } catch (MalformedURLException e) {
            log.debug(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            log.debug(e.getMessage(), e);
        } catch (ProtocolException e) {
            log.debug(e.getMessage(), e);
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
        }
        return false;
    }

}
