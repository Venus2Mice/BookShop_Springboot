package com.example.BookShop_Springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.BookShop_Springboot.dto.RoleDto;
import com.example.BookShop_Springboot.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	@Query(value = "SELECT * FROM roles", nativeQuery = true)
	List<Role> findAll();
	
	@Query(value = "SELECT * FROMM roles WHERE name LIKE %?1%", nativeQuery = true)
	List<Role> findLikeName(String name);
	
	@Query(value = "SELECT * FROM roles WHERE status = ?1", nativeQuery = true)
	List<Role> findByStatus(boolean status);
	 
	@Query(value = "SELECT * FROM roles WHERE role_id = ?1", nativeQuery = true)
	Role findById(long id);
	
	@Query(value = "SELECT * FROM roles WHERE name = ?1", nativeQuery = true)
	Role findByName(String name);
	
	@Query(value = "UPDATE SET(status = ?1) WHERE role_id = ?2", nativeQuery = true)
	RoleDto statusUpdate(boolean status, long id);
}
