package com.berkay.pollbackend.dto.api;

/**
 * API tarafında dönen standart cevap yapısı.
 * Başarılı veya hatalı işlemler için tek tip bir response dönmek amacıyla kullanıyorum.
 */
public class ApiResponse {

    /**
     * İşlem başarılı mı değil mi bunu tutuyor.
     * true → işlem başarılı
     * false → bir hata veya başarısız durum var
     */
    private Boolean success;

    /**
     * İşlem sonucuyla ilgili mesaj.
     * Örn: "Kayıt başarılı", "Kullanıcı bulunamadı" gibi açıklamalar burada dönüyor.
     */
    private String message;

    /**
     * ApiResponse nesnesini oluştururken success ve message bilgilerini veriyorum.
     */
    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
