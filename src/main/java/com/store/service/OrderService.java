package com.store.service;

import com.store.model.Orders;
import com.store.model.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderService {
    Page<Orders> findPaginatedOrder(Pageable pageable, List sql);

}
