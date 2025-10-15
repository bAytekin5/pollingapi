package com.berkay.pollbackend.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * Spring Security context'inde kimliği doğrulanmış kullanıcının
 * doğrudan controller metot parametresi üzerinden alınmasını sağlayan özel anotasyon.
 *
 * <p>Kullanım örneği:</p>
 * <pre>
 *     @GetMapping("/me")
 *     public UserSummary getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
 *         return new UserSummary(userPrincipal.getId(), userPrincipal.getUsername());
 *     }
 * </pre>
 *
 * <p>Bu anotasyon, @AuthenticationPrincipal anotasyonuna bir sarmal (meta-annotation) olarak işlev görür.
 * Böylece Controller metodlarında doğrudan "CurrentUser" anlamına gelen okunabilir bir adla kullanılabilir.</p>
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
