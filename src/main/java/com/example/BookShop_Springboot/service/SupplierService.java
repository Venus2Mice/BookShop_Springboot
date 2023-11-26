package com.example.BookShop_Springboot.service;

import java.util.List;
import java.util.Optional;

import com.example.BookShop_Springboot.model.Supplier;

public interface SupplierService {
    Supplier save(Supplier supplier);

    Supplier update(Supplier supplier);

    List<Supplier> findAllByActivatedTrue();

    List<Supplier> findALl();

    Optional<Supplier> findById(Long id);

    void deleteById(Long id);

    void enableById(Long id);

    Supplier getFirst();
}
