package com.store.service;

import java.util.List;

import com.store.model.Roles;



public interface RoleService {
	public List<Roles> findAll();
	
	public Roles findByRoleID(String name);


}
