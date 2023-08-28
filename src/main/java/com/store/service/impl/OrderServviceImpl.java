package com.store.service.impl;

import com.store.model.Orders;
import com.store.model.Products;
import com.store.service.OrderService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
@Transactional
public class OrderServviceImpl implements OrderService {
    // phân trang
    @Override
    public Page<Orders> findPaginatedOrder(Pageable pageable, List sql) {
        List<Orders> orders = sql;
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Orders> list;

        if (orders.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, orders.size());
            list = orders.subList(startItem, toIndex);
        }
        Page<Orders> pageOrders = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), orders.size());
        return pageOrders;
    }
}
