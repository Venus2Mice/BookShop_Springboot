package com.example.BookShop_Springboot.repository;

import com.example.BookShop_Springboot.model.Order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

    Optional<Order> findById(String id);
    
}
