package com.store.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.store.model.Orders;

import java.util.List;


@Repository
public interface StatisticDAO extends JpaRepository<Orders, Long>{
	@Query(value = "{CALL sp_getToltalPriceMonth(:day,:month, :year)}", nativeQuery = true )
	String getTotalPriceMonth(@Param("day") String day,@Param("month") String month,@Param("year") String year);

	@Query(value = "select * from Fn_TongSP(:day,:month, :year)", nativeQuery = true )
	List<String> getProductTotal(@Param("day")String day,@Param("month")String month, @Param("year") String year);
}
