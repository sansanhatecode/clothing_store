package com.store.dao;

import com.store.model.Product_Colors;
import com.store.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductColorDAO extends JpaRepository<Product_Colors, Long> {
    Optional<Product_Colors> findByProductAndColorhex(Products id, String colorHex);
    void deleteByColorID(long id);

    Product_Colors findByColorID(Long colorID);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE product_color SET available = ? WHERE colorID = ?",nativeQuery = true)
    void updateQuantity(Integer newAvailable,Long colorID);
    @Query(value = "SELECT * from product_color where productID = :productID and available >= 0  ",nativeQuery = true)
    List<Product_Colors> findByProduct_ProductID(String productID);

    @Query(value = "SELECT * from product_color where productID = :productID and available > 0  ",nativeQuery = true)
    List<Product_Colors> findByProductProductID(String productID);
}
