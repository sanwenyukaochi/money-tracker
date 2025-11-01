package org.secure.security.authentication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.secure.security.authentication.handler.login.UserLoginInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtService {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String BEARER_PREFIX = "Bearer ";
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public String generateTokenFromUsername(String username, UserLoginInfo userInfo, long expiredTime) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(userInfo);
            return Jwts.builder()
                    .subject(username)
                    .claim("user", json)
                    .issuedAt(new Date())
                    .expiration(new Date((new Date()).getTime() + expiredTime))
                    .signWith(key())
                    .compact();
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize UserLoginInfo to JSON for JWT", e);
        }
        return null;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public UserLoginInfo validateJwtToken(String authToken, Class<UserLoginInfo> userLoginInfoClass) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Jws<Claims> claimsJws = Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            Claims claims = claimsJws.getPayload();
            String json = claims.get("user", String.class);
            return objectMapper.readValue(json, userLoginInfoClass);
        } catch (MalformedJwtException e) {
            log.error("JWT Token 无效: {}", e.getMessage());
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("JWT 已过期: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("JWT 不受支持: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("JWT 内容为空: {}", e.getMessage());
            throw e;
        } catch (JsonProcessingException e) {
            log.error("JWT 用户信息解析失败: {}", e.getMessage());
            throw e;
        }
    }
}
