package com.store.service;

import java.util.List;


import com.store.model.Authorities;
import com.store.model.Users;

public interface AuthoritiesService {


	List<Authorities> findAuthoritiesOfAdministrators();

	public Authorities createFromUser(Users user);

	public void delete(Integer id);

	Authorities create(Authorities auth);

	List<Authorities> findByUserID(String userID);
	


}
