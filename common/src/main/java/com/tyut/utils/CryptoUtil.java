package com.tyut.utils;

import com.tyut.properties.CryptoProperties;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class CryptoUtil {

    @Autowired
    private CryptoProperties cryptoProperties;

    private String salt;

    @PostConstruct
    public void init() {
        // 使用配置的AES密钥作为盐值
        this.salt = cryptoProperties.getAesKey();
    }

    /**
     * MD5加密密码（加盐）
     */
    public String encodePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }
        // 加盐加密：盐值 + 原始密码 + 盐值
        String saltedPassword = salt + rawPassword + salt;
        return DigestUtils.md5Hex(saltedPassword.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 验证密码是否匹配
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        String encryptedInput = encodePassword(rawPassword);
        return encryptedInput.equals(encodedPassword);
    }

    /**
     * 检查密码是否已加密（MD5格式）
     */
    public boolean isEncoded(String password) {
        if (password == null || password.length() != 32) {
            return false;
        }
        // MD5加密后的字符串长度为32位十六进制字符
        return password.matches("[0-9a-fA-F]{32}");
    }

    /**
     * AES加密身份证号码
     */
    public String encodeIdCard(String rawIdCard) {
        try {
            if (rawIdCard == null || rawIdCard.isEmpty()) {
                throw new RuntimeException("身份证号码不能为空");
            }
            
            // 使用MD5将盐值转换为16字节的AES密钥
            byte[] keyBytes = DigestUtils.md5(salt.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            byte[] encryptedBytes = cipher.doFinal(rawIdCard.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("AES加密身份证失败", e);
        }
    }

    /**
     * AES解密身份证号码
     */
    public String decodeIdCard(String encryptedIdCard) {
        try {
            if (encryptedIdCard == null || encryptedIdCard.isEmpty()) {
                throw new RuntimeException("加密身份证号码不能为空");
            }
            
            // 使用MD5将盐值转换为16字节的AES密钥
            byte[] keyBytes = DigestUtils.md5(salt.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedIdCard));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密身份证失败", e);
        }
    }

    /**
     * 验证身份证号码是否匹配
     */
    public boolean matchesIdCard(String rawIdCard, String encodedIdCard) {
        if (rawIdCard == null || encodedIdCard == null) {
            return false;
        }
        try {
            String decryptedIdCard = decodeIdCard(encodedIdCard);
            return rawIdCard.equals(decryptedIdCard);
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 测试加密解密是否对应的工具方法
     */
    public boolean testEncryption(String originalText) {
        try {
            String encrypted = encodePassword(originalText);
            return matches(originalText, encrypted);
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 检查身份证是否为AES加密格式
     */
    public boolean isIdCardEncrypted(String idCard) {
        if (idCard == null || idCard.isEmpty()) {
            return false;
        }
        try {
            // 尝试解码Base64，如果成功可能是AES加密的
            Base64.getDecoder().decode(idCard);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
