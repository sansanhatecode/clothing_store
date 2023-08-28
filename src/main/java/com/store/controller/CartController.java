package com.store.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.store.DTO.CartDto;
import com.store.DTO.ProductColorDTO;
import com.store.DTO.ProductColorUserDTO;
import com.store.DTO.ProductDTO;
import com.store.constant.SessionConstant;
import com.store.dao.OrderDetailDAO;
import com.store.dao.StatusDAO;
import com.store.model.*;
import com.store.payment.PaypalPaymentIntent;
import com.store.payment.PaypalPaymentMethod;
import com.store.service.*;
import com.store.dao.descStatusDAO;
import com.store.util.PaymentUtils;
import com.store.util.SessionUtil;
import com.store.api.CartApi;
import org.slf4j.Logger;
import com.store.model.staff;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.store.dao.staffDAO;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    private ProductService productService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private StatusDAO statusDAO;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    staffDAO staffDAO;
    @Autowired
    descStatusDAO descStatusDAO;
    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";
    public static String done = null;
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaypalService paypalService;
    @Autowired
    private OrderDetailDAO orderDetailDAO;
    private static final int MAX_SIZE = 6;


	@GetMapping("")
	public String dogetIndex(Model model) {
		return "layout/cart";
	}

	@PostMapping("/order")
	public String doOrder(Model model) {
		return "redirect:/cart";
	}

	@GetMapping("/update")
	public String doGetUpdate(@RequestParam("productID") String productID, @RequestParam("colorID") Long colorID,
			@RequestParam("quantity") Integer quantity, @RequestParam("categoryID") String categoryID,
			@RequestParam("isReplace") Boolean isRePlace, HttpSession session) {

		CartDto currentCart = SessionUtil.getCurrenCart(session);
		cartService.updateCart(currentCart, productID, colorID, quantity, categoryID, isRePlace);

		return "layout/cart :: #row";
	}

    @RequestMapping("/check/{userId}")
    public String doGetCheck(@PathVariable("userId") String userId,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
        Users users = userService.findById(userId);
        if (users == null) {
            return "redirect:/home";
        } else {
            List<Orders> orders = new ArrayList<>();
            try {
                Page<Orders> pageOrder = ordersService.findByUserID(userId, MAX_SIZE, page);
                orders = pageOrder.getContent();
                model.addAttribute("totalPages", pageOrder.getTotalPages());
                model.addAttribute("currentPage", page);
            } catch (Exception e) {
                orders = ordersService.findByAll();
            }
            model.addAttribute("orders", orders);
        }
        return "layout/check_order";
    }

    @RequestMapping("/cancelOrder/{id}")
    @Async
    public String canceOrder(@PathVariable("id") long id, ProductColorUserDTO prd, RedirectAttributes ra, Model model) throws MessagingException, UnsupportedEncodingException {
        //lấy được status id --> kiểm tra trạng thái --> nếu chưa xác nhận thì thực hiện hủy đơn
        //nếu trạng thái khác thực hiện gửi mail báo cho nv biết và nhân viên thực hiện hủy đơn
        Status status = statusDAO.findByStatusID(id);
        model.addAttribute("prd", prd);
        if (prd.getReason().isEmpty()){
            prd.setReason("Không có");
        }
        String subject;
        List<Order_Details> listOrderDetails = orderDetailDAO.findByOrder_OrderID(status.getOrders().getOrderID());
        List<String> ListTR = new ArrayList<>();
        listOrderDetails.forEach((item) -> {
            int i = 1;
            String b = "<tr>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\" >" + i + "<d>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\" >" + item.getOrder().getOrderID() + "<d>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\">" + item.getProduct().getName() + "<d>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\">" + item.getQuantity() + "<d>\n" +
                    "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\">" + item.getPrice() + "<d>\n" +
                    "\t\t\t<tr>\n";
            ListTR.add(b);
            i++;
        });
        String tr = ListTR.stream().collect(Collectors.joining(String.valueOf("")));
        if (status.getDescription().getDescriptionID() == 1) {
            status.setDescription(descStatusDAO.findById(4).get());
            if (prd.getReason().isEmpty()) {
                status.setReason("Không có");
            } else {
                status.setReason(prd.getReason());
            }
            statusDAO.save(status);
            subject = "Có 1 đơn hàng vừa bị người dùng hủy ";
        } else {
            status.setCancelOrder(true);
            statusDAO.save(status);
            subject = "Bạn nhận được 1 yêu cầu hủy đơn hàng từ phía người dùng ";
        }
        String emailStaff = status.getStaffID().getStaffID().getEmail();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("contact@shop.com", "Thông báo");
        helper.setTo(emailStaff);
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
                "\t\t\t\t<td style=\" border:1px solid black;padding: 0 15px;\" colspan=\"2\">" + status.getOrders().getPrice() + "<d>\n" +
                "\t\t\t<r>\n" +
                "\t\t<foot>\n" +
                "\t</table>" +
                " <h5>Lý do hủy đơn: "+prd.getReason()+"</h5>"+
                "<div style=\"margin-top: 40px;margin-bottom: 25px;\">\n" +
                "\t\t<a href=\"http://localhost:8080/admin/status\" style=\"text-decoration: none;border: 1px solid #191970;background-color: #fff;margin-top: 20px;padding: 15px;\n" +
                "\t\tborder-radius: 9px;color: #191970;font-size: 16px; cursor: pointer;\">Phê duyệt hủy đơn</a>\n" +
                "\t</div>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(mimeMessage);
        ra.addFlashAttribute("message", "Đã gửi phản hồi");
        String mess = "y";
        return doGetCheckOrderID(status.getOrders().getOrderID(),prd, model, mess);
    }

    @RequestMapping("/check/orderID/{orderID}")
    public String doGetCheckOrderID(@PathVariable("orderID") Long orderID, ProductColorUserDTO prd, Model model, String mess) {
        model.addAttribute("prd", prd);
        if (mess != null) {
            mess = null;
            model.addAttribute("mess", "mess");
        }
        Orders orders = ordersService.findById(orderID);
        if (orders == null) {
            return "redirect:/check/{userId}";
        } else {
            List<Order_Details> orderDetails = orderDetailsService.findByOrderID(orderID);
            List<String> catesID = new ArrayList<>();
            orderDetails.forEach(item -> {
                String cateid = productService.findById(item.getProduct().getProductID()).getCategory().getCategoryID();
                catesID.add(cateid);
            });
            model.addAttribute("catesID", catesID);
            model.addAttribute("orderDetails", orderDetails);
            model.addAttribute("orders", orders);
        }
        List<Status> status = statusService.findOrderID(orderID);
        model.addAttribute("status", status);
        return "layout/check_orderdetail";
    }

    @GetMapping("/checkout")
    public String doOrder(Model model, HttpSession session, RedirectAttributes ra) {
        if (done != null) {
            model.addAttribute("done", "done");
            done = null;
        }
        Users currentUser = (Users) session.getAttribute(SessionConstant.CURRENT_USER);
        if (ObjectUtils.isEmpty(currentUser)) {
            currentUser = userService.findById("default");
        }
        return "layout/cartcheckout";

    }

    @PostMapping("/pay")
    public String pay(@RequestParam("price") String price, HttpServletRequest request) {
        String cancelUrl = PaymentUtils.getBaseURL(request) + "/cart/" + URL_PAYPAL_CANCEL;
        String successUrl = PaymentUtils.getBaseURL(request) + "/cart/" + URL_PAYPAL_SUCCESS;
        double exchangeRate = 23000;
        try {
            Payment payment = paypalService.createPayment(Double.valueOf(price) / exchangeRate, "USD",
                    PaypalPaymentMethod.paypal, PaypalPaymentIntent.sale, "payment description", cancelUrl, successUrl);
            System.out.println(successUrl);
            for (Links links : payment.getLinks())
                if (links.getRel().equals("approval_url")) {
                    // chuyen den trang paypal
                    return "redirect:" + links.getHref();
                    // xu ly success and cancel
                }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay() {
        return "redirect:/cart/checkout";
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {

            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved"))
                done = "done";
            return "redirect:/cart/checkout";
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/cart";
    }
}
