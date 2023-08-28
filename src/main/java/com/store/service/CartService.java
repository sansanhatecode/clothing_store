package com.store.service;

import com.store.DTO.CartDto;
import com.store.model.Users;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface CartService {
    CartDto updateCart(CartDto cart, String productID, Long colorID, Integer quantity,String categoryID, boolean isReplace);

    Integer getTotalQuantity(CartDto cart);

    Double getTotalPrice(CartDto cart);

    void insert(CartDto cart, Users user,String customer, String address, String phone, String email, String payment, RedirectAttributes redirectAttributes);
}
