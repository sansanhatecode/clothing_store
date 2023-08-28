package com.store.admin;

import com.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin("*")
@Controller
public class AdminController {
	@Autowired
	UserService userService;

	@RequestMapping("/admin")
	public String admin(Model model) {
	    // Lấy thông tin xác thực của người dùng hiện tại
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/home"; // Chuyển hướng người dùng chưa đăng nhập về trang chủ
        }
	    // Kiểm tra xem người dùng có vai trò "admin" hay không
	    String isAdmin = authentication.getAuthorities().toString().toUpperCase();
	    System.out.println(isAdmin);
	    model.addAttribute("adminRole", isAdmin);
	    
	    return "/admin/admin";
	}
	@RequestMapping("/admin/authorizing")
	public String author() {
		return "/admin/authorities/authorities";
	}

	@RequestMapping("/admin/status")
	public String adminStatus() {
		return "/admin/status/status";
	}
}
