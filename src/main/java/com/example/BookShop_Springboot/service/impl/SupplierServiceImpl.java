package com.example.BookShop_Springboot.service.impl;

import java.util.List;
import java.util.Optional;

import com.example.BookShop_Springboot.model.Supplier;
import org.springframework.stereotype.Service;

import com.example.BookShop_Springboot.repository.SupplierRepository;
import com.example.BookShop_Springboot.service.SupplierService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public Supplier save(Supplier supplier) {
        Supplier supplierSave = new Supplier();
        supplierSave.setName(supplier.getName());
        supplierSave.setPhoneNumber(supplier.getPhoneNumber());
        supplierSave.setAddress(supplier.getAddress());
        supplierSave.setEmail(supplier.getEmail());
        supplierSave.set_activated(true);
        supplierSave.set_deleted(false);
        return supplierRepository.save(supplierSave);
    }

    @Override
    public Supplier update(Supplier supplier) {
        Supplier supplierUpdate = supplierRepository.getReferenceById(supplier.getId());
        supplierUpdate.setName(supplier.getName());
        supplierUpdate.setPhoneNumber(supplier.getPhoneNumber());
        supplierUpdate.setAddress(supplier.getAddress());
        supplierUpdate.setEmail(supplier.getEmail());
        return supplierRepository.save(supplierUpdate);
    }

    @Override
    public List<Supplier> findAllByActivatedTrue() {
        return supplierRepository.findAllByActivatedTrue();
    }

    @Override
    public List<Supplier> findALl() {
        return supplierRepository.findAll();
    }

    @Override
    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        Supplier supplier = supplierRepository.getReferenceById(id);
        supplier.set_activated(false);
        supplier.set_deleted(true);
        supplierRepository.save(supplier);
    }

    @Override
    public void enableById(Long id) {
        Supplier supplier = supplierRepository.getReferenceById(id);
        supplier.set_activated(true);
        supplier.set_deleted(false);
        supplierRepository.save(supplier);
    }

    @Override
    public Supplier getFirst() {
        if(!supplierRepository.findAll().isEmpty())
            return supplierRepository.findAll().get(0);
        return supplierRepository.save(new Supplier("default"));
    }

}
