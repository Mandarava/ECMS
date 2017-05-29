package com.finance.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by zt
 * 2017/5/29 21:27
 */
@Controller
@RequestMapping("/captcha")
public class KaptchaController {

    @Resource
    private Producer captchaProducer;

    @ResponseBody
    @RequestMapping(value = "/image")
    public void getKaptchaImage(HttpSession httpSession,
                                HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        // 生成验证码字符串
        String captchaText = captchaProducer.createText();
        // 生成验证码图片
        BufferedImage bufferedImage = captchaProducer.createImage(captchaText);

        // ------------
        // 生成cookie
        Cookie cookie = new Cookie(Constants.KAPTCHA_SESSION_KEY, captchaText);
        // 300秒生存期
        cookie.setMaxAge(300);
        // 将cookie加入response
        response.addCookie(cookie);
        // ------------
        httpSession.setAttribute(Constants.KAPTCHA_SESSION_KEY, captchaText);
        // ------------

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bufferedImage, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    @ResponseBody
    @RequestMapping(value = "/image/{code}")
    public void getKaptchaImage(HttpSession httpSession,
                                HttpServletResponse response,
                                @PathVariable("code") String uniqueCode) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        // 生成验证码字符串
        String captchaText = captchaProducer.createText();
        // 生成验证码图片
        BufferedImage bufferedImage = captchaProducer.createImage(captchaText);
        // here put it into session, or Key-value database else
        httpSession.setAttribute(uniqueCode, captchaText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bufferedImage, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    @ResponseBody
    @RequestMapping(value = "/code")
    public CaptchaResopnse getKaptchaImageCode() {
        CaptchaResopnse captchaResopnse = new CaptchaResopnse();
        captchaResopnse.setCode(UUID.randomUUID().toString());
        return captchaResopnse;
    }

    private static class CaptchaResopnse implements Serializable {
        private String code;
        private String url; //http://example.com/captcha/fetch?code=1463d84555c57c29e317

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
