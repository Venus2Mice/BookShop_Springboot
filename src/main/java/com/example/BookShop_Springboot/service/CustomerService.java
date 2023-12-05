package com.example.BookShop_Springboot.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.BookShop_Springboot.dto.CustomerDto;
import com.example.BookShop_Springboot.model.Customer;

public interface CustomerService {
    Customer save(CustomerDto customerDto);

    Customer findByUsername(String username);

    List<Customer> findALl();

    Customer update(CustomerDto customerDto);

    Customer changePass(CustomerDto customerDto);

    CustomerDto getCustomer(String email);

    Customer findByEmail(String email);

    void update(CustomerDto customerDto, MultipartFile img_avt);

    void disableById(Long id);

    void enableById(Long id);
}
