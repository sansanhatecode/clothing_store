package com.store.dao;

import java.util.List;

import com.store.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import com.store.model.Roles;
import org.springframework.data.jpa.repository.Query;

public interface RoleDAO extends JpaRepository<Roles, String> {
	Roles findByRoleID(String name);
	
}
