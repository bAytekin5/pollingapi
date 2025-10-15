package com.berkay.pollbackend.model.role;

/**
 * Kullanıcı rollerini sabit ve güvenli şekilde tanımlamak için kullanılan enum yapısıdır.
 * Uygulama genelinde yetkilendirme işlemlerinde kullanılır.
 * Enum kullanımı sayesinde string hataları önlenir, kod okunabilirliği ve güvenliği artar.
 */
public enum RoleName {

    ROLE_USER,  // Standart kullanıcı rolü: oy kullanma, profil görüntüleme gibi temel işlemler
    ROLE_ADMIN  // Yönetici rolü: sistem ayarları, kullanıcı yönetimi gibi yetkili işlemler
}