package com.berkay.pollbackend.model.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

/**
 * Tüm entity'lerde ortak olarak kullanılabilecek tarih alanlarını barındıran soyut sınıftır.
 * Doğrudan bir tabloya karşılık gelmez; alt sınıflar tarafından miras alınır.
 * Genellikle createdAt ve updatedAt gibi audit alanlarını merkezi olarak tanımlamak için kullanılır.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
// Spring Data JPA'nın auditing mekanizmasını etkinleştirir.
// Entity lifecycle olaylarını dinleyerek createdAt ve updatedAt alanlarını otomatik olarak set eder.

@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt"},
        allowGetters = true
)
// JSON çıktısında bu alanlar gizlenir, ancak getter metodlarıyla erişilebilir.
// API veri sadeliği ve güvenlik açısından önemlidir.

public abstract class DateAudit implements Serializable {
    // Serializable interface: Nesnenin ağ üzerinden taşınabilir veya disk'e yazılabilir olmasını sağlar.
    // Genellikle JPA entity'lerinde versiyonlama, cache ve session işlemleri için önerilir.

    @CreatedDate
    @Column(nullable = false, updatable = false)
    // Entity ilk oluşturulduğunda otomatik olarak atanır.
    // updatable=false → bu alan sonradan değiştirilemez, veri bütünlüğü korunur.
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    // Entity her güncellendiğinde bu alan otomatik olarak güncellenir.
    // Veri değişim geçmişini izlemek ve audit logları oluşturmak için kullanılır.
    private Instant updatedAt;

    // Getter ve setter metodları: Spring tarafından alanlara erişim ve güncelleme için kullanılır.

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}