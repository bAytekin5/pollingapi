package com.berkay.pollbackend.config;

import com.berkay.pollbackend.security.CustomerUserDetailsService;
import com.berkay.pollbackend.security.JwtAuthenticationEntryPoint;
import com.berkay.pollbackend.security.JwtAuthenticationFilter;
import com.berkay.pollbackend.security.JwtTokenProvider;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Uygulamanın güvenlik yapılandırmasını tanımlar.
 * Spring Security'nin modern yaklaşımı olan SecurityFilterChain ile filtreler, yetkilendirme ve oturum yönetimi yapılandırılır.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
// Web güvenliğini ve metot bazlı yetkilendirmeyi aktif eder.
public class SecurityConfig {

    // Kullanıcı bilgilerini veritabanından çekmek için özel UserDetailsService implementasyonu
    private final CustomerUserDetailsService customUserDetailsService;

    // Yetkisiz erişimlerde tetiklenen giriş noktası (401 döner)
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructor injection: Spring tarafından otomatik enjekte edilir.
     * Güvenlik bileşenleri burada bağlanır.
     */
    public SecurityConfig(CustomerUserDetailsService customUserDetailsService,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
            , JwtTokenProvider jwtTokenProvider) {

        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * JWT doğrulama filtresi: Her istekte token'ı kontrol eder.
     * UsernamePasswordAuthenticationFilter'dan önce çalıştırılır.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(customUserDetailsService, jwtTokenProvider);
    }

    /**
     * Şifreleri güvenli şekilde hashlemek için BCrypt kullanılır.
     * Spring Security'nin önerdiği varsayılan encoder'dır.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Kimlik doğrulama sağlayıcısı: UserDetailsService ve PasswordEncoder'ı bağlar.
     * Login işlemlerinde kullanıcı doğrulaması burada gerçekleşir.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * AuthenticationManager bean'i: Login işlemlerinde kimlik doğrulama için kullanılır.
     * WebSecurityConfigurerAdapter artık kullanılmadığı için doğrudan bean olarak tanımlanır.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Uygulamanın güvenlik filtre zincirini tanımlar.
     * Hangi endpoint'lerin serbest, hangilerinin korumalı olduğunu belirtir.
     * JWT filtresi zincire eklenir, oturum yönetimi stateless yapılır.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Statik içerik (css/js/img/webjars/favicon vs) için önerilen yol
                        .requestMatchers(String.valueOf(PathRequest.toStaticResources().atCommonLocations())).permitAll()

                        // SPA ana sayfa vs.
                        .requestMatchers("/", "/index.html", "/favicon.ico").permitAll()

                        // Auth ve public API’lerin
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/user/checkUsernameAvailability",
                                "/api/user/checkEmailAvailability").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**").permitAll()

                        // gerisi auth ister
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
//    public SecurityFilterChain securityFilterChain(HttpSecurity http,
//                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
//
//        return http
//                // CORS (varsayılan ayarla; özel config'in varsa configurationSource ver)
//                .cors(Customizer.withDefaults())
//
//                // CSRF: stateless API için kapalı
//                .csrf(csrf -> csrf.disable())
//
//                // Yetkisiz erişim handler'ı
//                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
//
//                // Oturum tutulmasın
//                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//                // Yetkilendirme kuralları
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg",
//                                "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                // JWT filtresi: UsernamePasswordAuthenticationFilter'dan önce
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//
//                .build();
//    }
}