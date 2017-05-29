package com.finance.util.myutil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Created by zt
 * 2017/5/29 18:03
 */
public final class ImageUtil {

    private static final String VERIFICATION_CODE = "ABCDEFFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SIZE = 5;
    private static final int WIDTH = 120;
    private static final int HEIGHT = 30;
    private static final int LINES = 2;
    private static final int FONT_SIZE = 25;

    /**
     * 获得验证码
     *
     * @return 第一个为验证码String，第二个为图片BufferedImage
     */
    public static Object[] createCaptchaImage() {
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        drawRect(graphics);
        // 验证码
        String captcha = generateRandomCharacter();
        // 画验证码
        drawCaptchaCode(graphics, captcha);
        // 画干扰线
        drawInterferenceLine(graphics);
        return new Object[]{captcha, bufferedImage};
    }

    private static void drawRect(Graphics graphics) {
        graphics.setColor(getRandomColor());
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private static void drawCaptchaCode(Graphics graphics, String captcha) {
        for (int i = 0; i < captcha.length(); i++) {
            graphics.setColor(getRandomColor());
            graphics.setFont(new Font(
                    null, Font.BOLD + Font.ITALIC, FONT_SIZE));
            graphics.drawString(Character.toString(captcha.charAt(i)), i * WIDTH / SIZE, HEIGHT * 2 / 3);
        }
    }

    private static void drawInterferenceLine(Graphics graphics) {
        Random random = new Random();
        for (int i = 0; i < LINES; i++) {
            graphics.setColor(getRandomColor());
            graphics.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT), random.nextInt(WIDTH), random.nextInt(HEIGHT));
        }
    }

    private static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private static String generateRandomCharacter() {
        char[] ch = VERIFICATION_CODE.toCharArray();
        Random random = new Random();
        int length = ch.length;
        int index;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            index = random.nextInt(length);
            stringBuilder.append(ch[index]);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws IOException {
        Object[] objs = createCaptchaImage();
        BufferedImage image = (BufferedImage) objs[1];
        OutputStream os = new FileOutputStream("G:/1.png");
        ImageIO.write(image, "png", os);
        os.close();
    }
}
