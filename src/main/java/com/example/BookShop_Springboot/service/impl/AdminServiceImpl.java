package com.example.BookShop_Springboot.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.BookShop_Springboot.dto.AdminDto;
import com.example.BookShop_Springboot.dto.ProductDto;
import com.example.BookShop_Springboot.dto.RoleDto;
import com.example.BookShop_Springboot.model.Admin;
import com.example.BookShop_Springboot.model.Role;
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
	public Page<AdminDto> getAllAdmins(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<AdminDto> dtoList= transferData(adminRepository.findAll());
        Page<AdminDto> dtoPage = toPage(dtoList, pageable);
        return dtoPage;
	}

	@Override
	public Page<AdminDto> searchByUserName(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<AdminDto> dtoList= transferData(adminRepository.findLikeUsername(keyword));
        Page<AdminDto> dtoPage = toPage(dtoList, pageable);
        return dtoPage;
	}

	@Override
	public Page<AdminDto> searhByRole(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<AdminDto> dtoList= transferData(adminRepository.findLikeUsername(keyword));
        Page<AdminDto> dtoPage = toPage(dtoList, pageable);
        return dtoPage;
	}

    @Override
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
	
	@Override
	public AdminDto findByUserName(String username) {
		return adminRepository.findByUsername(username) == null ? null : transferData(adminRepository.findByUsername(username));
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
	public Admin update(AdminDto adminDto) {
		Admin admin = adminRepository.findByUsername(adminDto.getUsername());
		admin.setAddress(adminDto.getAddress());
	    admin.setPhone(adminDto.getPhone());
	    admin.setRole(RoleRepository.findById(adminDto.getRole().getId()));
	    
	    return adminRepository.save(admin);
	}
	
	public Admin updateRole(RoleDto roleDto, AdminDto adminDto) {
		Admin admin = adminRepository.findByUsername(adminDto.getUsername());
		Role role = RoleRepository.findById(roleDto.getRoleId());
		admin.setRole(role);
		
		return admin;
	}
	
	@Override
    public void enableByUsername(String username) {
    	Admin admin = adminRepository.findByUsername(username);
    	System.out.println("test: " + admin.getUsername()+ ":" + username);
    	admin.setStatus(true);
    	adminRepository.save(admin);
    }
	
	@Override
    public void disableByUsername(String username) {
    	Admin admin = adminRepository.findByUsername(username);
    	System.out.println("test: " + admin.getUsername()+ ":" + username);
    	admin.setStatus(false);
    	adminRepository.save(admin);
    }

	private Page toPage(List list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }
	
	@Override
	public List<AdminDto> transferData(List<Admin> list) {
		
		List<AdminDto> results = new ArrayList<AdminDto>();
		for (Admin model : list) {
			AdminDto dto = new AdminDto();
			
			dto.setUsername(model.getUsername());
			dto.setEmail(model.getEmail());
			dto.setPassword(model.getPassword());
			dto.setPhone(model.getPhone());
			dto.setAddress(model.getAddress());			
			dto.setRole(model.getRole());
			dto.setStatus(model.isStatus());
			
			results.add(dto);
		}

		return results;
	}
	
	@Override
	public AdminDto transferData(Admin model) {
		AdminDto dto = new AdminDto();
		
		dto.setUsername(model.getUsername());
		dto.setEmail(model.getEmail());
		dto.setPassword(model.getPassword());
		dto.setPhone(model.getPhone());
		dto.setAddress(model.getAddress());			
		dto.setRole(model.getRole());
		dto.setStatus(model.isStatus());
		
		return dto;
	}
    
}
