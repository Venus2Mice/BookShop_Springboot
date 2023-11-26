package com.example.BookShop_Springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.BookShop_Springboot.dto.CategoryDto;
import com.example.BookShop_Springboot.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "update Category set name = ?1 where id = ?2")
    Category update(String name, Long id);

    @Query(value = "select * from categories where is_activated = true", nativeQuery = true)
    List<Category> findAllByActivatedTrue();

    @Query(value = "select new com.example.BookShop_Springboot.dto.CategoryDto(c.id, c.name, count(p.category.id)) " +
            "from Category c join Product p on c.id = p.category.id " +
            "where c.is_activated = true and c.is_deleted = false " +
            "group by c.id ")
    List<CategoryDto> getCategoriesAndSize();
}
