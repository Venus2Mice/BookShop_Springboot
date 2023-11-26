package com.example.BookShop_Springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.BookShop_Springboot.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.deleted = false and p.activated = true")
    List<Product> getAllProduct();

    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    List<Product> findAllByNameOrDescription(String keyword);

    @Query("select p from Product p inner join Category c ON c.id = p.category.id" +
            " where p.category.name = ?1 and p.activated = true and p.deleted = false")
    List<Product> findAllByCategory(String category);

    @Query("select p from Product p where p.authors = ?1 and p.activated = true and p.deleted = false order by ?1 asc")
    List<Product> findByOrderByAuthorsAsc(String authors);

    @Query("select p from Product p where p.publisher = ?1 and p.activated = true and p.deleted = false order by ?1 asc")
    List<Product> findByOrderByPublishersAsc(String publisher);

    @Query(value = "select " +
            "p.product_id, p.name, p.description, p.current_quantity, p.sale_price, p.image, p.activated, p.deleted, p.authors, p.publisher, p.discount "
            +
            "from products p where p.activated = true and p.deleted = false order by rand() limit 9", nativeQuery = true)
    List<Product> randomProduct();

    @Query(value = "select " +
            "p.product_id, p.name, p.description, p.current_quantity, p.sale_price, p.image, p.activated, p.deleted, p.authors, p.publisher, p.discount "
            +
            "from products p where p.deleted = false and p.activated = true order by p.cost_price desc limit 9", nativeQuery = true)
    List<Product> filterHighProducts();

    @Query(value = "select " +
            "p.product_id, p.name, p.description, p.current_quantity, p.sale_price, p.image, p.activated, p.deleted, p.authors, p.publisher, p.discount "
            +
            "from products p where p.deleted = false and p.activated = true order by p.cost_price asc limit 9", nativeQuery = true)
    List<Product> filterLowerProducts();

    @Query(value = "select p.product_id, p.name, p.description, p.current_quantity, p.sale_price, p.image, p.activated, p.deleted, p.authors, p.publisher, p.discount from products p where p.deleted = false and p.activated = true limit 4", nativeQuery = true)
    List<Product> listViewProduct();

    @Query(value = "select p from Product p inner join Category c on c.id = ?1 and p.category.id = ?1 where p.activated = true and p.deleted = false")
    List<Product> getProductByCategoryId(Long id);

    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    List<Product> searchProducts(String keyword);

    Product findByName(String name);
}
