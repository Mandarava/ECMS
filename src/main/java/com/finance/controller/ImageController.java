package com.finance.controller;

import com.finance.util.myutil.ImageUtil;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by zt
 * 2017/5/29 16:56
 */
@Controller
public class ImageController {

    @GetMapping(value = "image/captcha")
    public void getCaptchaImage(HttpServletResponse response, HttpSession session) throws IOException {
        Object[] object = ImageUtil.createCaptchaImage();
        String captcha = (String) object[0];
        BufferedImage bufferedImage = (BufferedImage) object[1];

        session.setAttribute("piccode", captcha);
        response.setContentType("image/jpeg");
        OutputStream os = response.getOutputStream();
        ImageIO.write(bufferedImage, "jpg", os);
        IOUtils.closeQuietly(os);
    }

}
