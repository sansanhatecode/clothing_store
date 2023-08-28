package com.store.dao;

import com.store.model.StatusName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusNameDAO extends JpaRepository<StatusName, Integer> {
}
