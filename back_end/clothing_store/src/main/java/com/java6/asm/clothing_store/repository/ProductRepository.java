package com.java6.asm.clothing_store.repository;

import com.java6.asm.clothing_store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
