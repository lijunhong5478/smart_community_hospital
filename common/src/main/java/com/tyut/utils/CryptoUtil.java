package com.tyut.utils;

import com.tyut.properties.CryptoProperties;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

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
     * 批量重置密码工具方法 - 将所有用户密码重置为123456的MD5加密版本
     */
    public String generateDefaultEncryptedPassword() {
        return encodePassword("123456");
    }

    /**
     * 直接MD5加密（不加盐，用于兼容旧数据）
     */
    public String md5Encrypt(String content) {
        if (content == null) {
            throw new RuntimeException("内容不能为空");
        }
        return DigestUtils.md5Hex(content.getBytes(StandardCharsets.UTF_8));
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
}
