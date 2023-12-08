package com.example.BookShop_Springboot.service;

import com.example.BookShop_Springboot.model.ShoppingCart;
import com.example.BookShop_Springboot.model.Order;
import com.example.BookShop_Springboot.model.Product;

import java.util.List;

public interface OrderService {
    Order save(ShoppingCart shoppingCart, String address, String phone, String paymentMethod);

    List<Order> findAll(String username);

    List<Order> findALlOrders();

    Order acceptOrder(String id);
    
    List<Product> getBestSellingProducts();

    void cancelOrderByCustomer(String id);

    void acceptOrderByCustomer(String id);

    Order getOrderById(String id);

    void savePaypal(ShoppingCart cart, String address, String phone);
}
