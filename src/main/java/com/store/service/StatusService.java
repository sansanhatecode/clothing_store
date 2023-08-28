package com.store.service;


import java.util.Date;
import java.util.List;

import com.store.model.Orders;
import com.store.model.descriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.store.model.Status;

import com.store.model.staff;

@Service
public interface StatusService {
	public List<Status> findAll();

	public Status findById(Long id);

	public Status update(Status status);
	public Status updateStatus(Status status);
	public Status updateDesAndDate(Status status);
    Status insert(Status status);
	public Status findByOrderID(Orders order);
	public List<Status> findByCurrentStaff(staff st);
	public List<Status> findByStatusAndDesc(staff st, long desc);
	public List<Status> findAllDesc(long desc);
	public List<Status> findAllDesc2(long desc, long desc2);
	public List<Status> findByStatusAndDesc1(staff st, long desc,long desc1);

	List<Status> findOrderID(Long orderID);
	Page<Status> findPaginatedOrder(Pageable pageable, List sql);

}
