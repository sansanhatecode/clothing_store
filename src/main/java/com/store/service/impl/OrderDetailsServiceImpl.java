package com.store.service.impl;

import com.store.dao.OrderDetailDAO;
import com.store.DTO.CartDetailDto;
import com.store.model.Order_Details;
import com.store.model.Orders;
import com.store.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    @Autowired
    private OrderDetailDAO dao;

    @Override
    public void insert(CartDetailDto cartDetailDto) {
        dao.insert(cartDetailDto);
    }

    @Override
    public List<Order_Details> findByOrderID(Long orderID) {
        return dao.findByOrderID(orderID);
    }

}
