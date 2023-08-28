package com.store.service.impl;

import com.store.DTO.CartDetailDto;
import com.store.DTO.CartDto;
import com.store.api.CartApi;
import com.store.api.ProductApi;
import com.store.dao.StatusDAO;
import com.store.dao.descriptionStatusDAO;
import com.store.dao.StatusNameDAO;
import com.store.model.*;
import com.store.service.*;
import com.store.dao.staffDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;


import java.time.LocalTime;
import java.util.Date;

import java.util.List;

import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductColorsService productcolorsService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private descriptionStatusDAO descriptionStatusDAO;
    @Autowired
    private StatusNameDAO statusNameDAO;
    @Autowired
    private staffDAO staffDAO;
    @Autowired
    private CartApi cartApi;
    @Autowired
    private  UserService userService;

    @Override
    public CartDto updateCart(CartDto cart, String productID, Long colorID, Integer quantity, String categoryID, boolean isReplace) {
        Products product = productService.findById(productID);

        Map<String, CartDetailDto> details = cart.getDetails();

        String colorIDStr = String.valueOf(colorID);
        if (!details.containsKey(colorIDStr)) {
            // Thêm mới sản phẩm
            CartDetailDto newDetail = createNewCartDetail(cart, product, colorID, quantity, categoryID);
            details.put(colorIDStr, newDetail);
        } else if (quantity > 0) {
            // Cập nhật số lượng sản phẩm
            CartDetailDto existingDetail = details.get(colorIDStr);
            if (isReplace) {
                // Thay thế số lượng sản phẩm
                existingDetail.setQuantity(quantity);
            } else {
                // Cộng dồn số lượng sản phẩm
                existingDetail.setQuantity(existingDetail.getQuantity() + quantity);
            }
        } else {
            details.remove(colorIDStr);
        }

        // Cập nhật tổng số lượng và tổng giá trị của giỏ hàng
        cart.setTotalQuantity(getTotalQuantity(cart));
        cart.setTotalPrice(getTotalPrice(cart));

        return cart;
    }

    @Override
    public Integer getTotalQuantity(CartDto cart) {
        Integer totalQuantity = 0;
        Map<String, CartDetailDto> details = cart.getDetails();
        for (CartDetailDto cartDetail : details.values()) {
            totalQuantity += cartDetail.getQuantity();
        }
        return totalQuantity;
    }

    @Override
    public Double getTotalPrice(CartDto cart) {
        Double totalPrice = 0D;
        Map<String, CartDetailDto> details = cart.getDetails();
        for (CartDetailDto cartDetail : details.values()) {
            totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
        }
        return totalPrice;
    }

    @Transactional
    @Override
    public void insert(CartDto cart, Users user, String address, String customer,String phone, String email, String payment, RedirectAttributes redirectAttributes) {
        // insert vao order
        Orders order = new Orders();
        if (user.getUserID().trim().equals("default")){
            order.setCustomer(customer);
        } else {
            order.setCustomer(user.getUsername());
        }
        order.setUser(user);
        if (address.isEmpty() == false) {
            order.setAddress(address);
        }
        if (phone.isEmpty() == false) {
            order.setPhone(phone);
        }
        if (email.isEmpty() == false) {
            order.setEmail(email);
        }
        order.setEmail(email);
        if (cart.getTotalPrice() > 0) {
            order.setPrice(cart.getTotalPrice());
        } else {

        }
        try {
            // khi lưu vào 1 order mới
            //lưu order
            Orders orderRespone = ordersService.insert(order);
            Status status = new Status();
            status.setStatusID(order.getOrderID());
            switch (payment) {
                case "order":
                    status.setStatusname(statusNameDAO.findById(2).get());
                    break;
                case "paypal":
                    status.setStatusname(statusNameDAO.findById(1).get());
                    break;
            }
            status.setReason("Không có");
            status.setCancelOrder(false);
            status.setDescription(descriptionStatusDAO.findById(1).get());
            status.setNotes("Không có");
            status.setCreateDate(new Date());
            status.setTransportFee(cart.getTotalPrice() * 2 / 10);
            status.setFeeCollected(0.0);
            status.setOrders(order);
            // lấy ra thời gian thực
            int dt = java.time.LocalTime.now().getHour();
            //workingSession
            String workingSession = "Ca sáng";
            if (15 <= dt && dt <= 22) {
                workingSession = "Ca chiều";
            }
            //lấy ra nhân viên phụ trách order đó
            staff staff = staffDAO.findStaff(workingSession);
            //số lượng đơn hàng nhân viên này đang quản lý tăng 1
            staff.setOrderProcessing(staff.getOrderProcessing() + 1);
            status.setStaffID(staff);
            statusService.insert(status);
            // Duyet hashmap de insert lan luot vao order_details
            // trong luc duyet hashmap qua tung sp -> di update quantity cho tung san pham
            for (CartDetailDto cartDetail : cart.getDetails().values()) {
                cartDetail.setOrderID(orderRespone.getOrderID());
                orderDetailsService.insert(cartDetail);
                Product_Colors product_colors = productcolorsService.findById(cartDetail.getColorID());
                Integer newAvailable = product_colors.getAvailable() - cartDetail.getQuantity();
                productcolorsService.updateQuantity(newAvailable, cartDetail.getColorID());
            }
            cartApi.senMailStaff(staff.getStaffID().getEmail(), order, redirectAttributes);
            cartApi.senMailUser(staff.getStaffID().getEmail(),order.getEmail(), order, redirectAttributes);
        } catch (Exception ex) {

        }
    }

    private CartDetailDto createNewCartDetail(CartDto cart, Products product, Long colorID, Integer quantity, String categoryID) {
        CartDetailDto cartDetail = new CartDetailDto();
        cartDetail.setProductID(product.getProductID());
        cartDetail.setPrice(product.getPrice());
        cartDetail.setQuantity(quantity);
        cartDetail.setNameProduct(product.getName());
        cartDetail.setImg(product.getImg());
        cartDetail.setCategoryID(categoryID);

        // Kiểm tra màu sản phẩm đã tồn tại trong giỏ hàng hay chưa
        Map<String, CartDetailDto> details = cart.getDetails();
        String colorIDStr = String.valueOf(colorID);
        List<Product_Colors> productColors = productcolorsService.findbyProductID(product.getProductID());
        if (details.containsKey(colorIDStr)) {
            // Màu đã tồn tại trong giỏ hàng, cập nhật số lượng
            CartDetailDto existingDetail = details.get(colorIDStr);
            Integer existingQuantity = existingDetail.getQuantity();
            Integer newQuantity = existingQuantity + quantity;
            existingDetail.setQuantity(newQuantity);
            // Thêm cả 2 màu vào tên sản phẩm
            String existingColorHex = existingDetail.getColorhex();
            String newColorHex = productColors.stream()
                    .filter(c -> c.getColorID().equals(colorID))
                    .map(Product_Colors::getColorhex)
                    .findFirst()
                    .orElse(null);
            String updatedName = product.getName() + " (" + existingColorHex + ", " + newColorHex + ")";
            existingDetail.setNameProduct(updatedName);
        } else {
            // Màu chưa tồn tại trong giỏ hàng, tạo mới chi tiết giỏ hàng
            Product_Colors color = productColors.stream()
                    .filter(c -> c.getColorID().equals(colorID))
                    .findFirst()
                    .orElse(null);

            if (color == null) {
                throw new IllegalArgumentException("Màu sản phẩm không tồn tại");
            }

            cartDetail.setColorID(colorID);
            cartDetail.setColorhex(color.getColorhex());

            // Thêm màu vào tên sản phẩm
            String colorHex = color.getColorhex();
            String updatedName = product.getName() + " (" + colorHex + ")";
            cartDetail.setNameProduct(updatedName);

            details.put(colorIDStr, cartDetail);
        }

        return cartDetail;
    }


}



