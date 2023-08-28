package com.store.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.store.model.Roles;
import com.store.service.RoleService;


@CrossOrigin("*")
@RestController
@RequestMapping("/admin/roles")
public class AdminRoleRESTController {
	@Autowired
	RoleService roleService;
	
	@GetMapping
	public List<Roles> getAll(){
		return roleService.findAll();
	}
	
}
