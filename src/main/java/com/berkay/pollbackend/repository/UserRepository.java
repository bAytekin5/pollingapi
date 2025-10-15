package com.berkay.pollbackend.repository;

import com.berkay.pollbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User entity'si için veri erişim katmanıdır.
 * Spring Data JPA tarafından otomatik olarak implement edilir.
 * Uygulama genelinde kullanıcı verilerine erişim için merkezi noktadır.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository → CRUD işlemleri + pagination + sorting desteği sağlar.
    // Long → User entity'sinin birincil anahtar tipi.

    /**
     * E-posta adresine göre kullanıcıyı arar.
     * Genellikle login, şifre sıfırlama ve profil işlemlerinde kullanılır.
     *
     * @param email Kullanıcının e-posta adresi
     * @return Optional<User> → Kullanıcı varsa döner, yoksa boş
     */
    Optional<User> findByEmail(String email);

    /**
     * Belirtilen ID listesi içinde eşleşen ilk kullanıcıyı döner.
     * Genellikle toplu işlem senaryolarında (örneğin yetkilendirme) kullanılır.
     * Not: Eğer birden fazla kullanıcı döndürülmek isteniyorsa `List<User> findAllByIdIn(...)` tercih edilmelidir.
     *
     * @param userIds Kullanıcı ID'leri listesi
     * @return Optional<User> → Eşleşen ilk kullanıcı
     */
    Optional<User> findByIdIn(List<Long> userIds);

    /**
     * Kullanıcı adına göre kullanıcıyı arar.
     * Genellikle login işlemlerinde veya profil görüntülemede kullanılır.
     *
     * @param username Kullanıcının benzersiz kullanıcı adı
     * @return Optional<User> → Kullanıcı varsa döner
     */
    Optional<User> findByUsername(String username);

    /**
     * Belirtilen kullanıcı adının sistemde var olup olmadığını kontrol eder.
     * Genellikle kayıt işlemlerinde benzersizlik kontrolü için kullanılır.
     *
     * @param username Kontrol edilecek kullanıcı adı
     * @return Boolean → true: varsa, false: yoksa
     */
    Boolean existsByUsername(String username);

    /**
     * Belirtilen e-posta adresinin sistemde var olup olmadığını kontrol eder.
     * Kayıt ve şifre sıfırlama gibi işlemlerde doğrulama için kullanılır.
     *
     * @param email Kontrol edilecek e-posta adresi
     * @return Boolean → true: varsa, false: yoksa
     */
    Boolean existsByEmail(String email);

    /**
     * Kullanıcıyı username veya email ile arar.
     * Genellikle login işlemlerinde kullanılır, kullanıcı hangi alanı girerse girsin eşleşme sağlanır.
     *
     * @param username Kullanıcı adı
     * @param email E-posta adresi
     * @return Optional<User> → Eşleşen kullanıcı varsa döner
     */
    Optional<User> findByUsernameOrEmail(String username, String email);
}