package com.berkay.pollbackend.security;

import com.berkay.pollbackend.model.User;
import com.berkay.pollbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security tarafından kimlik doğrulama işlemlerinde kullanılan özel UserDetailsService implementasyonudur.
 * Kullanıcıyı username, email veya ID ile veritabanından bulur ve UserPrincipal nesnesine dönüştürür.
 */
@Service
public class CustomerUserDetailsService implements UserDetailsService {

    // UserRepository: Veritabanından kullanıcı bilgilerini çekmek için kullanılır.
    private final UserRepository userRepository;

    /**
     * Constructor injection: Spring tarafından otomatik olarak enjekte edilir.
     * Test edilebilirlik ve bağımlılık yönetimi açısından tercih edilir.
     */
    public CustomerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Kullanıcıyı username veya email ile veritabanından bulur.
     * Spring Security, login sırasında bu metodu çağırır.
     *
     * @param usernameOrEmail Kullanıcının girdiği username veya email
     * @return UserDetails → Spring Security'nin kullanacağı kimlik bilgileri
     * @throws UsernameNotFoundException → Kullanıcı bulunamazsa fırlatılır
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        // Veritabanında username veya email'e göre kullanıcı aranıyor
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : ")
                );

        // UserPrincipal: User nesnesini Spring Security'nin anlayacağı forma dönüştürür
        return UserPrincipal.create(user);
    }

    /**
     * Kullanıcıyı ID ile veritabanından bulur.
     * Genellikle JWT token içindeki kullanıcı ID'si ile çağrılır.
     *
     * @param id Kullanıcının veritabanındaki benzersiz ID'si
     * @return UserDetails → Spring Security'nin kullanacağı kimlik bilgileri
     * @throws UsernameNotFoundException → Kullanıcı bulunamazsa fırlatılır
     */
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with id : " + id)
                );

        return UserPrincipal.create(user);
    }
}