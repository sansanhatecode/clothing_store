package com.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.dao.RoleDAO;
import com.store.model.Roles;
import com.store.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleDAO dao;

	public List<Roles> findAll() {
		return dao.findAll();
	}


	@Override
	public Roles findByRoleID(String name) {
		// TODO Auto-generated method stub
		return dao.findByRoleID(name);
	}






}
