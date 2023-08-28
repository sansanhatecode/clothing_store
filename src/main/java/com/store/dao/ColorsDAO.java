package com.store.dao;

import com.store.model.Colors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorsDAO extends JpaRepository<Colors, Number> {
    Colors findByRGBColor(String color);
}
