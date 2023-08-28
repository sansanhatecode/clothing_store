package com.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import com.store.service.UserService;
import com.store.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.store.model.Users;
import com.store.service.UserService;
import com.store.service.impl.UserDetailsServiceImpl;

@EnableWebSecurity
@Configuration
@ComponentScan("com.store")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserService service;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {	
		 auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/admin").hasAnyAuthority("admin","staff")
			.antMatchers("/admin/authorizing").hasAuthority("admin")
			.antMatchers("/admin/authorities").hasAuthority("admin")
			.anyRequest().permitAll()	
			.and()
			 .formLogin()
	            .loginPage("/login") // Trang đăng nhập
	            .loginProcessingUrl("/perform-login") // URL xử lý đăng nhập do bạn định nghĩa
	            .defaultSuccessUrl("/admin") // Trang sau khi đăng nhập thành công
	            .permitAll()
	        .and()
			.exceptionHandling()
			.accessDeniedHandler((request, response, accessDeniedException) -> {
	            if (request.isUserInRole("customer")) {
	                response.sendRedirect("/home"); // Chuyển hướng người dùng về trang chủ (home)
	            } else {
	                response.sendRedirect("/home"); // Chuyển hướng đến trang truy cập bị từ chối (access denied) khác nếu cần
	            }
	        });
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
