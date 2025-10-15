package com.berkay.pollbackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT token üretimi, doğrulaması ve içinden kullanıcı bilgisi çıkarımı için kullanılan yardımcı sınıf.
 */
@Component
public class JwtTokenProvider {

    // Loglama için SLF4J kullanımı
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    // application.properties içinden gelen gizli anahtar
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    // Token geçerlilik süresi (milisaniye cinsinden)
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Kullanıcı kimliğiyle JWT token üretir.
     *
     * @param authentication Spring Security'den gelen kimlik doğrulama nesnesi
     * @return imzalanmış JWT token
     */
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);


        return Jwts.builder()
                .subject(Long.toString(userPrincipal.getId())) // Token'ın subject alanına kullanıcı ID'si yazılır
                .issuedAt(now)                                 // Token oluşturulma zamanı
                .expiration(expiryDate)                        // Token geçerlilik süresi
                .signWith(getSignKey())                           // Token imzalanır
                .compact();                                    // Token string olarak döndürülür
    }

    /**
     * JWT içinden kullanıcı ID'sini çıkarır.
     *
     * @param token gelen JWT token
     * @return kullanıcı ID'si (Long)
     */
    public Long getUserIdFromJWT(String token) {
        // Token doğrulanır ve içeriği (claims) alınır
        Claims claims = Jwts.parser()
                .verifyWith(getSignKey()) // imza doğrulaması yapılır
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Subject alanı kullanıcı ID'si olarak döner
        return Long.parseLong(claims.getSubject());
    }

    /**
     * Token geçerli mi diye kontrol eder.
     *
     * @param authToken gelen JWT token
     * @return geçerliyse true, değilse false
     */
    public boolean validateToken(String authToken) {
        try {
            // Token imzası ve yapısı doğrulanır
            Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT Signature"); // İmza uyuşmuyor
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT Token"); // Token yapısı bozuk
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT Token"); // Token süresi dolmuş
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT Token"); // Desteklenmeyen format
        } catch (IllegalArgumentException e) {
            logger.error("JWT Claims string is empty"); // Token boş veya null
        }
        return false;
    }
}