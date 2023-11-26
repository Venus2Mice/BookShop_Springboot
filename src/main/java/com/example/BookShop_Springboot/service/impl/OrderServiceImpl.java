package com.example.BookShop_Springboot.service.impl;

import com.example.BookShop_Springboot.model.Order;
import com.example.BookShop_Springboot.model.CartItem;
import com.example.BookShop_Springboot.model.Customer;
import com.example.BookShop_Springboot.model.OrderDetail;
import com.example.BookShop_Springboot.model.Product;
import com.example.BookShop_Springboot.model.ShoppingCart;
import com.example.BookShop_Springboot.repository.CustomerRepository;
import com.example.BookShop_Springboot.repository.OrderDetailRepository;
import com.example.BookShop_Springboot.repository.OrderRepository;
import com.example.BookShop_Springboot.service.OrderService;
import com.example.BookShop_Springboot.service.ShoppingCartService;
import com.example.BookShop_Springboot.utils.DateFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository detailRepository;
    private final CustomerRepository customerRepository;
    private final ShoppingCartService cartService;

    @Override
    @Transactional
    public Order save(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(DateFormatter.getCurrentDateFormatted());
        order.setCustomer(shoppingCart.getCustomer());
        order.setTax(2);
        order.setTotalPrice(shoppingCart.getTotalPrice());
        order.setAccept(false);
        order.setPaymentMethod("Cash");
        order.setOrderStatus("Pending");
        order.setQuantity(shoppingCart.getTotalItems());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartItem item : shoppingCart.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());
            detailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }
        order.setOrderDetailList(orderDetailList);
        cartService.deleteCartById(shoppingCart.getId());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll(String username) {
        Customer customer = customerRepository.findByUsername(username);
        List<Order> orders = customer.getOrders();
        return orders;
    }

    @Override
    public List<Order> findALlOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order acceptOrder(Long id) {
        // Order order = orderRepository.getById(id);
        Order order = orderRepository.findById(id).orElse(null);
        order.setAccept(true);
        order.setDeliveryDate(DateFormatter.getCurrentDateFormatted());
        return orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Product> getBestSellingProducts() {
        List<Order> allOrders = orderRepository.findAll(); // Lấy tất cả các Order

        // Tạo một HashMap để lưu tổng quantity và totalPrice cho mỗi Product
        Map<Product, Double> productRatios = new HashMap<>();

        for (Order order : allOrders) {
            List<OrderDetail> orderDetails = order.getOrderDetailList();
            int quantity = order.getQuantity();
            double totalPrice = order.getTotalPrice();
            for (OrderDetail orderDetail : orderDetails) {
                Product product = orderDetail.getProduct();
                if (productRatios.containsKey(product)) {
                    productRatios.put(product, productRatios.get(product) + totalPrice / quantity);
                } else {
                    productRatios.put(product, totalPrice / quantity);
                }
            }
        }

        // Sắp xếp Map theo tỷ lệ totalPrice/quantity
        List<Map.Entry<Product, Double>> sortedList = new ArrayList<>(productRatios.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());

        // Tạo danh sách sản phẩm từ Map đã sắp xếp
        List<Product> bestSellingProducts = new ArrayList<>();
        for (Map.Entry<Product, Double> entry : sortedList) {
            bestSellingProducts.add(entry.getKey());
        }

        return bestSellingProducts;
    }

}
