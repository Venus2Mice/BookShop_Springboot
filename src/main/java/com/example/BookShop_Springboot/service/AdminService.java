package com.example.BookShop_Springboot.service;

import com.example.BookShop_Springboot.dto.AdminDto;
import com.example.BookShop_Springboot.model.Admin;

public interface AdminService {
    Admin findByUsername(String username);

    Admin findByEmail(String email);

    Admin save(AdminDto adminDto);
}
