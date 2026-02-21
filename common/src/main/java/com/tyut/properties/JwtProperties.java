package com.tyut.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tyut.jwt")
public class JwtProperties {
    private String secretKey;
    private Long ttl;
    // admin
    private String adminTokenName;
    // resident
    private String residentTokenName;
    // doctor
    private String doctorTokenName;
}
