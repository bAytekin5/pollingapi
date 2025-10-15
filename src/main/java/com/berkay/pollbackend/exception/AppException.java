package com.berkay.pollbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Uygulama genelinde fırlatabileceğim özel exception sınıfı.
 * Genelde beklenmeyen durumlarda ya da kendi belirlediğim hata senaryolarında kullanıyorum.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {

    /**
     * Sadece hata mesajı vermek istediğim durumlarda bunu kullanıyorum.
     */
    public AppException(String message) {
        super(message);
    }

    /**
     * Hem hata mesajını hem de altında yatan asıl nedeni (cause) iletmek istediğimde bu yapıyı kullanıyorum.
     * Örneğin bir service içinde başka bir exception yakalayıp sarmalamam gerekirse.
     */
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
