package com.java6.asm.clothing_store.repository;

import com.java6.asm.clothing_store.entity.Order;
import com.java6.asm.clothing_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUser(User user);

}