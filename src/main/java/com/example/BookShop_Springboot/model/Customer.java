package com.example.BookShop_Springboot.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private boolean status;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String img_avt;

    @Column(name = "passwordHash")
    private String password;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShoppingCart cart;

    public Customer() {
        this.cart = new ShoppingCart();
        this.orders = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", cart=" + cart.getId() +
                ", orders=" + orders.size() +
                '}';
    }

    // @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    // private ShoppingCart cart;

    // @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    // private List<Order> orders;

    // public Customer() {
    // this.country = "VN";
    // this.cart = new ShoppingCart();
    // this.orders = new ArrayList<>();
    // }

    // @Override
    // public String toString() {
    // return "Customer{" +
    // "id=" + id +
    // ", username='" + username + '\'' +
    // ", password='" + password + '\'' +
    // ", phone='" + phone + '\'' +
    // ", address='" + address + '\'' +
    // ", city=" + city.getName() +
    // ", country='" + country + '\'' +
    // ", roles=" + role +
    // ", cart=" + cart.getId() +
    // ", orders=" + orders.size() +
    // '}';
    // }

}
