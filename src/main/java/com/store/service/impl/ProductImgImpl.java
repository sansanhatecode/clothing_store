package com.store.service.impl;

import com.store.dao.ProductImgDAO;
import com.store.model.Product_Colors;
import com.store.model.Product_Images;
import com.store.model.Products;
import com.store.service.ProductImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
@Transactional
public class ProductImgImpl implements ProductImgService {
    @Autowired
    ProductImgDAO productImgDAO;
    @Override
    public List<Product_Images> findAll() {
       return productImgDAO.findAll();
    }

    @Override
    public Product_Images save(Product_Images productImages) throws SQLException {
         return productImgDAO.saveAndFlush(productImages);
    }

//    @Override
//    public Optional<Product_Images> findByProductcolor(Product_Colors productColors) {
//        return productImgDAO.findByProductcolor(productColors);
//    }

    @Override
    public List<Product_Images> findByProductcolorId(Product_Colors productColors) {
        return productImgDAO.findByProductcolor(productColors);
    }

    @Override
    public Optional<Product_Images> findById(long id) {
        return productImgDAO.findById(id);
    }

    @Override
    public void deleteImg(long id) {
        productImgDAO.deleteByImgID(id);
    }

    @Override
    public Page<Product_Images> findPaginated(org.springframework.data.domain.Pageable pageable, List sql) {
        List<Product_Images> products = sql;
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Product_Images> list;

        if (products.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, products.size());
            list = products.subList(startItem, toIndex);
        }
        Page<Product_Images> productImgPage = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), products.size());
        return productImgPage;
    }

    @Override
    public List<String> top3NameImg(long colorID) {
        return productImgDAO.findimg(colorID);
    }



    @Override
    public int countImg(String id) {
        return   productImgDAO.countImg(id);
    }
}
