package com.store.service.impl;

import com.store.dao.CategoryDAO;
import com.store.model.Categories;
import com.store.service.CatelogyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CatelogyService {
    @Autowired
    private CategoryDAO dao;

    @Override
    public List<Categories> findAll() {
        return dao.findAll();
    }

    @Override
    public Categories findByCategoryID(String categoryID) {
        return dao.findByCategoryID(categoryID);
    }


}
