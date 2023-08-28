package com.store.dao;


import java.util.Date;
import java.util.List;

import com.store.model.staff;
import com.store.model.Orders;
import com.store.model.descriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.store.model.staff;
import com.store.model.Status;
import org.springframework.data.repository.query.Param;
import com.store.model.staff;
import javax.transaction.Transactional;


public interface StatusDAO extends JpaRepository<Status, Long> {
    Status findByStatusID(Long id);

    @Query(value = "select * from Status where staffID = :staff  order by statusID DESC ", nativeQuery = true)
    List<Status> findByStaffIDDESC(@Param("staff") staff st);

    @Query(value = "select * from Status where staffID = :staff and description = :idDesc order by statusID DESC ", nativeQuery = true)
    List<Status> findByStaffAndStatusName(@Param("staff") staff st,@Param("idDesc") long id);
    @Query(value = "select * from Status where  description = :idDesc order by statusID DESC ", nativeQuery = true)
    List<Status> findAllDesc(@Param("idDesc") long id);
    @Query(value = "select * from Status where staffID = :staff and (description = :idDesc or description = :idDesc1) order by statusID DESC ", nativeQuery = true)
    List<Status> findByStaffAndStatusName2(@Param("staff") staff st,@Param("idDesc") long id,@Param("idDesc1") long id1);
    @Query(value = "select * from Status where description = :idDesc or description = :idDesc1 order by statusID DESC ", nativeQuery = true)
    List<Status> findAlldesc2(@Param("idDesc") long id,@Param("idDesc1") long id1);
    @Query(value = "select * from Status where staffID = :id and cancelOrder = 1  order by statusID DESC ", nativeQuery = true)
    List<Status> finddescCancelOrder(@Param("id") long id);
    @Query(value = "select * from Status where  cancelOrder = 1  order by statusID DESC ", nativeQuery = true)
    List<Status> finddescAllCancelOrder();
    Status findByOrders(Orders orders);

    @Query(value = "select count(*) from Status where description = '1' and staffID= :id")
    public int selectCountStatusName(@Param("id") staff id);
    @Query(value = "select count(*) from Status where description = '1'")
    public int selectCountStatusNameAll();
    @Modifying(clearAutomatically = true)
    @Transactional
    //: Parameter value [2] did not match expected type [com.store.model.descriptionStatu
    @Query(value = "update Status set description = :descID  WHERE orders = :ID")
    void UpdateDescAndDate(@Param("descID") descriptionStatus desc, @Param("ID") Orders id);

    @Query(value = "select count(*) from Status where staffID= :id", nativeQuery = true)
    int findAllStatusByStaff(@Param("id") staff id);
    @Query(value = "select count(*) from Status ", nativeQuery = true)
    int findAllStatusByAll();
    @Query(value = "select count(*) from Status  where description = 5 and staffID = :id ", nativeQuery = true)
    int findStatusByStaffDone(@Param("id") staff id);

    @Query(value = "select count(*) from Status  where canCelOrder = 1 ", nativeQuery = true)
    int findStatusAllByCancelOrder();
    @Query(value = "select count(*) from Status  where description = 5 ", nativeQuery = true)
    int findStatusByStaffDoneAll();
    @Query(value = "select count(*) from Status  where description = 4 and staffID = :id ", nativeQuery = true)
    int findStatusByStaffFalse(@Param("id") staff id);
    @Query(value = "select count(*) from Status  where canCelOrder = 1 and staffID = :id", nativeQuery = true)
    int findStatusByCancelOrder(@Param("id") staff id);
    @Query(value = "select count(*) from Status  where description = 4", nativeQuery = true)
    int findStatusByStaffFalseAll();

    @Query(value = "SELECT * from status where orderID = :orderID  ",nativeQuery = true)
    List<Status> findOrderID(Long orderID);
}
