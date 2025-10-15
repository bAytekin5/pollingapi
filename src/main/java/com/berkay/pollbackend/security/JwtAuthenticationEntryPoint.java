package com.berkay.pollbackend.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Bu sınıf, kimlik doğrulama başarısız olduğunda tetiklenen giriş noktasıdır.
 * Spring Security'nin AuthenticationEntryPoint arayüzünü implement eder.
 * Genellikle JWT ile korunan endpoint'lere yetkisiz erişimlerde devreye girer.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Logger: Hataları ve olayları loglamak için kullanılır.
    // LoggerFactory ile sınıfın adını bağlayarak log çıktılarında kaynak görünür olur.
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    /**
     * Bu metot, yetkisiz bir kullanıcı korunan bir kaynağa erişmeye çalıştığında çağrılır.
     * HTTP 401 (Unauthorized) hatası döndürülür ve hata mesajı loglanır.
     *
     * @param request       İstek nesnesi (kimin erişmeye çalıştığını içerir)
     * @param response      Yanıt nesnesi (buradan hata kodu döndürülür)
     * @param authException Kimlik doğrulama hatası (neden başarısız olduğunu içerir)
     * @throws IOException      Giriş/Çıkış hatası olabilir
     * @throws ServletException Servlet kaynaklı hata olabilir
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // Hata loglanıyor: Yetkisiz erişim denemesi ve sebebi
        logger.error("Responding with unauthorized error. Message - {}", authException.getMessage());

        // HTTP 401 hatası döndürülüyor: Kullanıcı yetkisiz
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}