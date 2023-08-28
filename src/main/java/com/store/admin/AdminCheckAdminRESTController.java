package com.store.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.model.Users;
import com.store.service.UserService;



@CrossOrigin("*")
@RestController
@RequestMapping("/admin/checkadmin")
public class AdminCheckAdminRESTController {
	@Autowired
	UserService userService;
	@GetMapping
	public List<Users> getAccounts() {
			return userService.getAdministrators();
	}
}
