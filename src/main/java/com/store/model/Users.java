package com.store.model;





import javax.validation.constraints.NotNull;


import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")

public class Users implements Serializable{
	@Id
	@NotBlank(message = "Không để trống UserID")
	String userID;
	@NotBlank(message = "Không để trống UserName")
	String username;
	@NotBlank(message = "Không để trống Password")
	String password;
	@NotBlank(message = "Không để trống Phone")
	String phone;
	@NotBlank(message = "Không để trống Email")
	@Email(message = "Không đúng định dạng email")
	String email;
	@NotBlank(message = "Không để trống Address")
	String address;
	Boolean isDeleted;
	@Column(name = "reset_password_token")
	private String resetPasswordToken;
	@Temporal(TemporalType.DATE)
	@Column(name = "createdDate")
	Date createDate = new Date();
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Orders> orders;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	List<Authorities> authorities;
	
	

}
