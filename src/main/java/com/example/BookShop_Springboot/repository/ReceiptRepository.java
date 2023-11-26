package com.example.BookShop_Springboot.repository;

import com.example.BookShop_Springboot.model.Receipt;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @Query("select r from Receipt r inner join Supplier s ON s.id = r.supplier.id" +
            " where r.supplier.name = ?1 ")
    List<Receipt> findAllBySupplier(String supplier);

    @Query(value = "select r from Receipt r inner join Supplier s on s.id = ?1 and r.supplier.id = ?1 ")
    List<Receipt> getReceiptBySupplierId(Long id);

    @Query("select r from Receipt r where r.name like %?1% ")
    List<Receipt> searchReceipts(String keyword);

    @Query("select r from Receipt r where r.adminUpdate.id = ?1")
    List<Receipt> findAllByAdminId(Long id);

}
