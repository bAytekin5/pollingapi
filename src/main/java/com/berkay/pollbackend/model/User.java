package com.berkay.pollbackend.model;

import com.berkay.pollbackend.model.audit.DateAudit;
import com.berkay.pollbackend.model.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

/**
 * User entity'si, sistemdeki her bir kullanıcıyı temsil eder.
 * JPA tarafından "users" adlı tabloya karşılık gelir.
 * Kimlik doğrulama, yetkilendirme ve kullanıcıya özel işlemler bu model üzerinden yürütülür.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
// Veritabanı düzeyinde username ve email alanlarının benzersiz olmasını garanti eder.
// Bu, kullanıcı kayıtlarında veri bütünlüğü ve kimlik doğrulama süreçleri için kritiktir.
public class User extends DateAudit {
    // DateAudit sınıfından miras alarak createdAt ve updatedAt alanlarını otomatik olarak kullanır.
    // Böylece kullanıcıların oluşturulma ve güncellenme zamanları merkezi şekilde takip edilir.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Birincil anahtar alanıdır. Veritabanı tarafından otomatik olarak üretilir (auto-increment).
    // GenerationType.IDENTITY → MySQL gibi veritabanlarında yaygın olarak kullanılır.
    private Long id;

    @NotBlank
    @Size(max = 40)
    // Kullanıcının tam adı. Boş olamaz ve maksimum 40 karakterle sınırlandırılmıştır.
    // UI/UX ve veri doğrulama açısından önemlidir.
    private String name;

    @NotBlank
    @Size(max = 15)
    // Kullanıcının sistem içindeki benzersiz kullanıcı adı.
    // Maksimum uzunluk sınırı ile hem performans hem de veri tutarlılığı sağlanır.
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    // Kullanıcının e-posta adresi. Doğal anahtar olarak işaretlenmiştir.
    // @Email → format doğrulaması sağlar. @NaturalId → business logic açısından benzersizdir.
    private String email;

    @NotBlank
    @Size(max = 100)
    // Kullanıcının şifresi. Boş olamaz ve maksimum uzunluk sınırı vardır.
    // Gerçek uygulamalarda bu alan hashlenmiş olarak saklanmalıdır (örneğin BCrypt).
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    // Kullanıcı ile roller arasında çoktan çoğa ilişki tanımlanır.
    // Ara tablo: user_roles → kullanıcı ve rol eşleşmelerini tutar.
    // FetchType.LAZY → roller sadece ihtiyaç duyulduğunda yüklenir, performans için önerilir.
    private Set<Role> roles = new HashSet<>();

    /**
     * Parametresiz constructor → JPA tarafından nesne oluşturulurken zorunludur.
     * Reflection ile çalıştığı için mutlaka tanımlanmalıdır.
     */
    public User() {
    }

    /**
     * Domain modelini oluşturmak için kullanılan constructor.
     * Genellikle servis katmanında yeni kullanıcı oluştururken kullanılır.
     *
     * @param name     Kullanıcının adı
     * @param username Kullanıcı adı
     * @param email    E-posta adresi
     * @param password Şifre (hashlenmiş olması beklenir)
     */
    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getter/setter metodları: Entity alanlarına erişim ve güncelleme sağlar.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}