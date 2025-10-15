package com.berkay.pollbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Kullanıcının gönderdiği istek hatalıysa (eksik parametre, yanlış format vs.)
 * bu exception'ı fırlatıyorum. Controller veya Service katmanında sık kullanılır.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Sadece hata mesajını iletmek istediğim durumlarda.
     * Örn: "Geçersiz e-posta adresi" gibi.
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Hem mesajı hem de asıl nedeni (cause) birlikte taşımam gerektiğinde.
     * Örn: başka bir exception'ı yakalayıp bu sınıfla sarmalamak istediğimde.
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
