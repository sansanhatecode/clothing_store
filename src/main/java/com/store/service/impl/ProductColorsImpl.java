package com.store.service.impl;

import com.store.dao.ProductColorDAO;
import com.store.model.Product_Colors;
import com.store.model.Products;
import com.store.service.ProductColorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
@Transactional
public class ProductColorsImpl implements ProductColorsService {
    @Autowired
    public ProductColorDAO productColorDAO;
    @Override
    public List<Product_Colors> findAll() {
        return  productColorDAO.findAll();
    }

    @Override
    public List<Product_Colors> findByProductID1(String id) {
        return productColorDAO.findByProduct_ProductID(id);
    }

    @Override
    public Product_Colors save(Product_Colors productColors) throws SQLException {
        return productColorDAO.saveAndFlush(productColors);
    }
    @Override
    public Optional<Product_Colors> findByID(long colorID) {
        return productColorDAO.findById(colorID);
    }

    @Override
    public Optional<Product_Colors> findByproductIDAndColorHex(Products productID, String colorHex) {
        return productColorDAO.findByProductAndColorhex(productID, colorHex);
    }

    @Override
    public void deleteColor(long id) {
        productColorDAO.deleteByColorID(id);
    }


    @Override
    public Product_Colors findById(Long colorID) {
        Optional<Product_Colors> optional = productColorDAO.findById(colorID);
        return optional.isPresent() ? optional.get() : null;
    }
    @Override
    public void updateQuantity(Integer newAvailable, Long productID) {
        productColorDAO.updateQuantity(newAvailable, productID);
    }

    @Override
    public List<Product_Colors> findbyProductID(String ProductID) {
        return productColorDAO.findByProductProductID(ProductID);
    }
}
