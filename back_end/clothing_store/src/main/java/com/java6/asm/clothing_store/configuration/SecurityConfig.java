package com.java6.asm.clothing_store.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@Order(2) // 🚀 Xử lý sau cấu hình dành cho refresh token
public class SecurityConfig {

    private final String[] PUBLIC_URLS = {
            "/auth/system/*",
            "/auth/google/*",
            "/auth/zalo/*",
    };
    /**
     * ✅ Cấu hình bảo mật cho ứng dụng Spring Security
     * - Ngăn chặn truy cập trái phép vào các API
     * - Bật xác thực bằng JWT cho các request
     * 🔹 Cấu hình cụ thể:
     * - Cho phép truy cập công khai vào các endpoint `/auth/login`, `/auth/refresh`, `/auth/logout`
     * - Mọi request khác đều yêu cầu xác thực
     * - Sử dụng OAuth2 Resource Server với JWT để xác thực người dùng
     * @param http Đối tượng cấu hình bảo mật của Spring Security
     * @return SecurityFilterChain - Chuỗi bộ lọc bảo mật đã được thiết lập
     * @throws Exception Nếu có lỗi trong quá trình cấu hình bảo mật
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // ❌ Tắt CSRF (vì API không dùng session)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/system/login",
//                                "/auth/system/refresh",  // ✅ Cho phép truy cập refresh token
                                "/auth/system/logout"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 🔥 Gắn EntryPoint xử lý lỗi 401
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/auth/google/success", true) // ✅ Xử lý khi đăng nhập Google thành công
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()) // 🛡️ Xác thực JWT cho tài khoản System
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
//                .addFilterBefore(jwtRequestFilter, AbstractPreAuthenticatedProcessingFilter.class); // 🚀 Thêm filter trước JWT

        ;

        return http.build();
    }




    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Áp dụng cho tất cả API
                        .allowedOrigins("http://localhost:5173") // Cho phép frontend truy cập
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Các phương thức HTTP được phép
                        .allowedHeaders("*") // Chấp nhận tất cả headers
                        .allowCredentials(true); // Cho phép gửi cookie (nếu cần)
            }
        };
    }


}