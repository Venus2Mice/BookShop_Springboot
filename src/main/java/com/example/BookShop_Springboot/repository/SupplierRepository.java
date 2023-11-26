package com.example.BookShop_Springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.BookShop_Springboot.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    @Query(value = "select * from suppliers where is_activated = true", nativeQuery = true)
    List<Supplier> findAllByActivatedTrue();
}
