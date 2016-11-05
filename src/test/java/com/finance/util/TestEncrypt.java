package com.finance.util;

import com.finance.util.myutil.EncryptUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zt on 2016/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring*.xml"})
public class TestEncrypt {

    @Test
    public void encodeMD5() {
        String MD5 = EncryptUtil.encodeMD5("This is a test");
        Assert.assertEquals("CE114E4501D2F4E2DCEA3E17B546F339", MD5.toUpperCase());
    }

    @Test
    public void encodeSHA1() {
        String SHA1 = EncryptUtil.encodeSHA1("This is a test");
        Assert.assertEquals("A54D88E06612D820BC3BE72877C74F257B561B19", SHA1.toUpperCase());
    }

    @Test
    public void encodeSHA256() {
        String SHA256 = EncryptUtil.encodeSHA256("This is a test");
        Assert.assertEquals("C7BE1ED902FB8DD4D48997C6452F5D7E509FBCDBE2808B16BCF4EDCE4C07D14E", SHA256.toUpperCase());
    }

    @Test
    public void encodeSHA512() {
        String SHA512 = EncryptUtil.encodeSHA512("This is a test");
        Assert.assertEquals("A028D4F74B602BA45EB0A93C9A4677240DCF281A1A9322F183BD32F0BED82EC72DE9C3957B2F4C9A1CCF7ED14F85D73498DF38017E703D47EBB9F0B3BF116F69", SHA512.toUpperCase());
    }

    @Test
    public void encodeBASE64() {
        String BASE64 = EncryptUtil.encodeBASE64("This is a test");
        Assert.assertEquals("VGhpcyBpcyBhIHRlc3Q=", BASE64);
    }

    @Test
    public void decodeBASE64() {
        String plaintext = EncryptUtil.decodeBASE64("VGhpcyBpcyBhIHRlc3Q=");
        Assert.assertEquals("This is a test", plaintext);
    }

    @Test
    public void encodeHmacMD5() {
        String key = EncryptUtil.getHmaMD5key();
        String HmacMD5 = EncryptUtil.encodeHmacMD5("This is a test", key);
        System.out.print("key :  " + key + "\nsha" + HmacMD5);
    }

    @Test
    public void encodeHmacSHA1() {
        String key = EncryptUtil.getHmaSHA1key();
        String HmacSHA1 = EncryptUtil.encodeHmacSHA1("This is a test", key);
        System.out.print("key :  " + key + "\nsha" + HmacSHA1);
    }

    @Test
    public void encodeHmacSHA256() {
        String key = EncryptUtil.getHmaSHA256key();
        String HmacSHA256 = EncryptUtil.encodeHmacSHA256("This is a test", key);
        System.out.print("key :  " + key + "\nsha" + HmacSHA256);
    }

    @Test
    public void encodeHmacSHA512() {
        String key = EncryptUtil.getHmaSHA512key();
        String HmacSHA512 = EncryptUtil.encodeHmacSHA512("This is a test", key);
        System.out.print("key :  " + key + "\nsha" + HmacSHA512);
    }

    @Test
    public void bcrypt() {
        String plaintext = "This is a test";
        String ciphertext = EncryptUtil.Bcrypt(plaintext);
        boolean flag = BCrypt.checkpw(plaintext, ciphertext);
        Assert.assertTrue(flag);
    }

}
