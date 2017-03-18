package com.fooddelivery.Model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "operator")
public class Operator {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long oper_id;
	
	@NotNull
	private String oper_name;
	
	@NotNull
	private String oper_contact_number;
	
	@NotNull
	private String oper_username;
	
	@NotNull
	private String oper_password;
	
	public Operator() { }

	public Operator(long operId) { 
	   this.oper_id = operId;
	}
	  
	public Operator
	(
			String operName, 
			String operContactNumber, 
			String operUsername,
			String operPassword
	) {
	  this.oper_name = operName;
	  this.oper_contact_number = operContactNumber;
	  this.oper_username = operUsername;
	  this.oper_password = operPassword;
	}
	
	public long getOperId() {
		return oper_id;
	}

	public void setOperId(long operId) {
		this.oper_id = operId;
	}

	public String getOperName() {
		return oper_name;
	}

	public void setOperName(String operName) {
		this.oper_name = operName;
	}

	public String getOperContactNumber() { 
		return oper_contact_number; 
	}

	public void setOperContactNumber(String operContactNumber) {
		this.oper_contact_number = operContactNumber;
	}
	
	public String getOperUsername() {
		return oper_username;
	}
	
	public void setOperUsername(String operUsername) {
		this.oper_username = operUsername;
	}

	public String getOperPassword() {
		return oper_password;
	}

	public void setOperPassword(String operPassword) {
		this.oper_password = operPassword;
	}
	
	
	
}
