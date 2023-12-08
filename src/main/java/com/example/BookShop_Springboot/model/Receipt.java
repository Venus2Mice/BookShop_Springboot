package com.example.BookShop_Springboot.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @Column(name = "receipt_id", length = 16, unique = true)
    private String id;
    private String description;
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
    @JoinColumn(name = "fk_supplier_receipt", referencedColumnName = "supplier_id")
    private Supplier supplier;

    @PrePersist
    public void generateUniqueOrderId() {
        // Tạo UUID
        UUID uuid = UUID.randomUUID();
        // Chuyển UUID thành chuỗi và loại bỏ dấu "-"
        String receiptIdString = uuid.toString().replace("-", "");
        // Chuyển chuỗi thành chữ hoa (nếu là chữ cái)
        this.id = receiptIdString.toUpperCase();
        // Cắt lấy 16 ký tự đầu
        if (this.id.length() > 16) {
            this.id = this.id.substring(0, 16);
        }
    }
}
