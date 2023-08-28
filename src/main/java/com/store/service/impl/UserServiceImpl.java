package com.store.service.impl;

import java.util.List;
import java.util.Optional;

import com.store.model.Authorities;
import com.store.model.Roles;
import com.store.service.AuthoritiesService;
import com.store.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.store.dao.UsersDAO;
import com.store.model.Users;
import com.store.service.UserService;
import com.store.util.UserNotFoundException;




@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UsersDAO dao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;


	private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

	
	@Autowired
	AuthoritiesService authoritiesService;
	
	@Autowired
	RoleService roleService;

	@Override
	public List<Users> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public Users findById(String id) {
		// TODO Auto-generated method stub
		return dao.findByUserID(id);
	}

	@Override
	public Users create(Users user) {
		// TODO Auto-generated method stub
		return dao.save(user);
	}

	@Override
	public Users update(Users user) {
		// TODO Auto-generated method stub
		return dao.save(user);
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		dao.deleteById(id);
	}

	@Override
	public List<Users> getAdministrators() {
		// TODO Auto-generated method stub
		return dao.getAdminitrators();
	}

	@Override
	public List<Users> searchUsers(String keyword) {
		// TODO Auto-generated method stub
		return dao.findByUsernameContaining(keyword);
	}

	@Override
	public List<Users> findByAuthorities_Role_RoleID(String roleId) {
		// TODO Auto-generated method stub
		return dao.findByAuthorities_Role_RoleID(roleId);
	}

	@Override
	public Users findByEmailCheck(String email) {
		Users users = dao.findByEmail(email);
		if (users != null)
		{
			return users;
		}else
			return null;
	}

	@Override
	public Users findByPhone(String phone) {
		Users users = dao.findByPhone(phone);
		if (users != null)
		{
			return users;
		}else
			return null;
	}

	@Override
	public Users doLogin(String userID, String checkpassword) {
	    Users user = dao.findByUserID(userID);
	    if (null != user) {

	        String password = user.getPassword();

	        boolean check = passwordEncoder.matches(checkpassword, password);
	        if (check) {
	            List<Authorities> authoritiesList = user.getAuthorities();
	            for (Authorities authorities : authoritiesList) {
	                String userRole = authorities.getRole().getRoleID();
	                if (userRole.equalsIgnoreCase("admin")) {
	                    return user; // Trả về người dùng nếu có vai trò admin
	                }
	            }
	        }
	    }
	    return user; // Trả về null nếu không xác thực thành công hoặc không có vai trò admin
	}

	@Override
	public Users save(Users user, String UserID) {
		Users users = dao.findByUserID(UserID);

		if (null == users){
			user.setIsDeleted(Boolean.TRUE);
			String HashPassword = bcrypt.encode(user.getPassword());
			user.setPassword(HashPassword);
			Users user1 =dao.saveAndFlush(user);
			Roles role = roleService.findByRoleID("customer");
			Authorities authorities = new Authorities();
			authorities.setRole(role);
			authorities.setUser(user1);
			Authorities authorities1 = authoritiesService.create(authorities);

			return user;
		}else {
			return null;
		}
	}


	@Override
	public Users findByEmail(String email) {
		// TODO Auto-generated method stub
		return dao.findByEmail(email);
	}


	@Override
	public boolean isEmailExists(String email) {
		// TODO Auto-generated method stub
		return dao.existsByEmail(email);
	}


	@Override
	public boolean isUserIDExists(String userID) {
		// TODO Auto-generated method stub
		return dao.existsById(userID);
	}


     
    public Users getByResetPasswordToken(String token) {
        return dao.findByResetPasswordToken(token);
    }
     
    public void updatePassword(Users users, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        users.setPassword(encodedPassword);
         
        users.setResetPasswordToken(null);
        dao.save(users);
    }

	@Override
	public int findCountUserID(String value) {
		return dao.findCoutUserID(value);
	}

	@Override
	public int findCountphone(String value) {
		return dao.findCoutphone(value);
	}

	@Override
	public int findCountEmail(String value) {
		return dao.findCoutEmail(value);
	}


	@Override
	public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
		Users users = dao.findByEmail(email);
        if (users != null) {
            users.setResetPasswordToken(token);
            dao.save(users);
        } else {
            throw new UserNotFoundException("Không tìm thấy khách hàng nào có email " + email);
        }
		
	}	
}
