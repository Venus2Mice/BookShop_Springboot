package com.example.BookShop_Springboot.service;

import java.util.List;

import com.example.BookShop_Springboot.dto.RoleDto;
import com.example.BookShop_Springboot.model.Role;

public interface RoleService {

	List<RoleDto> findAll();
	List<RoleDto> findLikeName(String name);
	List<RoleDto> findByStatus(boolean status);
	 
	RoleDto findById(long id);
	Role findByIdModel(long id);
	RoleDto findByName(String name);
	
	RoleDto save(RoleDto dto);
	RoleDto update(RoleDto dto);
	
	RoleDto statusUpdate(boolean status, long id);
	
	List<RoleDto> transferData(List<Role> list);
	RoleDto transferData(Role model);
}
