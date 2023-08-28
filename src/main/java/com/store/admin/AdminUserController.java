package com.store.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.store.model.Authorities;
import com.store.model.Roles;
import com.store.model.Users;
import com.store.service.AuthoritiesService;
import com.store.service.RoleService;
import com.store.service.UserService;

@CrossOrigin("*")
@Controller
public class AdminUserController {
	@Autowired
	UserService userService;
	@Autowired
	AuthoritiesService authoritiesService;
	@Autowired
	RoleService roleService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@GetMapping("/admin/user")
	public String adminUser(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		List<Users> users = userService.findAll();

		int totalItems = users.size();
		int totalPages = (int) Math.ceil(totalItems / (double) size);
		int startItem = page * size + 1;
		int endItem = Math.min((page + 1) * size, totalItems);

		List<Users> userList = users.subList(page * size, endItem);

		model.addAttribute("users", userList);
		model.addAttribute("user", new Users());
		model.addAttribute("currentPage", page + 1);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startItem", startItem);
		model.addAttribute("endItem", endItem);

		return "/admin/user/user";
	}

	// Hàm hiển thị trang danh sách người dùng với phân trang
	private String showUserListPage(Model model, int page, int size) {
		List<Users> users = userService.findAll();
		int totalItems = users.size();
		int totalPages = (int) Math.ceil(totalItems / (double) size);
		int startItem = page * size + 1;
		int endItem = Math.min((page + 1) * size, totalItems);

		List<Users> userList = users.subList(page * size, endItem);
		model.addAttribute("users", userList);
		model.addAttribute("currentPage", page + 1);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startItem", startItem);
		model.addAttribute("endItem", endItem);

		return "/admin/user/user";
	}

	@GetMapping("/admin/user/new")
	public String createUserForm(Model model) {

		model.addAttribute("user", new Users());
		return "/admin/user/user";
	}

	@PostMapping("/admin/user/new")
	public String createUser(Model model, @Validated @ModelAttribute("user") Users user, Errors errors,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam("action") String action) {
		model.addAttribute("message", "Vui lòng sửa các lỗi sau: ");
		// Kiểm tra hành động là "create" hay "edit"
		if (action.equals("create")) {
			// Kiểm tra nếu userID đã tồn tại thì báo lỗi
			Users existingUser = userService.findById(user.getUserID());
			if (existingUser != null) {
				errors.rejectValue("userID", "userid.exists", "UserID đã tồn tại trong hệ thống.");
			}

			// Kiểm tra nếu email đã tồn tại thì báo lỗi
			if (userService.isEmailExists(user.getEmail())) {
				errors.rejectValue("email", "email.exists", "Email đã tồn tại trong hệ thống.");
			}

			// Nếu có lỗi, hiển thị trang danh sách người dùng với thông báo lỗi
			if (errors.hasErrors()) {
				return showUserListPage(model, page, size);
			}

			// Tiến hành tạo mới người dùng
			Roles customerRole = roleService.findByRoleID("customer");
			Authorities authorities = new Authorities();
			authorities.setUser(user);
			authorities.setRole(customerRole);

			String rawPassword = user.getPassword();
			String encodedPassword = passwordEncoder.encode(rawPassword);
			user.setPassword(encodedPassword);

			userService.create(user);
			authoritiesService.create(authorities);
			
		}
		
		// Cập nhật thông tin người dùng nếu action là "edit"
		else {
			Users existingUser = userService.findById(user.getUserID());
			if (existingUser == null) {
				model.addAttribute("message", "Người dùng không tồn tại trong hệ thống.");
				return "admin/user/user";
			}

			// Kiểm tra nếu email đã tồn tại và không phải của userID hiện tại thì báo lỗi
			if (userService.isEmailExists(user.getEmail()) && !existingUser.getEmail().equals(user.getEmail())) {
				model.addAttribute("message", "Vui lòng sửa các lỗi sau:");
				errors.rejectValue("email", "email.exists", "Email đã tồn tại trong hệ thống.");
				return "admin/user/user";
			}

			if (errors.hasErrors()) {
				model.addAttribute("action", "edit");
				return "admin/user/user";
			}

			// Cập nhật thông tin người dùng vào cơ sở dữ liệu
			String rawPassword = user.getPassword();
			String encodedPassword = passwordEncoder.encode(rawPassword);
			user.setPassword(encodedPassword);
			userService.update(user);
		}

		return "redirect:/admin/user"; // Chuyển hướng đến trang đã cập nhật hoặc tạo mới user
	}

	@RequestMapping("/admin/edit/{id}")
	public String edit(@PathVariable("id") String id, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, Model model) {
	    // Lấy thông tin người dùng cần chỉnh sửa
	    Users existingUser = userService.findById(id);
	    // Kiểm tra nếu userID không tồn tại trong hệ thống
	    if (existingUser == null) {
	        model.addAttribute("message", "Người dùng không tồn tại trong hệ thống.");
	        return "admin/user/user";
	    }
	    
	    // Hiển thị thông tin người dùng cần chỉnh sửa
	    model.addAttribute("user", existingUser);
	    model.addAttribute("action", "edit"); // Xác định hành động là chỉnh sửa
	    showUserListPage(model, page, size);
	    return "admin/user/user";
	}

	@GetMapping("/admin/delete/{id}")
	public String delete(@PathVariable("id") String id) {
		userService.delete(id);
		return "redirect:/admin/user";
	}

	@GetMapping("/admin/search")
	public String searchUsers(@RequestParam("keyword") String keyword, Model model,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
		List<Users> users = userService.searchUsers(keyword);
		int totalItems = users.size();
		int totalPages = (int) Math.ceil(totalItems / (double) size);
		int startItem = page * size + 1;
		int endItem = Math.min((page + 1) * size, totalItems);
		List<Users> userList = users.subList(page * size, endItem);

		model.addAttribute("users", userList);
		model.addAttribute("user", new Users());
		model.addAttribute("currentPage", page + 1);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startItem", startItem);
		model.addAttribute("endItem", endItem);

		return "/admin/user/user";
	}

}
