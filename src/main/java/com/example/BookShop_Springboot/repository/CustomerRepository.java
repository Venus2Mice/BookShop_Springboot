package com.example.BookShop_Springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.BookShop_Springboot.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    
    Customer findByUsername(String username);

    Customer findByEmail(String email);

    @Query(value = "select * from customer", nativeQuery = true)
    List<Customer> findALl();
}
