package com.example.BookShop_Springboot.repository;

import com.example.BookShop_Springboot.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long>{
    
}
