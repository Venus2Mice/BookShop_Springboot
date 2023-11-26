package com.example.BookShop_Springboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "receipt_detail")
public class ReceiptDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_detail_id")
    private Long id;

    @ManyToOne
    private Receipt receipt;

    @ManyToOne
    private Product product;

    private int quantity;

    private double totalPrice;
}
