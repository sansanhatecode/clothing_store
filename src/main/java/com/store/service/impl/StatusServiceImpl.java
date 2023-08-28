package com.store.service.impl;





import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.store.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.store.dao.StatusDAO;
import com.store.service.StatusService;

import javax.transaction.Transactional;

@Service
public class StatusServiceImpl implements StatusService {
	@Autowired
	StatusDAO dao;
	
	@Override
	public List<Status> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public Status findById(Long id) {
		// TODO Auto-generated method stub
		return dao.findByStatusID(id);
	}

	@Override
	public Status update(Status status) {
		return dao.save(status);	
	}
	@Override
	@Transactional(rollbackOn = {Exception.class, Throwable.class})
	public Status updateStatus(Status status)  {
		return dao.saveAndFlush(status);
	}
	@Override
	public Status updateDesAndDate(Status status) {
		return dao.save(status);
	}


	@Override
	public Status insert(Status status) {
		// TODO Auto-generated method stub
		return dao.saveAndFlush(status);
	}

	@Override
	public Status findByOrderID(Orders order) {
		return dao.findByOrders(order);
	}

	@Override
	public List<Status> findByCurrentStaff(staff st) {
		return dao.findByStaffIDDESC(st);
	}

	@Override
	public List<Status> findByStatusAndDesc(staff st, long desc) {
		return dao.findByStaffAndStatusName(st,desc);
	}

	@Override
	public List<Status> findAllDesc(long desc) {
		return dao.findAllDesc(desc);
	}

	@Override
	public List<Status> findAllDesc2(long desc, long desc2) {
		return dao.findAlldesc2(desc, desc2);
	}

	@Override
	public List<Status> findByStatusAndDesc1(staff st, long desc, long desc1) {
		return dao.findByStaffAndStatusName2(st,desc,desc1);
	}

	@Override
	public List<Status> findOrderID(Long orderID) {
		return dao.findOrderID(orderID);
	}
	@Override
	public Page<Status> findPaginatedOrder(Pageable pageable, List sql) {
		List<Status> orders = sql;
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Status> list;

		if (orders.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, orders.size());
			list = orders.subList(startItem, toIndex);
		}
		Page<Status> pageOrders = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), orders.size());
		return pageOrders;
	}
}
