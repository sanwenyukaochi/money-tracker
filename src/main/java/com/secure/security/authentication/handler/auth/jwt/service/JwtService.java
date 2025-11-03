package com.secure.security.authentication.handler.auth.jwt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secure.security.common.web.constant.ResponseCodeConstants;
import com.secure.security.common.web.exception.BaseException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import com.secure.security.authentication.handler.auth.UserLoginInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    private final ObjectMapper objectMapper;

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

    public UserLoginInfo validateJwtToken(String authToken, Class<UserLoginInfo> userLoginInfoClass) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            Claims claims = claimsJws.getPayload();
            String json = claims.get("user", String.class);
            return objectMapper.readValue(json, userLoginInfoClass);
        } catch (MalformedJwtException e) {
            log.error("JWT Token 无效: {}", e.getMessage());
            throw new BaseException(ResponseCodeConstants.TOKEN_MALFORMED, "JWT Token 无效", HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            log.error("JWT 已过期: {}", e.getMessage());
            throw new BaseException(ResponseCodeConstants.TOKEN_EXPIRED, "JWT 已过期", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            log.error("JWT 不受支持: {}", e.getMessage());
            throw new BaseException(ResponseCodeConstants.TOKEN_UNSUPPORTED, "JWT 不受支持", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            log.error("JWT 内容为空: {}", e.getMessage());
            throw new BaseException(ResponseCodeConstants.TOKEN_EMPTY, "JWT 内容为空", HttpStatus.UNAUTHORIZED);
        } catch (JsonProcessingException e) {
            log.error("JWT 用户信息解析失败: {}", e.getMessage());
            throw new BaseException(ResponseCodeConstants.TOKEN_PARSE_ERROR, "JWT 用户信息解析失败", HttpStatus.UNAUTHORIZED);
        }
    }
}
