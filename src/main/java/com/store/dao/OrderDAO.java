package com.store.dao;

import com.store.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDAO extends JpaRepository<Orders, Long> {

    @Query(value = "SELECT * from orders where userID = :userID Order BY orderID desc ",nativeQuery = true)
    Page<Orders> findByUserID(String userID, Pageable pageable);

    @Query(value = "select * from orders Order by orderID desc", nativeQuery = true)
    List<Orders> findAllDESC();
    ////////////////
    /// query order theo tháng năm và trạng thái
    @Query(value = "SELECT cast(datepart(month,Date) as varchar)+'-'+cast(datepart(yyyy,Date) as varchar) as MonthAndYear\n" +
            "FROM orders group by  cast(datepart(MONTH,Date) as varchar)+'-'+cast(datepart(yyyy,Date) as varchar)", nativeQuery = true)
    List<String> findDateAnDCountMonth();
    @Query(value = "select orders.orderID from orders inner join status on status.orderID = orders.orderID " +
            "where MONTH(orders.date) = :Month and YEAR(orders.date) = :year and status.description = '5' order by orderID DESC",nativeQuery = true)
    List<Long> findOrderByMonthAndDone(@Param("Month")String month, @Param("year") String year);
    @Query(value = "select orders.orderID from orders inner join status on status.orderID = orders.orderID " +
            "where MONTH(orders.date) = :Month and YEAR(orders.date) = :year and status.description = '4' order by orderID DESC",nativeQuery = true)
    List<Long> findOrderByMonthAndCancelOrder(@Param("Month")String month, @Param("year") String year);
    @Query(value = "select orders.orderID from orders inner join status on status.orderID = orders.orderID " +
            "where MONTH(orders.date) = :Month and YEAR(orders.date) = :year and (status.description = '2' or status.description = '3' or status.description = '1') order by orderID DESC",nativeQuery = true)
    List<Long> findOrderByMonthAndProcessing(@Param("Month")String month, @Param("year") String year);
    @Query(value = "select orders.orderID from orders inner join status on status.orderID = orders.orderID " +
            "where MONTH(orders.date) = :Month and YEAR(orders.date) = :year order by orderID DESC",nativeQuery = true)
    List<Long> findOrderByMonth(@Param("Month")String month, @Param("year") String year);
    /////////
    //query order theo trangj thai
    @Query(value = "select orders.orderID from orders inner join status on status.orderID = orders.orderID " +
            "where  status.description = :stt order by orderID DESC",nativeQuery = true)
    List<Long> findOrderByStatus(@Param("stt") String stt );
    @Query(value = "select orders.orderID from orders inner join status on status.orderID = orders.orderID " +
            "where  status.description = '1' or status.description = '2' or status.description = '3' order by orderID DESC",nativeQuery = true)
    List<Long> findOrderByStatusProcessing();
    @Query(value = "select count(*) from orders inner join status on status.orderID = orders.orderID where MONTH(orders.date) = :Month and YEAR(orders.date) = :year and status.description = :stt", nativeQuery = true)
    int countByMonthAndYearAndStatus(@Param("Month") String month, @Param("year") String year, @Param("stt") String stt);
    @Query(value = "select count(*) from orders inner join status on status.orderID = orders.orderID where status.description = :stt", nativeQuery = true)
    int countByStatus(@Param("stt") String stt);
    @Query(value = "select count(*) from orders inner join status on status.orderID = orders.orderID where (status.description = '1' or  status.description = '2' or  status.description = '3')", nativeQuery = true)
    int countByStatusProcessing();
    @Query(value = "select count(*) from orders inner join status on status.orderID = orders.orderID where MONTH(orders.date) = :month and YEAR(orders.date) = :year  and (status.description = '1' or  status.description = '2' or  status.description = '3')", nativeQuery = true)
    int countByStatusProcessingAndMonthYear(@Param("month") String month, @Param("year") String year);
    @Query(value = "select count(*) from orders inner join status on status.orderID = orders.orderID", nativeQuery = true)
    int countAll();
    @Query(value = "select count(*) from orders inner join status on status.orderID = orders.orderID where MONTH(orders.date) = :month and YEAR(orders.date) = :year", nativeQuery = true)
    int countByMonthAndYear(@Param("month") String month, @Param("year") String year);
    Orders findByOrderID(Long OrderID);
}
