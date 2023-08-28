package com.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

@Controller
public class ContactController {
    @Autowired
    JavaMailSender mailSender;

    @RequestMapping("/contact")
    public String doGetContact() {
        return "layout/contact";
    }

    @PostMapping("/contact")
    public String senMail(@RequestParam("name") String name,
                          @RequestParam("email") String email,
                          @RequestParam("message") String message,
                          RedirectAttributes ra) throws UnsupportedEncodingException, MessagingException {
        String pattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

        if (name.isEmpty() || email.isEmpty() || message.isEmpty() || !Pattern.matches(pattern, email)) {
            ra.addFlashAttribute("message", "Vui lòng nhập đầy đủ thông tin");
            return "redirect:/contact";
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setFrom("contact@shop.com", "Phản hồi khách hàng");
        helper.setTo("phatndps15970@fpt.edu.vn");
        //abc@gmail.com
        String subject = "Đây là phản hồi của khách hàng đén bạn";

        String content = "<p>Tên khách hàng : " + name + "</p>"
                + "<p>Địa chỉ mail : " + email + "  </p>"
                + "<p>Phản hồi của khách hàng : " + message + "</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(mimeMessage);
        ra.addFlashAttribute("message", "Đã gửi phản hồi");
        return "redirect:/contact";

    }
}
