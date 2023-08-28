package com.store.service;

import com.store.DTO.CartDetailDto;
import com.store.model.Order_Details;
import com.store.model.Orders;

import java.util.List;

public interface OrderDetailsService  {
    void insert(CartDetailDto cartDetailDto);

    List<Order_Details> findByOrderID(Long orderID);
}
