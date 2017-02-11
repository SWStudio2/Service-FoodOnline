package com.fooddelivery.Model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long cus_id;
	
	@NotNull
	private String cus_email;
	
	@NotNull
	private String cus_name;
	
	@NotNull
	private String cus_username;
	
	@NotNull
	private String cus_password;
	
	@NotNull
	private String cus_contact_number;
	
	@NotNull
	private Date cus_created_date;
	
	public Customer() { }

	public Customer(long cusId) { 
	   this.cus_id = cusId;
	}
	  
	public Customer
	(	
			String cusEmail, 
			String cusName, 
			String cusUserName, 
			String cusPassword,
			String cusContactNumber,
			Date cusCreatedDate
	) {
	  this.cus_email = cusEmail;
	  this.cus_name = cusName;
	  this.cus_username = cusUserName;
	  this.cus_password = cusPassword;
	  this.cus_contact_number = cusContactNumber;
	  this.cus_created_date = cusCreatedDate;
	}
	
	public long getCusId() {
		return cus_id;
	}

	public void setCusId(long cusId) {
		this.cus_id = cusId;
	}

	public String getCusEmail() {
		return cus_email;
	}

	public void setCusEmail(String cusEmail) {
		this.cus_email = cusEmail;
	}

	public String getCusName() { 
		return cus_name; 
	}

	public void setCusName(String cusName) {
		this.cus_name = cusName;
	}
	
	public String getCusUserName() {
		return cus_username;
	}
	
	public void setCusUserName(String cusUserName) {
		this.cus_username = cusUserName;
	} 

	public String getCusPassword() {
		return cus_password;
	}

	public void setCusPassword(String cusPassword) {
		this.cus_password = cusPassword;
	}
	
	public String getCusContactNumber() {
		return cus_contact_number;
	}
	
	public void setCusContactNumber(String cusContactNumber) {
		this.cus_contact_number = cusContactNumber;
	}
	
	public Date getCusCreatedDate() {
		return cus_created_date;
	}
	
	public void setCusCreatedDate(Date cusCreatedDate) {
		this.cus_created_date = cusCreatedDate;
	}
}
