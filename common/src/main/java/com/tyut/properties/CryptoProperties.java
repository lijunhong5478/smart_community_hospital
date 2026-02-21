package com.tyut.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tyut.crypto")
public class CryptoProperties {
    private String aesKey;
}
