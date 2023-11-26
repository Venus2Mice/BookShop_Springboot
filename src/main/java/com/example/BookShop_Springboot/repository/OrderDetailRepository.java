package com.example.BookShop_Springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.BookShop_Springboot.model.OrderDetail;
import com.example.BookShop_Springboot.model.Order;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{
    @Query("select o from Order o where o.customer.id = ?1")
    List<Order> findAllByCustomerId(Long id);
}
