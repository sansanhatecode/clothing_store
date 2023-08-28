package com.store.dao;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.store.model.Users;
import org.springframework.data.repository.query.Param;


public interface UsersDAO extends JpaRepository<Users, String> {
	@Query("SELECT DISTINCT ar.user FROM Authorities ar WHERE ar.role.id IN ('staff', 'admin','customer')")
	List<Users> getAdminitrators();

	@Query("Select user from Users "
			+ "where userID like 'keyword' or username like 'keyword' or email like 'keyword' ")
	List<Users> findByKeyword(String keyword);

	@Query(value = "select count(*) from Users where userID = :value", nativeQuery = true)
	int findCoutUserID( @Param("value") String value);
	@Query(value = "select count(*) from Users where phone = :value", nativeQuery = true)
	int findCoutphone( @Param("value") String value);
	@Query(value = "select count(*) from Users where email = :value", nativeQuery = true)
	int findCoutEmail( @Param("value") String value);
	List<Users> findByUsernameContaining(String keyword);


	List<Users> findByAuthorities_Role_RoleID(String roleID);

	Users findByUserID(String userID);



	boolean existsByEmail(String email);


	Users findByEmail(String email);

    Users findByResetPasswordToken(String token);
	Users findByPhone(String phone);



}
