package com.finance.util.myutil;

import com.google.gson.Gson;

import com.finance.model.pojo.FundDO;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;

/**
 * Created by zt
 * 2017/6/11 11:55
 */
public class RSAUtil {

    public static final int DEFAULT_RSA_KEY_SIZE = 2048; // 秘钥默认长度
    private static final String RSA = "RSA";
    private static final String RSA_ECB_PKCS1 = "RSA/ECB/PKCS1Padding"; // 1024/2048
    private static final int MAX_ENCRYPT_BLOCK = (DEFAULT_RSA_KEY_SIZE / 8) - 11;
    private static final int MAX_DECRYPT_BLOCK = DEFAULT_RSA_KEY_SIZE / 8;
    private static final Logger logger = LoggerFactory.getLogger(RSAUtil.class);

    public static KeyPair generateKeyPair() {
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSA);
            keyGen.initialize(DEFAULT_RSA_KEY_SIZE);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            logger.debug(e.getMessage(), e);
        }
        return null;
    }

    public static byte[] encryptByPublicKey(byte[] plainData, PublicKey publicKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getEncoded());
            KeyFactory kf = KeyFactory.getInstance(RSA);
            PublicKey keyPublic = kf.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1);
            cipher.init(Cipher.ENCRYPT_MODE, keyPublic);
            return cipher.doFinal(plainData);
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
        return new byte[]{};
    }

    public static byte[] encryptByPrivateKey(byte[] plainData, PrivateKey privateKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
            KeyFactory kf = KeyFactory.getInstance(RSA);
            PrivateKey keyPrivate = kf.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1);
            cipher.init(Cipher.ENCRYPT_MODE, keyPrivate);
            return cipher.doFinal(plainData);
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
        return new byte[]{};
    }

    public static byte[] decryptByPublicKey(byte[] cipherData, PublicKey publicKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getEncoded());
            KeyFactory kf = KeyFactory.getInstance(RSA);
            PublicKey keyPublic = kf.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1);
            cipher.init(Cipher.DECRYPT_MODE, keyPublic);
            return cipher.doFinal(cipherData);
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
        return new byte[]{};
    }

    public static byte[] decryptByPrivateKey(byte[] cipherData, PrivateKey privateKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
            KeyFactory ke = KeyFactory.getInstance(RSA);
            PrivateKey keyPrivate = ke.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1);
            cipher.init(Cipher.DECRYPT_MODE, keyPrivate);
            return cipher.doFinal(cipherData);
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
        return new byte[]{};
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 密文
     * @param privateKey    私钥
     */
    public static byte[] decryptByPrivateKeySplit(byte[] encryptedData, PrivateKey privateKey) {
        int inputLength = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        while (inputLength - offset > 0) {
            if (inputLength - offset > MAX_DECRYPT_BLOCK) {
                cache = decryptByPrivateKey(Arrays.copyOfRange(encryptedData, offset, offset + MAX_DECRYPT_BLOCK), privateKey);
            } else {
                cache = decryptByPrivateKey(Arrays.copyOfRange(encryptedData, offset, encryptedData.length), privateKey);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        IOUtils.closeQuietly(out);
        return decryptedData;
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      明文
     * @param publicKey 公钥
     */
    public static byte[] encryptByPublicKeySplit(byte[] data, PublicKey publicKey) {
        int inputLength = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        while (inputLength - offset > 0) {
            if (inputLength - offset > MAX_ENCRYPT_BLOCK) {
                cache = encryptByPublicKey(Arrays.copyOfRange(data, offset, offset + MAX_ENCRYPT_BLOCK), publicKey);
            } else {
                cache = encryptByPublicKey(Arrays.copyOfRange(data, offset, data.length), publicKey);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        IOUtils.closeQuietly(out);
        return encryptedData;
    }

    public static void main(String[] args) {
        KeyPair keyPair = generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("public key " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println("private key " + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        List<FundDO> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new FundDO());
        }
        String cipherText2 = Base64.getEncoder().encodeToString(encryptByPublicKeySplit(new Gson().toJson(list).getBytes(), publicKey));
        System.out.println(cipherText2);
        System.out.println(new String(decryptByPrivateKeySplit(Base64.getDecoder().decode(cipherText2), privateKey)));
    }

}
