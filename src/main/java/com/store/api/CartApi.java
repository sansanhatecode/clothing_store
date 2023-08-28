package com.store.api;

import com.store.constant.SessionConstant;
import com.store.DTO.CartDto;
import com.store.dao.OrderDetailDAO;
import com.store.model.Order_Details;
import com.store.model.Orders;
import com.store.model.Users;
import com.store.service.CartService;
import com.store.service.UserService;
import com.store.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartApi {
    // /api/cart/update?productId =.....&quantity=....&isReplace=.....

    @Autowired
    private CartService cartService;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    OrderDetailDAO orderDetailDAO;
    @Autowired
    UserService userService;

    @GetMapping("/update")
    public ResponseEntity<?> doGetUpdate(
            @RequestParam("productID") String productID,
            @RequestParam("colorID") Long colorID,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("categoryID") String categoryID,
            @RequestParam("isReplace") Boolean isRePlace, HttpSession session) {


        CartDto currentCart = SessionUtil.getCurrenCart(session);
        cartService.updateCart(currentCart, productID, colorID, quantity, categoryID, isRePlace);

        return ResponseEntity.ok(currentCart);
    }

    @GetMapping("/checkout")
    public ResponseEntity<?> doGetCheckout(@RequestParam("address") String address,
                                           @RequestParam("phone") String phone,
                                           @RequestParam("email") String email,
                                           @RequestParam("payment") String payment,
                                           @RequestParam("customer") String customer,
                                           HttpSession session, RedirectAttributes redirectAttributes) {

        String pattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (address.isEmpty() || email.isEmpty() || phone.isEmpty() || payment.isEmpty() || customer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //404
        }
        if (!Pattern.matches(pattern, email)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);// 401
        }
        Users currentUser = (Users) session.getAttribute(SessionConstant.CURRENT_USER);
        if (currentUser == null) {
            currentUser = userService.findById("default");
        }
        CartDto currentCart = SessionUtil.getCurrenCart(session);
        try {
            cartService.insert(currentCart, currentUser, address, customer, phone, email, payment, redirectAttributes);
            session.setAttribute(SessionConstant.CURRENT_CART, new CartDto());
            return new ResponseEntity<>(HttpStatus.OK);// 200
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);// 400
        }
    }

    @PostMapping("/Staff")
    public String senMailStaff(
            @RequestParam("mail") String email,
            @RequestParam("Orders") Orders order,
            RedirectAttributes ra) throws UnsupportedEncodingException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("contact@shop.com", "Thông báo");
        helper.setTo(email);
        List<Order_Details> listOrderDetails = orderDetailDAO.findByOrder_OrderID(order.getOrderID());
        List<String> ListTR = new ArrayList<>();
        listOrderDetails.forEach((item) -> {
            int i = 1;

            String b = "<tr>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\" >" + i + "<d>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\" >" + item.getOrder().getOrderID() + "<d>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\">" + item.getProduct().getName() + "<d>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\">" + item.getQuantity() + "<d>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\">" + item.getPrice() + "<d>\n" +
                    "\t\t\t<r>\n";
            ListTR.add(b);
            i++;
        });
        String tr = ListTR.stream().collect(Collectors.joining(String.valueOf("")));

        //abc@gmail.com
        String subject = "có 1 đơn hàng mới chờ bạn xác nhận";
        String content = "<table style=\"border:1px solid black; text-align: center;\">\n" +
                "\t\t<thead>\n" +
                "\t\t\t<th style=\" border:1px solid black;padding: 0 15px;\">stt<h>\n" +
                "\t\t\t<th style=\" border:1px solid black;padding: 0 15px;\">Mã đơn hàng<h>\n" +
                "\t\t\t<th style=\" border:1px solid black;padding: 0 15px;\">Tên<h>\n" +
                "\t\t\t<th style=\" border:1px solid black;padding: 0 15px;\">Số lượng<h>\n" +
                "\t\t\t<th style=\" border:1px solid black;padding: 0 15px;\">Giá<h>\n" +
                "\t\t<head>\n" +
                "\t\t<tbody>\n" +
                tr +
                "\t\t<body>\n" +
                "\t\t<tfoot>\n" +
                "\t\t\t<tr row=\"1\">\n" +
                "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\" colspan=\"3\">Thành tiền<d>\n" +
                "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\" colspan=\"2\">" + order.getPrice() + "<d>\n" +
                "\t\t\t<r>\n" +
                "\t\t<foot>\n" +
                "\t</table>" +
                "<div style=\"margin-top: 40px;margin-bottom: 25px;\">\n" +
                "\t\t<a href=\"http://localhost:8080/admin/status\" style=\"text-decoration: none;border: 1px solid #191970;background-color: #fff;margin-top: 20px;padding: 15px;\n" +
                "\t\tborder-radius: 9px;color: #191970;font-size: 16px; cursor: pointer;\">Xác nhận đơn hàng</a>\n" +
                "\t</div>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(mimeMessage);
        ra.addFlashAttribute("message", "Đã gửi phản hồi");
        return "redirect:/home";
    }

    @PostMapping("/sendMailUser")
    public String senMailUser(
            @RequestParam("mailstaff") String emailstaff,
            @RequestParam("mail") String email,
            @RequestParam("Orders") Orders order,
            RedirectAttributes ra) throws UnsupportedEncodingException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("contact@shop.com", "Thông báo");
        helper.setTo(email);
        List<Order_Details> listOrderDetails = orderDetailDAO.findByOrder_OrderID(order.getOrderID());
        List<String> ListTR = new ArrayList<>();
        listOrderDetails.forEach((item) -> {
            int i = 0;
            i = i + 1;
            String b = "<tr>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\" >" + i + "<d>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\" >" + item.getOrder().getOrderID() + "<d>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\">" + item.getProduct().getName() + "<d>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\">" + item.getQuantity() + "<d>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\">" + item.getPrice() + "<d>\n" +
                    "\t\t\t<r>\n";
            ListTR.add(b);
        });
        String tr = ListTR.stream().collect(Collectors.joining(String.valueOf("")));
        String subject = "Bạn vừa đặt thành công đơn hàng";
        String content = "<table style=\"border:1px solid black; text-align: center;\">\n" +
                "\t\t<thead>\n" +
                "\t\t\t<th style=\" border:1px solid black;padding: 0 15px;\">stt<h>\n" +
                "\t\t\t<th style=\" border:1px solid black;padding: 0 15px;\">Mã đơn hàng<h>\n" +
                "\t\t\t<th style=\" border:1px solid black;padding: 0 15px;\">Tên<h>\n" +
                "\t\t\t<th style=\" border:1px solid black;padding: 0 15px;\">Số lượng<h>\n" +
                "\t\t\t<th style=\" border:1px solid black;padding: 0 15px;\">Giá<h>\n" +
                "\t\t<head>\n" +
                "\t\t<tbody>\n" +
                tr +
                "\t\t<body>\n" +
                "\t\t<tfoot>\n" +
                "\t\t\t<tr row=\"1\">\n" +
                "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\" colspan=\"3\">Thành tiền<d>\n" +
                "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\" colspan=\"2\">" + order.getPrice() + "<d>\n" +
                "\t\t\t<r>\n" +
                "\t\t<foot>\n" +
                "\t</table>" +
                "<h4>Chân thành cảm ơn quý khách!</h4>\n" +
                "\t<h5>Nếu có bất kì thắc mắc hãy liên hệ email này:" + emailstaff + " </h5>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(mimeMessage);
        ra.addFlashAttribute("message", "Đã gửi phản hồi");
        return "redirect:/home";
    }
}



	

