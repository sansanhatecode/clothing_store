package com.store.service;

import com.store.model.Orders;
import com.store.model.Products;
import com.store.model.Users;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrdersService {
    Orders insert(Orders orders);

    Page<Orders> findByUserID(String userId, int pageSize, int pageNumber) throws  Exception;

    List<Orders> findByAll();

     Orders findById(Long Order);
}
