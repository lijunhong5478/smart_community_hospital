package com.tyut.utils;

import com.tyut.exception.BaseException;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
public class JwtUtil {

    private JwtUtil() {}

    //    secret-key: tyut
    public static String generateToken(Integer role,
                                       Long id,
                                       String secretKey,
                                       Long ttl) {

        return Jwts.builder()
                .claim("role", role)
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ttl))
                .signWith(
                        SignatureAlgorithm.HS256,
                        secretKey.getBytes()
                )
                .compact();
    }

    public static Claims parseToken(String token, String secretKey) {

        try {
            return Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            throw new BaseException("Token已过期");
        } catch (JwtException e) {
            throw new BaseException("Token无效");
        }
    }
}
