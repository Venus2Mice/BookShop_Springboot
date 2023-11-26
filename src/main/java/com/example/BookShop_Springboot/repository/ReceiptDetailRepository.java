package com.example.BookShop_Springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BookShop_Springboot.model.ReceiptDetail;

@Repository
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long> {

    List<ReceiptDetail> findByReceiptId(Long id);

}
