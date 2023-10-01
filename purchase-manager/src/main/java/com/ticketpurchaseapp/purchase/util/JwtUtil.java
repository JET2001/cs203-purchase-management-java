package com.ticketpurchaseapp.purchase.util;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private static String secretKey;
    private static final long TOKEN_VALIDITY_MILLIS = 60 * 60 * 1000; // 1 hour in milliseconds
    private static final long TOKEN_EXPIRATION_MILLIS = 20 * 60 * 1000; // 20 minutes in milliseconds

    @Value("${jwt.secretKey}")
    private void setSecret(String secretKey) {
        JwtUtil.secretKey = secretKey;
    }

    public static String generateToken(int userId) {
        return generateToken(String.valueOf(userId));
    }

    public static String generateToken(String userIdStr) { 
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);
        Key signingKey = Keys.hmacShaKeyFor(secretKeyBytes);

        Date now = new Date();
        Date tokenExpiration = new Date(now.getTime() + TOKEN_VALIDITY_MILLIS);
        
        return Jwts.builder()
                .setSubject(userIdStr)
                .setIssuedAt(now)
                .setExpiration(tokenExpiration)
                .signWith(signingKey, signatureAlgorithm)
                .compact();
    }

    public static String verifyToken(String token) throws JwtException {
        try {
            byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);
            Key signingKey = Keys.hmacShaKeyFor(secretKeyBytes);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }

    public static boolean isTokenAboutToExpire(String token) throws JwtException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Base64.getDecoder().decode(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            Date now = new Date();
            long timeDifferenceMillis = expirationDate.getTime() - now.getTime();
            return timeDifferenceMillis <= TOKEN_EXPIRATION_MILLIS;
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }
}