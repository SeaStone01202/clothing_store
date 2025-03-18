package com.java6.asm.clothing_store.service.implement;

import com.java6.asm.clothing_store.entity.User;
import com.java6.asm.clothing_store.repository.UserRepository;
import com.java6.asm.clothing_store.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenServiceImpl implements AuthService {


    private UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        if (userRepository.findByUsername(username).isPresent())
            return Optional.of(userRepository.findByUsername(username).get());
        else
            return Optional.empty();
    }
}
