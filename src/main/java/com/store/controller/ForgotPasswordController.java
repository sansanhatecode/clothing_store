package com.store.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.sym.Name;
import com.store.model.Users;
import com.store.service.UserService;
import com.store.util.SessionUtil;
import com.store.util.UserNotFoundException;

import net.bytebuddy.utility.RandomString;

@Controller
public class ForgotPasswordController {
	
	@Autowired
	UserService userService;
	@Autowired
	private JavaMailSender mailSender;
	
	@GetMapping("/forgot_password")
	public String ForgotPasswordFrom(Model model) {
		model.addAttribute("pageTile", "Forgot Password");
		return "layout/forgot_password";
	}
	
	
	@PostMapping("/forgot_password")
	public String processForgotPassword(HttpServletRequest request, Model model)  throws UserNotFoundException {
	    String email = request.getParameter("email");
	    String token = RandomString.make(30);
	     
	    try {
	        userService.updateResetPasswordToken(token, email);
	        String resetPasswordLink = SessionUtil.getSiteURL(request) + "/reset_password?token=" + token;
	        
	        sendEmail(email, resetPasswordLink);
	        model.addAttribute("message", "Chúng tôi đã gửi một liên kết đặt lại mật khẩu đến email của bạn. Vui lòng kiểm tra.");
	         
	    } catch (UserNotFoundException ex) {
	        model.addAttribute("error", ex.getMessage());
	    } catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
	    	 model.addAttribute("error", "Lỗi khi gửi mail");
		} 
	    
	         
	    return "layout/forgot_password";
	}


	private void sendEmail(String email, String resetPasswordLink)
			throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("contact@shop.com", "ShopQuanAo");
		helper.setTo(email);

		String subject = "Đây là link đặt lại mật khẩu của bạn";
	     
	    String content = "<p>Xin chào,</p>"
	            + "<p>Bạn đã yêu cầu đặt lại mật khẩu của mình</p>"
	            + "<p>Nhấp vào liên kiết dưới để thay đổi mật khẩu của bạn</p>"
	            + "<p><a href=\"" + resetPasswordLink + "\">Change my password</a></p>"
	            + "<br>"
	            + "<p>Bỏ qua mail này nếu bạn nhớ mật khẩu của mình "
	            + "Hoặc bạn chưa thực hiện yêu cầu</p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
		return;
	}
	@GetMapping ("/reset_password")
	public String showResetPasswordFrom(@Param(value  = "token") String token, Model model) {
		Users users = userService.getByResetPasswordToken(token);
			if (users == null) {
				model.addAttribute("title", "Đặt lại mật khẩu");
				model.addAttribute("message", "Mã không hợp lệ");
				return "message";
			}
			model.addAttribute("token", token);
			model.addAttribute("pageTile", "Reset Password");
		return "layout/reset_password_from";
	}
	
	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request, Model model,RedirectAttributes ra) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		Users users = userService.getByResetPasswordToken(token);
		if (users == null) {
			model.addAttribute("title", "Đặt lại mật khẩu");
			model.addAttribute("message", "Mã không hợp lệ");
			return "message";
		}else {
			userService.updatePassword(users, password);
			ra.addFlashAttribute("message", "Bạn đã thay đổi mật khẩu thành công.");
			return "redirect:/login";
		}
	}
}
