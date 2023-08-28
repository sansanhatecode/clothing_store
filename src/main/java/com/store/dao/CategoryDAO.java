package com.store.dao;

import com.store.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryDAO extends JpaRepository<Categories, String> {
    List<Categories> findAll();
    Categories findByCategoryID(String CategoryID);
}
