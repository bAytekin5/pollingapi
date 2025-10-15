package com.berkay.pollbackend.dto.login;

import jakarta.validation.constraints.NotBlank;

/**
 * Kullanıcı giriş (login) isteğini temsil eden DTO sınıfı.
 *
 * <p>Bu sınıf, istemciden gelen kimlik bilgilerini (kullanıcı adı veya e-posta + şifre)
 * taşır ve Spring Validation aracılığıyla doğrulama kurallarını uygular.</p>
 *
 * <p>Controller katmanında {@code @Valid} anotasyonu ile birlikte kullanıldığında,
 * eksik veya geçersiz alanlarda otomatik olarak 400 (Bad Request) hatası üretilir.</p>
 */
public class LoginRequest {

    /**
     * Kullanıcının sisteme giriş için kullandığı kimlik bilgisi.
     * <p>
     * - Boş bırakılamaz. <br>
     * - Kullanıcı adı veya e-posta olarak kullanılabilir. <br>
     * - Backend tarafında hangi formatta geldiği kontrol edilerek
     * ilgili kullanıcı hesabı bulunur.
     * </p>
     */
    @NotBlank
    private String usernameOrEmail;

    /**
     * Kullanıcının şifresi.
     * <p>
     * - Boş bırakılamaz. <br>
     * - Şifre hiçbir zaman loglanmamalı veya açık biçimde saklanmamalıdır. <br>
     * - Controller veya Service katmanında alınan bu değer,
     * authentication mekanizmasına iletilmeden önce yalnızca geçici olarak tutulur.
     * </p>
     */
    @NotBlank
    private String password;

    /**
     * Kullanıcı adı veya e-posta bilgisinin getter metodu.
     */
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    /**
     * Kullanıcı adı veya e-posta bilgisinin setter metodu.
     */
    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    /**
     * Şifre alanının getter metodu.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Şifre alanının setter metodu.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
