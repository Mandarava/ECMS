package com.finance.util.myutil;

import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * hash
 *
 * @author zt c.
 */
public final class EncryptUtil {

    public static final String SHA_1 = "SHA-1";
    public static final String MD5 = "MD5";

    public static String encodeMD5(String plaintext) {
        MessageDigest md = null;
        String ciphertext = null;
        byte bt[] = plaintext.getBytes();
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(bt);
            ciphertext = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return plaintext;
        }
        return ciphertext;
    }

    public static String encodeSHA1(String plaintext) {
        MessageDigest md = null;
        String ciphertext = null;
        byte bt[] = plaintext.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(bt);
            ciphertext = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return plaintext;
        }
        return ciphertext;
    }

    public static String encodeSHA256(String plaintext) {
        MessageDigest md = null;
        String ciphertext = null;
        byte bt[] = plaintext.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(bt);
            ciphertext = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return plaintext;
        }
        return ciphertext;
    }

    public static String encodeSHA512(String plaintext) {
        MessageDigest md = null;
        String ciphertext = null;
        byte bt[] = plaintext.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(bt);
            ciphertext = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return plaintext;
        }

        return ciphertext;
    }


    public static String encodeBASE64(String plaintext) {
        String base64 = null;
        try {
            base64 = Base64.getEncoder().encodeToString(plaintext.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return base64;
    }

    public static String decodeBASE64(String ciphertext) {
        String plaintext = null;
        try {
            plaintext = new String(Base64.getDecoder().decode(ciphertext), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return plaintext;
    }


    /**
     * 根据给定密钥生成算法创建密钥
     *
     * @param algorithm 密钥算法
     * @return 密钥
     * @throws RuntimeException 当 {@link java.security.NoSuchAlgorithmException} 发生时
     */
    private static String getHmacKey(String algorithm) {
        //初始化KeyGenerator
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        //产生密钥
        SecretKey secretKey = keyGenerator.generateKey();
        //获得密钥
        return secretKey.getEncoded().toString();
    }

    /**
     * 获取 HmaMD5的密钥
     *
     * @return HmaMD5的密钥
     * @throws RuntimeException 当 {@link java.security.NoSuchAlgorithmException} 发生时
     */
    public static String getHmaMD5key() {
        return getHmacKey("HmacMD5");
    }

    /**
     * 获取 HmaSHA的密钥
     *
     * @return HmaSHA的密钥
     * @throws RuntimeException 当 {@link java.security.NoSuchAlgorithmException} 发生时
     */
    public static String getHmaSHA1key() {
        return getHmacKey("HmacSHA1");
    }

    /**
     * 获取 HmaSHA256的密钥
     *
     * @return HmaSHA256的密钥
     * @throws RuntimeException 当 {@link java.security.NoSuchAlgorithmException} 发生时
     */
    public static String getHmaSHA256key() {
        return getHmacKey("HmacSHA256");
    }

    /**
     * 获取 HmaSHA512的密钥
     *
     * @return HmaSHA384的密钥
     * @throws RuntimeException 当 {@link java.security.NoSuchAlgorithmException} 发生时
     */
    public static String getHmaSHA512key() {
        return getHmacKey("HmacSHA512");
    }


    /**
     * 使用HmacMD5消息摘要算法计算消息摘要
     *
     * @param data 做消息摘要的数据
     * @param key  密钥
     * @return 消息摘要（长度为16的字节数组）
     */
    public static String encodeHmacMD5(String data, String key) {
        Mac mac = null;
        String ciphertext = null;
        try {
            Key k = new SecretKeySpec(key.getBytes("UTF-8"), "HmacMD5");
            mac = Mac.getInstance("HmacMD5");
            mac.init(k);
            byte[] bytes = mac.doFinal(data.getBytes("UTF-8"));
            ciphertext = bytes2Hex(bytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return ciphertext;
    }


    /**
     * 使用HmacSHA消息摘要算法计算消息摘要
     *
     * @param data 做消息摘要的数据
     * @param key  密钥
     * @return 消息摘要（长度为16的字节数组）
     */
    public static String encodeHmacSHA1(String data, String key) {
        Mac mac = null;
        String ciphertext = null;
        try {
            Key k = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
            mac = Mac.getInstance("HmacSHA1");
            mac.init(k);
            byte[] bytes = mac.doFinal(data.getBytes("UTF-8"));
            ciphertext = bytes2Hex(bytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return ciphertext;
    }


    /**
     * 使用HmacSHA256消息摘要算法计算消息摘要
     *
     * @param data 做消息摘要的数据
     * @param key  密钥
     * @return 消息摘要（长度为16的字节数组）
     */
    public static String encodeHmacSHA256(String data, String key) {
        Mac mac = null;
        String ciphertext = null;
        try {
            Key k = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            mac = Mac.getInstance("HmacSHA256");
            mac.init(k);
            byte[] bytes = mac.doFinal(data.getBytes("UTF-8"));
            ciphertext = bytes2Hex(bytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return ciphertext;
    }


    /**
     * 使用HmacSHA512消息摘要算法计算消息摘要
     *
     * @param data 做消息摘要的数据
     * @param key  密钥
     * @return 消息摘要（长度为16的字节数组）
     */
    public static String encodeHmacSHA512(String data, String key) {
        Mac mac = null;
        String ciphertext = null;
        try {
            Key k = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA512");
            mac = Mac.getInstance("HmacSHA512");
            mac.init(k);
            byte[] bytes = mac.doFinal(data.getBytes("UTF-8"));
            ciphertext = bytes2Hex(bytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return ciphertext;
    }

    public static String bytes2Hex(byte bytes[]) {
        String hex = null;
        StringBuffer hash = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            hex = Integer.toHexString(bytes[i] & 0xff);
            if (hex.length() == 1) {
                hash.append('0');
            }
            hash.append(hex);
        }
        return hash.toString();
    }

    /**
     * SHA1
     *
     * @param str 字符串
     * @return
     */
    public static String getSHA1(String str) {
        String generatedPassword = null;
        if (null == str || 0 == str.length()) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            generatedPassword = new String(buf).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return generatedPassword;
    }

    public static String Bcrypt(String plaintext) {
        String ciphertext = null;
        ciphertext = BCrypt.hashpw(plaintext, BCrypt.gensalt());
        return ciphertext;
    }

}
