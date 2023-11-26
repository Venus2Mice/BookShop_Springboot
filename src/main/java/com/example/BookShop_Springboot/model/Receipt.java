package com.example.BookShop_Springboot.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "receipts")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "receipt_id")
    private Long id;
    private String name;
    private Date createDate;
    private double totalPrice;
    private boolean checkOut;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receipt")
    private List<ReceiptDetail> receiptDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_adminCreate_receipt", referencedColumnName = "user_ID")
    private Admin adminCreate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_adminUpdate_receipt", referencedColumnName = "user_ID")
    private Admin adminUpdate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_supplier_receipt" ,referencedColumnName = "supplier_id")
    private Supplier supplier;

    public Receipt(String name) {
        this.name = name;
        this.createDate = new Date();
        this.checkOut = false;
    }
}
