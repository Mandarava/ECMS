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
import java.security.Signature;
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

    public static final int DEFAULT_RSA_KEY_SIZE = 1024; // 秘钥默认长度
    private static final String RSA = "RSA";
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String RSA_ECB_PKCS1 = "RSA/ECB/PKCS1Padding"; // 1024/2048
    private static final int MAX_ENCRYPT_BLOCK = (DEFAULT_RSA_KEY_SIZE / 8) - 11;
    private static final int MAX_DECRYPT_BLOCK = DEFAULT_RSA_KEY_SIZE / 8;
    private static final Logger logger = LoggerFactory.getLogger(RSAUtil.class);

    public static String sign(byte[] data, String privateKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(privateKey);

            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance(RSA);

            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(priKey);
            signature.update(data);

            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
        return null;
    }

    public static boolean verify(byte[] data, String publicKey, String sign) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKey);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance(RSA);

            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(data);

            return signature.verify(Base64.getDecoder().decode(sign));

        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
        return false;
    }

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
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PublicKey keyPublic = keyFactory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1);
//            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
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
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PrivateKey keyPrivate = keyFactory.generatePrivate(keySpec);
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
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PublicKey keyPublic = keyFactory.generatePublic(keySpec);
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
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PrivateKey keyPrivate = keyFactory.generatePrivate(keySpec);
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
    public static String decryptByPrivateKeySplit(byte[] encryptedData, PrivateKey privateKey) {
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
        return new String(decryptedData);
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      明文
     * @param publicKey 公钥
     */
    public static String encryptByPublicKeySplit(byte[] data, PublicKey publicKey) {
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
        return Base64.getEncoder().encodeToString(encryptedData);
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
        String cipherText2 = encryptByPublicKeySplit(new Gson().toJson(list).getBytes(), publicKey);
        System.out.println(cipherText2);
        System.out.println(decryptByPrivateKeySplit(Base64.getDecoder().decode(cipherText2), privateKey));
    }

}
