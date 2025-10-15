package com.berkay.pollbackend.dto.jwt;

/**
 * JWT tabanlı kimlik doğrulama sonrası istemciye dönen yanıt (response) nesnesi.
 *
 * <p>Başarılı kimlik doğrulama işleminin ardından, sunucu tarafından oluşturulan
 * erişim jetonu (access token) ve token türü bilgilerini içerir. İstemci bu token'ı
 * sonraki API isteklerinde Authorization başlığında gönderir.</p>
 *
 * <p>Örnek JSON yanıt:</p>
 * <pre>
 * {
 *   "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0Ijox...",
 *   "tokenType": "Bearer"
 * }
 * </pre>
 */
public class JwtAuthenticationResponse {

    /**
     * Sunucu tarafından üretilen JWT erişim jetonu.
     * <p>
     * - Kimliği doğrulanmış kullanıcıyı temsil eder. <br>
     * - Belirli bir süre (expiration) sonunda geçerliliğini yitirir. <br>
     * - İstemci tarafından genellikle şu şekilde kullanılır: <br>
     * {@code Authorization: Bearer <accessToken>}
     * </p>
     */
    private String accessToken;

    /**
     * Token türü (genellikle "Bearer").
     * <p>
     * - Authorization başlığında token’ın türünü belirtir. <br>
     * - Varsayılan olarak “Bearer” değerine sahiptir.
     * </p>
     */
    private String tokenType = "Bearer";

    /**
     * Kimlik doğrulama sonrasında oluşturulan JWT token’ı alır.
     *
     * @param accessToken üretilen JWT erişim jetonu
     */
    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    // Getters / Setters

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
