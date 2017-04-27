package com.fooddelivery.Model.Order.fulltimemessenger;

import java.util.Date;

public class Customer {

	private long cusId;
	private String cusName;
	private String cusUsername;
	private String cusPassword;
	private String cusContactNumber;
	private Date cusCreatedDate;

	public Customer() { }

	public Customer(long cusId) { 
	   this.cusId = cusId;
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
	  this.cusName = cusName;
	  this.cusUsername = cusUserName;
	  this.cusPassword = cusPassword;
	  this.cusContactNumber = cusContactNumber;
	  this.cusCreatedDate = cusCreatedDate;
	}
	
	public long getCusId() {
		return cusId;
	}

	public void setCusId(long cusId) {
		this.cusId = cusId;
	}

	public String getCusName() { 
		return cusName; 
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	
	public String getCusUserName() {
		return cusUsername;
	}
	
	public void setCusUserName(String cusUserName) {
		this.cusUsername = cusUserName;
	} 

	public String getCusPassword() {
		return cusPassword;
	}

	public void setCusPassword(String cusPassword) {
		this.cusPassword = cusPassword;
	}
	
	public String getCusContactNumber() {
		return cusContactNumber;
	}
	
	public void setCusContactNumber(String cusContactNumber) {
		this.cusContactNumber = cusContactNumber;
	}
	
	public Date getCusCreatedDate() {
		return cusCreatedDate;
	}
	
	public void setCusCreatedDate(Date cusCreatedDate) {
		this.cusCreatedDate = cusCreatedDate;
	}
	
	//------------MAP MODEL-------------
	public void mapping(com.fooddelivery.Model.Customer customer){
		this.cusId = customer.getCusId();
		this.cusName = customer.getCusName();
		this.cusUsername = customer.getCusUserName();
		this.cusPassword = customer.getCusPassword();
		this.cusContactNumber = customer.getCusContactNumber();
		this.cusCreatedDate = customer.getCusCreatedDate();
	}
	
}
