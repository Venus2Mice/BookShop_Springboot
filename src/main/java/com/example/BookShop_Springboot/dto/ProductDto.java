package com.example.BookShop_Springboot.dto;

import com.example.BookShop_Springboot.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private String authors;
    private String publisher;
    private int currentQuantity;
    private double salePrice;
    private String image;
    private Category category;
    private boolean activated;
    private boolean deleted;
    private String currentPage;
    private double discountPrice;
    private double discount;
}
