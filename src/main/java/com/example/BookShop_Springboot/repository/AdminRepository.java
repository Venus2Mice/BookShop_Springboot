package com.example.BookShop_Springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.BookShop_Springboot.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	
	@Query (value = "SELECT * FROM users u WHERE u.username = ?1", nativeQuery = true)
    Admin findByUsername(String username);
    
	@Query(value = "SELECT u FROM Admin u WHERE u.username LIKE CONCAT('%', :username, '%')")
    List<Admin> findLikeUsername(String username);
    
    @Query(value = "SELECT * FROM users u INNER JOIN roles r on u.fk_user_role = r.role_id WHERE r.name = %1", nativeQuery = true)
    List<Admin> findByRole(String name);

    Admin findByEmail(String email);
}
