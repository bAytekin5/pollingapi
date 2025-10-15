package com.berkay.pollbackend.model.role;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

/**
 * Role entity'si, kullanıcıların sahip olabileceği yetkileri temsil eder.
 * Örneğin: ROLE_USER, ROLE_ADMIN gibi roller.
 * Uygulama genelinde yetkilendirme işlemlerinde kullanılır.
 */
@Entity
@Table(name = "roles")
// Entity'nin veritabanında "roles" adlı tabloya karşılık gelmesini sağlar.
// Tablo adı açıkça belirtilerek veritabanı ile uygulama arasında tutarlılık sağlanır.
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Birincil anahtar alanıdır. Otomatik olarak veritabanı tarafından üretilir (auto-increment).
    // GenerationType.IDENTITY → MySQL gibi veritabanlarında yaygın olarak kullanılır.
    private Long id;

    @Enumerated(EnumType.STRING)
    // Enum değerinin veritabanında string olarak saklanmasını sağlar.
    // Böylece veritabanında okunabilir değerler (örneğin "ROLE_USER") tutulur.

    @NaturalId
    // Hibernate tarafından doğal anahtar olarak işaretlenir.
    // Uygulama içinde bu alan üzerinden benzersiz sorgulama yapılabilir (örneğin: findByName).
    // Genellikle business-meaning taşıyan alanlar için kullanılır.

    @Column(length = 60)
    // Veritabanı sütunu için maksimum karakter uzunluğu belirlenir.
    // Enum değerlerinin uzunluğu sınırlanarak veri bütünlüğü ve performans korunur.
    private RoleName name;

    /**
     * Parametresiz constructor → JPA tarafından nesne oluşturulurken zorunludur.
     * Reflection ile çalıştığı için mutlaka tanımlanmalıdır.
     */
    public Role() {
    }

    /**
     * İş kurallarına göre Role nesnesi oluşturmak için kullanılan constructor.
     * Genellikle rol atama işlemlerinde kullanılır.
     *
     * @param name Enum olarak tanımlanmış rol adı (örneğin RoleName.ROLE_USER)
     */
    public Role(RoleName name) {
        this.name = name;
    }

    // Getter/setter metodları: Entity alanlarına erişim ve güncelleme sağlar.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}