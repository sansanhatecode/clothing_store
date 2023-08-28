package com.store.service;

import com.store.model.Categories;

import java.util.List;

public interface CatelogyService {
    List<Categories> findAll();
    Categories findByCategoryID(String categoryID);
}
