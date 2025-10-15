package com.berkay.pollbackend;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.util.TimeZone;

@SpringBootApplication
// Uygulamanın başlangıç noktasıdır. Spring Boot'un otomatik yapılandırma, bileşen tarama ve konfigürasyon özelliklerini etkinleştirir.
@EntityScan(basePackageClasses = {
        PollApplication.class,
        Jsr310JpaConverters.class
})
// JPA entity taramasını özelleştirir. Hem uygulama sınıfının bulunduğu paket hem de Java 8 tarih/zaman API dönüşümleri için gerekli converter sınıfı taramaya dahil edilir.
public class PollApplication {

    @PostConstruct
        // Uygulama başlatıldığında çalışacak olan metottur. JVM düzeyinde zaman dilimi ayarı yapılır.
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // Tüm tarih/zaman işlemlerinin UTC zaman diliminde çalışmasını sağlar.
        // Bu ayar, global uygulamalarda zaman tutarlılığı ve veri bütünlüğü açısından  öneme sahiptir.
    }

    public static void main(String[] args) {
        SpringApplication.run(PollApplication.class, args);
        // Spring Boot uygulamasını başlatır. Tüm konfigürasyonlar, bean tanımları ve lifecycle işlemleri burada tetiklenir.
    }
}