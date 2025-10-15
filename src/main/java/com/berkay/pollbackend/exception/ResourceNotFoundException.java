package com.berkay.pollbackend.exception;

/**
 * Veritabanında veya sistemde aradığım bir kaynağı bulamadığımda bu hatayı fırlatıyorum.
 * Örneğin: "User not found with id : 5" gibi durumlarda.
 * Genellikle Service katmanında repository sorgularında kullanılır.
 */
public class ResourceNotFoundException extends RuntimeException {

    // Hangi entity veya tablo tipinde arama yapıldığını tutuyorum (örnek: "User", "Post" vs.)
    private String resourceName;

    // Hangi alana göre arama yapıldığını (örnek: "id", "username" vs.)
    private String fieldName;

    // O alandaki değeri (örnek: 5, "username" vs.)
    private Object fieldValue;

    /**
     * Kaynak bulunamadığında detaylı bir mesajla exception oluşturuyorum.
     * Mesaj otomatik olarak şu formatta geliyor:
     * "User not found with id : 5"
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
