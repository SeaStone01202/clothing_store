package com.java6.asm.clothing_store.service.authentication.token;

public interface TokenService {

    String generateToken(String username);

    String validateToken(String token);

    void deleteToken(String token);
}