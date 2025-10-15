package com.berkay.pollbackend.repository;

import com.berkay.pollbackend.model.role.Role;
import com.berkay.pollbackend.model.role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Role entity'si için veri erişim katmanıdır.
 * Spring tarafından otomatik olarak tanınır ve implement edilir.
 * Exception translation sağlar, yani veritabanı hatalarını Spring'in standart exception'larına dönüştürür.
 * Uygulama genelinde rol verilerine erişim için merkezi noktadır.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // JpaRepository → CRUD işlemleri + pagination + sorting desteği sağlar.
    // Long → Role entity'sinin birincil anahtar (ID) tipi.

    /**
     * Enum olarak tanımlanmış RoleName değerine göre Role nesnesini arar.
     * Genellikle kullanıcıya rol atama, yetkilendirme ve token üretimi gibi işlemlerde kullanılır.
     * Örneğin: ROLE_USER, ROLE_ADMIN gibi rollerin veritabanında olup olmadığını kontrol eder.
     *
     * @param name RoleName enum değeri (örneğin RoleName.ROLE_USER)
     * @return Optional<Role> → Eşleşen rol varsa döner, yoksa boş
     */
    Optional<Role> findByName(RoleName name);
}