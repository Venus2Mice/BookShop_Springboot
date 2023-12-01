package com.example.BookShop_Springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.BookShop_Springboot.dto.AdminDto;
import com.example.BookShop_Springboot.dto.RoleDto;
import com.example.BookShop_Springboot.model.Admin;

public interface AdminService {
	Page<AdminDto> getAllAdmins(int pageNo);
	Page<AdminDto> searchByUserName(int pageNo, String keyword);
	Page<AdminDto> searhByRole(int pageNo, String keyword);
	
    AdminDto findByEmail(String email);
    AdminDto findByUserName(String username);
    
    Admin save(AdminDto adminDto);
    Admin update(AdminDto adminDto);
    Admin updateRole(RoleDto roleDto, AdminDto adminDto);
    
	List<AdminDto> transferData(List<Admin> list);
	AdminDto transferData(Admin model);
}
