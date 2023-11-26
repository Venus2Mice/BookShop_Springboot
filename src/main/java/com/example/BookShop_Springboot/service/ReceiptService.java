package com.example.BookShop_Springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.BookShop_Springboot.dto.ReceiptDto;
import com.example.BookShop_Springboot.model.Admin;
import com.example.BookShop_Springboot.model.Receipt;
import com.example.BookShop_Springboot.model.ReceiptDetail;

public interface ReceiptService {
    List<ReceiptDto> allReceipt();

    Receipt save(ReceiptDto receiptDto);

    Receipt save(Receipt receipt);

    Receipt initDefault(Admin adminCreate, Admin adminUpdate);

    Receipt update(ReceiptDto receiptDto);

    Receipt update(Receipt receiptNew, Receipt receiptOld);

    void enableById(Long id);

    void deleteById(Long id);

    ReceiptDto getById(Long id);

    Receipt findById(Long id);

    Page<ReceiptDto> searchReceipts(int pageNo, String keyword);

    Page<ReceiptDto> getAllReceipts(int pageNo);

    List<ReceiptDto> findAllBySupplier(String supplier);

    List<ReceiptDto> findBySupplierId(Long id);

    List<ReceiptDto> searchReceipts(String keyword);

    List<ReceiptDetail> findByReceiptId(Long id);

    void removeProductFromReceiptDetail(Long id);

    void saveReceiptDetail(Admin findByEmail, List<ReceiptDetail> receiptDetails, Long id);

}
