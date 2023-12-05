package com.example.BookShop_Springboot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id", length = 16, unique = true)
    private String id;
    private Date orderDate;
    private Date deliveryDate;
    private String orderStatus;
    private double totalPrice;
    private double tax;
    private int quantity;
    private String paymentMethod;
    private boolean isAccept;
    private boolean isCancelByCustomer;
    private String address;
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderDetail> orderDetailList;

    @PrePersist
    public void generateUniqueOrderId() {
        // Tạo UUID
        UUID uuid = UUID.randomUUID();
        // Chuyển UUID thành chuỗi và loại bỏ dấu "-"
        String orderIdString = uuid.toString().replace("-", "");
        // Chuyển chuỗi thành chữ hoa (nếu là chữ cái)
        this.id = orderIdString.toUpperCase();
        // Cắt lấy 16 ký tự đầu
        if (this.id.length() > 16) {
            this.id = this.id.substring(0, 16);
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDate +
                ", totalPrice=" + totalPrice +
                ", tax='" + tax + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", customer=" + customer.getUsername() +
                ", orderDetailList=" + orderDetailList.size() +
                '}';
    }
}
