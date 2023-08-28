package com.store.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.store.model.Authorities;
import com.store.model.Roles;
import com.store.model.Users;

public interface AuthoritiesDAO extends JpaRepository<Authorities, Integer> {
	@Query("SELECT DISTINCT a FROM Authorities a WHERE a.user IN ?1")
	List<Authorities> authoritiesOf(List<Users> accounts);



	List<Authorities> findByUserUserID(String userID);
}
