package com.java6.asm.clothing_store.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .filename("key.env") // Chỉ định tên file là key.env
                .directory("./") // Đường dẫn đến file (thư mục gốc của project)
                .load();
    }
}