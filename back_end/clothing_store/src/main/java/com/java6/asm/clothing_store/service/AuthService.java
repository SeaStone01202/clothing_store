package com.java6.asm.clothing_store.service;

import com.java6.asm.clothing_store.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AuthService {

    Optional<User> findByUsername(String username);

}
