package com.store.service;

import com.store.model.Product_Colors;
import com.store.model.Product_Images;
import com.store.model.Products;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductImgService {
    List<Product_Images> findAll();
    Product_Images save(Product_Images productImages) throws SQLException;
    List<Product_Images> findByProductcolorId(Product_Colors productColors);
    Optional<Product_Images> findById(long id);
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    public void deleteImg(long id);
    Page<Product_Images> findPaginated(Pageable pageable, List sql);
    List<String> top3NameImg(long colorID);
    int countImg(String id);
}
