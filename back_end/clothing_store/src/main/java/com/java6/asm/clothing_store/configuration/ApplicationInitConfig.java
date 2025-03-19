package com.java6.asm.clothing_store.configuration;

import com.java6.asm.clothing_store.constance.RoleEnum;
import com.java6.asm.clothing_store.constance.StatusEnum;
import com.java6.asm.clothing_store.constance.TypeAccountEnum;
import com.java6.asm.clothing_store.entity.User;
import com.java6.asm.clothing_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                User user = User.builder()
                        .username("admin")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .role(RoleEnum.ADMIN)
                        .status(StatusEnum.ACTIVE) // Set giá trị mặc định rõ ràng
                        .type(TypeAccountEnum.SYSTEM)
                        .build();
                userRepository.save(user);
                log.warn("Admin user added with default password: admin. Please change!");
            }
            log.info("Application initialization completed .....");
        };
    }
}
