package com.berkay.pollbackend.security;

import com.berkay.pollbackend.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Spring Security'nin kimlik doğrulama ve yetkilendirme süreçlerinde kullandığı kullanıcı temsilcisidir.
 * Domain model olan User nesnesini, güvenlik sisteminin anlayacağı UserDetails formatına dönüştürür.
 */
public class UserPrincipal implements UserDetails {

    private Long id;
    private String name;
    private String username;

    @JsonIgnore // API çıktısında email gösterilmesin (gizlilik için)
    private String email;

    @JsonIgnore // Şifre hiçbir zaman dışa aktarılmamalı (güvenlik için)
    private String password;

    // Kullanıcının rol ve yetkileri (örneğin ROLE_USER, ROLE_ADMIN)
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor: Tüm alanları set eder, immutable yapı sağlar.
     * Bu sınıf dışarıdan sadece create() metodu ile oluşturulmalıdır.
     */
    public UserPrincipal(Long id, String name, String username, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Factory metodu: Veritabanındaki User entity'sini alır ve UserPrincipal nesnesine dönüştürür.
     * Spring Security'nin kullanabileceği formatta yetkileri (GrantedAuthority) hazırlar.
     *
     * @param user Veritabanından gelen domain model
     * @return UserPrincipal → Spring Security'nin anlayacağı kullanıcı temsili
     */
    public static UserPrincipal create(User user) {
        // Kullanıcının rollerini Spring Security'nin anlayacağı yetki formatına dönüştürür
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())) // Enum'dan rol adı alınır
                .collect(Collectors.toList());

        // Tüm alanları set ederek yeni bir UserPrincipal oluşturur
        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    // JWT token üretimi gibi yerlerde kullanılabilecek getter'lar
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Spring Security'nin yetkilendirme için çağırdığı metot.
     * Kullanıcının sahip olduğu roller burada döner.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Kimlik doğrulama için gerekli alanlar
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // Hesap süresi dolmuş mu? true → aktif
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Hesap kilitli mi? true → açık
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Şifre süresi dolmuş mu? true → geçerli
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Hesap aktif mi? true → kullanılabilir
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Eşitlik kontrolü: Kimlik doğrulamada ve cache işlemlerinde önemlidir.
     * Sadece ID'ye göre karşılaştırma yapılır.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal other = (UserPrincipal) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}