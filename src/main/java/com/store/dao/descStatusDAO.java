package com.store.dao;

import com.store.model.descriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface descStatusDAO extends JpaRepository<descriptionStatus, Integer> {
}
