package com.store.service;

import java.util.List;

import com.store.model.Users;
import com.store.util.UserNotFoundException;

public interface UserService {
	public List<Users> findAll();

	public Users findById(String id);

	public Users create(Users product);

	public Users update(Users product);

	public void delete(String id);
	
	public List<Users> getAdministrators();

	public List<Users> searchUsers(String keyword);

	public List<Users> findByAuthorities_Role_RoleID(String roleId);


	Users findByEmailCheck(String email);

	Users findByPhone(String phone);




	public Users doLogin(String userID, String checkpassword);


	

	public Users findByEmail(String email);

	public boolean isEmailExists(String email);

	public boolean isUserIDExists(String userID);





	Users save(Users user, String userID);
	
	void updateResetPasswordToken(String token, String email) throws UserNotFoundException;
	
	Users getByResetPasswordToken(String token);
	
	void updatePassword(Users user, String password);

	int findCountUserID(String value);
	int findCountphone(String value);
	int findCountEmail(String value);
}
