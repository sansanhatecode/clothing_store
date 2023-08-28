package com.store.controller;

import com.store.DTO.ProductDTO;
import com.store.configs.CustomConfiguration;
import com.store.constant.SessionConstant;
import com.store.dao.ProductDAO;
import com.store.model.Authorities;
import com.store.model.Product_Colors;
import com.store.model.Products;
import com.store.model.Users;

import com.store.service.ProductColorsService;
import com.store.service.ProductImgService;
import com.store.service.ProductService;
import com.store.service.UserService;
import com.store.DTO.sellingProductsDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {
	@Autowired
	private ProductService productService;
	@Autowired
	private UserService userService;
	@Autowired
	private ProductColorsService productColorsService;
	@Autowired
	CustomConfiguration customConfiguration;
	@Autowired
	ProductImgService productImgService;
	private static final int MAX_SIZE = 4;
	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ProductDAO productDAO;

	private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();


	@RequestMapping({"/", "/home"})
	public String home(@RequestParam(value = "pageM", required = false, defaultValue = "1") int pageM,
					   @RequestParam(value = "pageW", required = false, defaultValue = "1") int pageW, Model model) {
		List<Products> productM = new ArrayList<>();
		List<sellingProductsDTO> prdM = new ArrayList<>();
		try {
			Page<Products> pageProductM = productService.findMen(MAX_SIZE, pageM);
			productM = pageProductM.getContent();
			productM.forEach(item -> {
				sellingProductsDTO prd = customConfiguration.modelMapper().map(item, sellingProductsDTO.class);
				prd.setColor(productColorsService.findbyProductID(item.getProductID()));
				List<Product_Colors> color = productColorsService.findbyProductID(item.getProductID());
				prd.setNameImg(productImgService.top3NameImg(color.get(0).getColorID()));
				prdM.add(prd);
			});
			model.addAttribute("totalPagesM", pageProductM.getTotalPages());
			model.addAttribute("currentPageM", pageM);
		} catch (Exception e) {
			productM = productService.findAll();
		}
		model.addAttribute("productM", prdM);
		List<sellingProductsDTO> prdW = new ArrayList<>();
		//list women
		List<Products> productW = new ArrayList<>();
		try {
			Page<Products> pageProduct = productService.findWomen(MAX_SIZE, pageW);
			productW = pageProduct.getContent();

			productW.forEach(item -> {
				sellingProductsDTO prd = customConfiguration.modelMapper().map(item, sellingProductsDTO.class);
				prd.setColor(productColorsService.findbyProductID(item.getProductID()));
				List<Product_Colors> color = productColorsService.findbyProductID(item.getProductID());
				prd.setNameImg(productImgService.top3NameImg(color.get(0).getColorID()));
				prdW.add(prd);
			});
			model.addAttribute("totalPagesW", pageProduct.getTotalPages());
			model.addAttribute("currentPageW", pageW);
		} catch (Exception e) {
			productW = productService.findAll();
		}

		//list sản phẩm bán chạy
		int pageSizeSellPrd = Math.round(productDAO.sellingproducts().size() / 4 * 10) / 10;
		List<List<sellingProductsDTO>> list = new ArrayList<>();
		for (int i = 1; i <= pageSizeSellPrd; i++) {
			List<String> ls = productDAO.selling(i);
			List<sellingProductsDTO> lsSell = new ArrayList<>();
			ls.forEach(item -> {
				Products prd = productService.findByProductID(item.split(",")[0]);
				List<Product_Colors> color = productColorsService.findbyProductID(item.split(",")[0]);
				sellingProductsDTO prdSell = customConfiguration.modelMapper().map(prd, sellingProductsDTO.class);
				List<String> nameImg = productImgService.top3NameImg(color.get(0).getColorID());
				prdSell.setNameImg(nameImg);
				prdSell.setColor(color);
				prdSell.setQuantitysold(item.split(",")[1]);
				lsSell.add(prdSell);
				if (lsSell.size() == 4) {
					list.add(lsSell);
				}
			});
		}
		//list sp mới
		List<List<sellingProductsDTO>> listProductsNew = new ArrayList<>();
		List<sellingProductsDTO> listsell = new ArrayList<>();
		List<Products> prdnew = productService.productsNew();
		for (int i = 0; i < prdnew.toArray().length; i++) {
			List<Product_Colors> color = productColorsService.findbyProductID(prdnew.get(i).getProductID());
			sellingProductsDTO prdSell = customConfiguration.modelMapper().map(prdnew.get(i), sellingProductsDTO.class);
			List<String> nameImg = productImgService.top3NameImg(color.get(0).getColorID());
			prdSell.setColor(color);
			prdSell.setNameImg(nameImg);
			listsell.add(prdSell);
			if (listsell.size() == 4) {
				listProductsNew.add(listsell);
				listsell = new ArrayList<>();
			}
		}


		model.addAttribute("sell", list);
		model.addAttribute("productNew", listProductsNew);
		model.addAttribute("productW", prdW);
		return "/layout/home";
	}

	@RequestMapping("/login")
	public String doGetLogin(Model model) {
		model.addAttribute("userRequest", new Users());
		return "layout/login";
	}

	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String login(@RequestParam("userID") String username, @RequestParam("password") String password,
						@ModelAttribute("userRequest") Users userRequest, HttpSession session, RedirectAttributes ra)
			throws Exception {
		Users users = userService.findById(username);
		if (null == users) {
			ra.addFlashAttribute("message", "Tài khoản không đúng !");
			return "redirect:/login";
		} else {
			if (!passwordEncoder.matches(password, users.getPassword())) {
				ra.addFlashAttribute("message", "Mật khẩu không đúng !");
				return "redirect:/login";
			}
		}
		// Tạo đối tượng Authentication từ thông tin người dùng
		Authentication authentication = authenticationManagerBuilder.getObject()
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		// Xác thực thành công, lưu thông tin người dùng vào session
		if (authentication.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			Users userResponse = userService.doLogin(username, password);
			session.setAttribute(SessionConstant.CURRENT_USER, userResponse);

			// Kiểm tra vai trò của người dùng và chuyển hướng đến trang tương ứng
			List<Authorities> authoritiesList = userResponse.getAuthorities();
			for (Authorities authorities : authoritiesList) {
				String userRole = authorities.getRole().getRoleID();
				if (userRole.equalsIgnoreCase("admin") || userRole.equalsIgnoreCase("staff")) {
					return "redirect:/admin/user";
				}
			}

			return "redirect:/home";
		} else {
			throw new Exception("Invalid username or password");

		}

	}

	@RequestMapping("/logout")
	public String doGetLogout(HttpSession session) {
		session.removeAttribute(SessionConstant.CURRENT_USER);
		return "redirect:/home";
	}

	@GetMapping("/register")
	public String doGetRegister(Model model) {
		model.addAttribute("userRequest", new Users());
		return "layout/register";
	}

	@PostMapping("/register")
	public String goPostRegister(@ModelAttribute("userRequest") Users userRequest, HttpSession session,
								 RedirectAttributes ra) {
		Users userEmail = userService.findByEmailCheck(userRequest.getEmail());
		if (userEmail != null) {
			ra.addFlashAttribute("message", "Email đã tồn tại !");
			return "redirect:/register";
		}
		;
		Users userPhone = userService.findByPhone(userRequest.getPhone());
		if (userPhone != null) {
			ra.addFlashAttribute("message", "Số điện thoại đã tồn tại !");
			return "redirect:/register";
		}
		;
		Users userResponse = userService.save(userRequest, userRequest.getUserID());

		if (userResponse != null) {
			session.setAttribute("currentUser", userResponse);
			return "redirect:/home";
		} else {
			ra.addFlashAttribute("message", "Username đã tồn tại !");
			return "redirect:/register";
		}
	}
}
