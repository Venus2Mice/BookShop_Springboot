package com.example.BookShop_Springboot.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.BookShop_Springboot.dto.CustomerDto;
import com.example.BookShop_Springboot.model.Customer;

public interface CustomerService {
    Customer save(CustomerDto customerDto);

    Customer findByUsername(String username);

    Customer update(CustomerDto customerDto);

    Customer changePass(CustomerDto customerDto);

    CustomerDto getCustomer(String username);

    Customer findByEmail(String email);

    void update(CustomerDto customerDto, MultipartFile img_avt);
}
