package com.example.BookShop_Springboot.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookShop_Springboot.dto.RoleDto;
import com.example.BookShop_Springboot.model.Product;
import com.example.BookShop_Springboot.model.Role;
import com.example.BookShop_Springboot.repository.ProductRepository;
import com.example.BookShop_Springboot.repository.RoleRepository;
import com.example.BookShop_Springboot.service.RoleService;
import com.example.BookShop_Springboot.utils.ImageUpload;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<RoleDto> findAll() {
		return transferData(roleRepository.findAll());
	}

	@Override
	public List<RoleDto> findLikeName(String name) {
		return transferData(roleRepository.findLikeName(name));
	}

	@Override
	public List<RoleDto> findByStatus(boolean status) {
		return transferData(roleRepository.findByStatus(status));
	}

	@Override
	public RoleDto findById(long id) {
		return transferData(roleRepository.findById(id));
	}
	
	@Override
	public Role findByIdModel(long id) {
		return roleRepository.findById(id);
	}

	@Override
	public RoleDto findByName(String name) {
		return transferData((roleRepository.findByName(name)));
	}

	@Override
	public RoleDto save(RoleDto dto) {
		Role model = new Role();
		model.setId(dto.getRoleId());
		model.setDescription(dto.getDescription());
		model.setName(dto.getName());
		model.setStatus(true);
		
		return transferData(roleRepository.save(model));
	}

	@Override
	public RoleDto update(RoleDto dto) {
		Role model = new Role();
		model.setId(dto.getRoleId());
		model.setDescription(dto.getDescription());
		model.setName(dto.getName());
		model.setStatus(true);
		
		return transferData(roleRepository.save(model));
	}

	@Override
	public RoleDto statusUpdate(boolean status, long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RoleDto> transferData(List<Role> list) {
		List<RoleDto> results = new ArrayList();
		for (Role item : list) {
			RoleDto role = new RoleDto();
			
			role.setRoleId(item.getId());
			role.setDescription(item.getDescription());
			role.setName(item.getName());
			role.setStatus(item.isStatus());
			
			results.add(role);
		}
		return results;
	}

	@Override
	public RoleDto transferData(Role model) {
		RoleDto role = new RoleDto();
		
		role.setRoleId(model.getId());
		role.setDescription(model.getDescription());
		role.setName(model.getName());
		role.setStatus(model.isStatus());
		
		return role;
	}

}
