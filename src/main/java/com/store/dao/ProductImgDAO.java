package com.store.dao;

import com.store.model.Orders;
import com.store.model.Product_Colors;
import com.store.model.Product_Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductImgDAO extends JpaRepository<Product_Images, Long > {
    List<Product_Images> findAll() ;
    Optional<Product_Images> findByImgID(long id);
//    Optional<Product_Images> findByProductcolor(Product_Colors productColors);
    List<Product_Images> findByProductcolor(Product_Colors productColors);
    @Query(value = "select top(3) image from product_image where colorID = :colorID ", nativeQuery = true)
    List<String> findimg(@Param("colorID") long colorID);
    @Query(value = "select COUNT(*) from product_image where image = :id",nativeQuery = true)
    int countImg(String id);
    void deleteByImgID(long id);
}
