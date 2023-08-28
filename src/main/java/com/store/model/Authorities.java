package com.store.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;



import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;



@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authorities", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"userID", "roleID"})
})
public class Authorities {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer authoritiesID;
	@ManyToOne @JoinColumn(name = "userID")
	private Users user;
	@ManyToOne  @JoinColumn(name = "roleID")
	private Roles role;
	
	
}
