package com.fooddelivery.Model.Order.fulltimemessenger;

public class FullTimeMessenger {

	private long fullId;
	private String fullName;
	private String fullContactNumber;
	private String fullEmail;
	private String fullPassword;
	private int fullStatusId;
	private String fullLastestLattitude;
	private String fullLastestLongtitude;
	private String fullRecommendLattitude;
	private String fullRecommendLongtitude;

	public FullTimeMessenger() { }

	public FullTimeMessenger(long fullId) { 
	   this.fullId = fullId;
	}
	  
	public FullTimeMessenger
	(	
			String fullName, 
			String fullContactNumber, 
			String fullEmail, 
			String fullPassword,
			int fullStatus,
			String fullLastestLattitude,
			String fullLastestLongtitude,
			String fullRecommendLattitude,
			String fullRecommendLongtitude
	) {
	  this.fullName = fullName;
	  this.fullContactNumber = fullContactNumber;
	  this.fullEmail = fullEmail;
	  this.fullPassword = fullPassword;
	  this.fullStatusId = fullStatus;
	  this.fullLastestLattitude = fullLastestLattitude;
	  this.fullLastestLongtitude = fullLastestLongtitude;
	  this.fullRecommendLattitude = fullRecommendLattitude;
	  this.fullRecommendLongtitude = fullRecommendLongtitude;
	  
	}
	
	public long getFullId() {
		return fullId;
	}

	public void setFullId(long fullId) {
		this.fullId = fullId;
	}

	public String getFullName() { 
		return fullName; 
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getFullContactNumber() {
		return fullContactNumber;
	}
	
	public void setFullContactNumber(String fullContactNumber) {
		this.fullContactNumber = fullContactNumber;
	}
	
	public String getFullEmail() {
		return fullEmail;
	}
	
	public void setFullEmail(String fullEmail) {
		this.fullEmail = fullEmail;
	}
	
	public String getFullPassword() {
		return fullPassword;
	}

	public void setFullPassword(String fullPassword) {
		this.fullPassword = fullPassword;
	}
	
	public int getFullStatusId() {
		return fullStatusId;
	}
	
	public void setFullStatusId(int fullStatus) {
		this.fullStatusId = fullStatus;
	} 

	public String getFullLatestLattitude() {
		return fullLastestLattitude;
	}
	
	public void setFullLastestLattitude(String fullLastestLongtitude) {
		this.fullLastestLattitude = fullLastestLongtitude;
	}
	
	public String getFullLatestLongtitude() {
		return fullLastestLongtitude;
	}
	
	public void setFullLastestLongtitude(String fullLastestLongtitude) {
		this.fullLastestLongtitude = fullLastestLongtitude;
	}
	
	public String getFullRecommendLattitude() {
		return fullRecommendLattitude;
	}
	
	public void setFullRecommendLattitude(String fullRecommendLongtitude) {
		this.fullRecommendLattitude = fullRecommendLongtitude;
	}
	
	public String getFullRecommendLongtitude() {
		return fullRecommendLongtitude;
	}
	
	public void setFullRecommendLongtitude(String fullRecommendLongtitude) {
		this.fullRecommendLongtitude = fullRecommendLongtitude;
	}
	
	//----------MAP MODEL----------------
	public void mapping(com.fooddelivery.Model.FullTimeMessenger fullTimeMessenger) {
		this.fullId = fullTimeMessenger.getFullId();
		this.fullName = fullTimeMessenger.getFullName();
		this.fullContactNumber = fullTimeMessenger.getFullContactNumber();
		this.fullEmail = fullTimeMessenger.getFullEmail();
		this.fullPassword = fullTimeMessenger.getFullPassword();
		this.fullStatusId = fullTimeMessenger.getFullStatusId();
		this.fullLastestLattitude = fullTimeMessenger.getFullLatestLattitude();
		this.fullLastestLongtitude = fullTimeMessenger.getFullLatestLongtitude();
		this.fullRecommendLattitude = fullTimeMessenger.getFullRecommendLattitude();
		this.fullRecommendLongtitude = fullTimeMessenger.getFullRecommendLongtitude();
	  
	}
	
}
