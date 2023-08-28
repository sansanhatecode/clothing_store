package com.store.dao;

import com.store.model.staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.store.model.Users;
public interface staffDAO extends JpaRepository<staff, Integer> {
    @Query(nativeQuery = true, value = "select top(1) * from staff where workingSession = :workingSession Order by orderProcessing asc")
    staff findStaff(@Param("workingSession") String workingSession);
    staff findByStaffID(Users id);
	staff findByStaffID(String id);
}
