package com.example.BookShop_Springboot.service;

import com.example.BookShop_Springboot.model.ShoppingCart;
import com.example.BookShop_Springboot.model.Order;
import com.example.BookShop_Springboot.model.Product;

import java.util.List;

public interface OrderService {
    Order save(ShoppingCart shoppingCart);

    List<Order> findAll(String username);

    List<Order> findALlOrders();

    Order acceptOrder(Long id);
    
    List<Product> getBestSellingProducts();

    void cancelOrderByCustomer(Long id);

    void denyOrder(Long id);

    void acceptOrderByCustomer(Long id);

    Order getOrderById(Long id);
}
