package com.store.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.model.Authorities;
import com.store.model.Users;
import com.store.service.AuthoritiesService;
import com.store.service.UserService;



@CrossOrigin("*")
@RestController
@RequestMapping("/admin/authorities")
public class AdminAuthoritiesRESTController {
	@Autowired
	AuthoritiesService authoritiesService;
	@Autowired
	UserService userService;
	
	
	@GetMapping
	public List<Authorities> findAll() {	
		return authoritiesService.findAuthoritiesOfAdministrators();	
	}
	
	@GetMapping("/filterByRole/{roleId}")
    public List<Users> getUsersByRole(@PathVariable("roleId") String roleId) {
        return userService.findByAuthorities_Role_RoleID(roleId);
    }
	
	@PostMapping
	public Authorities post(@RequestBody Authorities auth) {
		return authoritiesService.create(auth);
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Integer id) {
		authoritiesService.delete(id);
	}
}
