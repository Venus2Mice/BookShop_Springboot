package com.example.BookShop_Springboot.dto;

import java.util.Date;
import java.util.List;

import com.example.BookShop_Springboot.model.Admin;
import com.example.BookShop_Springboot.model.ReceiptDetail;
import com.example.BookShop_Springboot.model.Supplier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDto {
    private Long id;
    private String name;
    private Date createDate;
    private double totalPrice;
    private String currentPage;
    private boolean checkOut;
    private Admin adminCreate;
    private Admin adminUpdate;
    private Supplier supplier;
    private List<ReceiptDetail> receiptDetails;
}
