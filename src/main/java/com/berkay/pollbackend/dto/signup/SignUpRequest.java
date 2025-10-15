package com.berkay.pollbackend.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Kullanıcı kayıt (sign-up) işlemleri için istek (DTO) nesnesi.
 *
 * <p>Bu sınıf, istemciden gelen kayıt bilgilerini taşır ve
 * {@code @Valid} anotasyonu ile birlikte çalışarak alan doğrulamalarını sağlar.</p>
 *
 * <p>Spring Boot / Spring Validation tarafından otomatik olarak
 * doğrulama yapılır; eksik veya hatalı alanlarda 400 (Bad Request) döner.</p>
 */
public class SignUpRequest {

    /**
     * Kullanıcının tam adı.
     * <p>
     * - Boş bırakılamaz. <br>
     * - Minimum 4, maksimum 40 karakter uzunluğunda olmalıdır.
     * </p>
     */
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    /**
     * Kullanıcı adı (benzersiz olmalı).
     * <p>
     * - Boş bırakılamaz. <br>
     * - Minimum 3, maksimum 15 karakter uzunluğunda olmalıdır. <br>
     * - Genellikle sistemde login kimliği olarak kullanılır.
     * </p>
     */
    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    /**
     * Kullanıcının e-posta adresi.
     * <p>
     * - Boş bırakılamaz. <br>
     * - Maksimum 40 karakter uzunluğunda olmalıdır. <br>
     * - Format kontrolü {@link Email} anotasyonu ile yapılır.
     * </p>
     */
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    /**
     * Kullanıcının şifresi.
     * <p>
     * - Boş bırakılamaz. <br>
     * - Minimum 6, maksimum 20 karakter olmalıdır. <br>
     * - Hashlenmeden önce bu alan yalnızca DTO katmanında bulunur;
     * Entity katmanına aktarılmadan önce şifrelenmelidir.
     * </p>
     */
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    /**
     * Ad alanının getter metodu.
     */
    public String getName() {
        return name;
    }

    /**
     * Ad alanının setter metodu.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Kullanıcı adı getter metodu.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Kullanıcı adı setter metodu.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * E-posta getter metodu.
     */
    public String getEmail() {
        return email;
    }

    /**
     * E-posta setter metodu.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Şifre getter metodu.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Şifre setter metodu.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
