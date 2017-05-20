package com.finance.util.myutil;

import org.springframework.security.crypto.bcrypt.BCrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * hash
 *
 * @author zt c.
 */
public final class EncryptUtil {

    private static final String DES_KEY = "nU+1kWLyGfc7BxAWp1JGwoB5tkWPStpK";
    private static final String ENCRYPT_ALGORITHM = "DESede";
    private static final String PADDING_MODE = "DESede/ECB/PKCS5Padding";

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

    public static String bcrypt(String plaintext) {
        String ciphertext = null;
        ciphertext = BCrypt.hashpw(plaintext, BCrypt.gensalt());
        return ciphertext;
    }

    public static String desedeEncrypt(String strDataToEncrypt) {
        String strCipherText = null;
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPT_ALGORITHM);
            SecretKey secretKey = keyFactory.generateSecret(new DESedeKeySpec(DES_KEY.getBytes("UTF-8")));

            Cipher desCipher = Cipher.getInstance(PADDING_MODE);
            desCipher.init(Cipher.ENCRYPT_MODE, secretKey, new SecureRandom());

            byte[] byteDataToEncrypt = strDataToEncrypt.getBytes("UTF-8");
            byte[] byteCipherText = desCipher.doFinal(byteDataToEncrypt);
            strCipherText = new BASE64Encoder().encode(byteCipherText);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strCipherText;
    }

    public static String desedeDecrypt(String strCipherText) {
        String strDecryptedText = null;
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPT_ALGORITHM);
            SecretKey secretKey = keyFactory.generateSecret(new DESedeKeySpec(DES_KEY.getBytes("UTF-8")));

            Cipher desCipher = Cipher.getInstance(PADDING_MODE);

            desCipher.init(Cipher.DECRYPT_MODE, secretKey, desCipher.getParameters());

            byte[] byteCipherText = new BASE64Decoder().decodeBuffer(strCipherText);
            byte[] byteDecryptedText = desCipher.doFinal(byteCipherText);

            strDecryptedText = new String(byteDecryptedText);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strDecryptedText;
    }

    public static String generateDESedeKey() {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("DESede");
            kgen.init(168);
            SecretKey desKey = kgen.generateKey();
            String key = new BASE64Encoder().encode(desKey.getEncoded());
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
