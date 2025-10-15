package com.berkay.pollbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Her HTTP isteği için yalnızca bir kez çalışan filtre.
 * Gelen isteğin Authorization header'ında JWT varsa doğrular
 * ve kullanıcıyı Spring Security context'ine ekler.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    public JwtAuthenticationFilter(CustomerUserDetailsService customerUserDetailsService,
                                   JwtTokenProvider jwtTokenProvider) {
        this.customerUserDetailsService = customerUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            //  Authorization başlığından Bearer token'ı çek
            String jwt = getJwtFromRequest(request);

            //  Token mevcut ve yapısal olarak doluysa doğrula
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {

                //  Token'dan kullanıcı ID'sini çıkar
                Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);

                //  Veritabanından kullanıcı detaylarını yükle (UserDetails)
                UserDetails userDetails = customerUserDetailsService.loadUserById(userId);

                //  Kullanıcı detaylarıyla authentication nesnesi oluştur
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,          // kimlik bilgisi
                                null,                 // şifre (JWT ile geldiği için gerek yok)
                                userDetails.getAuthorities()); // roller / yetkiler

                //  Ek bağlam bilgisi (IP, session, header vs.) ekle
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContext'e authentication bilgisini yerleştir
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        } catch (Exception e) {
            // Filtrede oluşan tüm hataları logla (ör. expired token, malformed header vs.)
            logger.error("Could not set user authentication in security context", e);
        }

        // Zincirdeki bir sonraki filtreye geç (her koşulda çağrılmalı)
        filterChain.doFilter(request, response);
    }

    /**
     * Authorization header'ından "Bearer <token>" formatındaki JWT'yi alır.
     * Header yoksa veya yanlış biçimdeyse null döner.
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Header varsa ve "Bearer " ile başlıyorsa token kısmını ayıkla
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // substring(7) → "Bearer " kısmını at
            return bearerToken.substring(7);
        }

        // Token bulunamadıysa null dön
        return null;
    }
}
