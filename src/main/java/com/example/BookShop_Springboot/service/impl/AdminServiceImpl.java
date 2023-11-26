package com.example.BookShop_Springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookShop_Springboot.dto.AdminDto;
import com.example.BookShop_Springboot.model.Admin;
import com.example.BookShop_Springboot.repository.AdminRepository;
import com.example.BookShop_Springboot.repository.RoleRepository;
import com.example.BookShop_Springboot.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository RoleRepository;

    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public Admin save(AdminDto adminDto) {
        Admin admin = new Admin();
        admin.setUsername(adminDto.getUsername());
        admin.setPhone(adminDto.getPhone());
        admin.setAddress(adminDto.getAddress());
        admin.setEmail(adminDto.getEmail());
        admin.setPassword(adminDto.getPassword());
        admin.setRole(RoleRepository.findByName("ADMIN"));
        admin.setStatus(true);
        return adminRepository.save(admin);
    }

    @Override
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
    
}
