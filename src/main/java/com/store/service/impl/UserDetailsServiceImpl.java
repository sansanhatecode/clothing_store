package com.store.service.impl;

import com.store.model.Authorities;
import com.store.model.Users;
import com.store.service.AuthoritiesService;
import com.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.store.model.Authorities;
import com.store.model.Users;
import com.store.service.AuthoritiesService;
import com.store.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthoritiesService authoritiesService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userService.findById(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found: " + username);
		}

		List<GrantedAuthority> authorities = user.getAuthorities().stream()
				.map(auth -> new SimpleGrantedAuthority(auth.getRole().getRoleID())).collect(Collectors.toList());
		System.out.println(authorities);
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				authorities);
	}

	public Collection<? extends GrantedAuthority> getAuthorities(String userID) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		// Lấy danh sách quyền của người dùng từ authoritiesService
		List<Authorities> userAuthorities = authoritiesService.findByUserID(userID);
		for (Authorities authority : userAuthorities) {
			authorities.add(new SimpleGrantedAuthority(authority.getRole().getRoleID()));
		}
		return authorities;
	}

}