package com.java6.asm.clothing_store.repository;

import com.java6.asm.clothing_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
