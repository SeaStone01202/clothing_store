package com.java6.asm.clothing_store.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(1)
public class PublicSecurityConfig {

    private String[] publicUrls = {
            "/auth/system/refresh",
            "/auth/system/login",
            "/user/register",
            "/product/list",
            "/product/**",
            "/category/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain refreshSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(publicUrls)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults());
        return http.build();
    }
}