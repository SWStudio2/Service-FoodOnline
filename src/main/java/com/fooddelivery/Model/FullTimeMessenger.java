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
@Table(name = "fulltime_messenger")
public class FullTimeMessenger {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long full_id;
	
	@NotNull
	private String full_name;
	
	@NotNull
	private String full_contact_number;
	
	@NotNull
	private String full_email;
	
	@NotNull
	private String full_password;
	
	@NotNull
	private String full_status;
	
	@NotNull
	private double full_lastest_lattitude;
	
	@NotNull
	private double full_lastest_longtitude;
	
	@NotNull
	private double full_recommend_lattitude;
	
	@NotNull
	private double full_recommend_longtitude;
	
	public FullTimeMessenger() { }

	public FullTimeMessenger(long fullId) { 
	   this.full_id = fullId;
	}
	  
	public FullTimeMessenger
	(	
			String fullName, 
			String fullContactNumber, 
			String fullEmail, 
			String fullPassword,
			String fullStatus,
			double fullLastestLattitude,
			double fullLastestLongtitude,
			double fullRecommendLattitude,
			double fullRecommendLongtitude
	) {
	  this.full_name = fullName;
	  this.full_contact_number = fullContactNumber;
	  this.full_email = fullEmail;
	  this.full_password = fullPassword;
	  this.full_status = fullStatus;
	  this.full_lastest_lattitude = fullLastestLattitude;
	  this.full_lastest_longtitude = fullLastestLongtitude;
	  this.full_recommend_lattitude = fullRecommendLattitude;
	  this.full_recommend_longtitude = fullRecommendLongtitude;
	  
	}
	
	public long getFullId() {
		return full_id;
	}

	public void setFullId(long fullId) {
		this.full_id = fullId;
	}

	public String getFullName() { 
		return full_name; 
	}

	public void setFullName(String fullName) {
		this.full_name = fullName;
	}
	
	public String getFullContactNumber() {
		return full_contact_number;
	}
	
	public void setFullContactNumber(String fullContactNumber) {
		this.full_contact_number = fullContactNumber;
	}
	
	public String getFullEmail() {
		return full_email;
	}
	
	public void setFullEmail(String fullEmail) {
		this.full_email = fullEmail;
	}
	
	public String getFullPassword() {
		return full_password;
	}

	public void setFullPassword(String fullPassword) {
		this.full_password = fullPassword;
	}
	
	public String getFullStatus() {
		return full_status;
	}
	
	public void setFullStatus(String fullStatus) {
		this.full_status = fullStatus;
	} 

	public Double getFullLatestLattitude() {
		return full_lastest_lattitude;
	}
	
	public void setFullLastestLattitude(Double fullLastestLongtitude) {
		this.full_lastest_lattitude = fullLastestLongtitude;
	}
	
	public Double getFullLatestLongtitude() {
		return full_lastest_longtitude;
	}
	
	public void setFullLastestLongtitude(Double fullLastestLongtitude) {
		this.full_lastest_longtitude = fullLastestLongtitude;
	}
	
	public Double getFullRecommendLattitude() {
		return full_recommend_lattitude;
	}
	
	public void setFullRecommendLattitude(Double fullRecommendLongtitude) {
		this.full_recommend_lattitude = fullRecommendLongtitude;
	}
	
	public Double getFullRecommendLongtitude() {
		return full_recommend_longtitude;
	}
	
	public void setFullRecommendLongtitude(Double fullRecommendLongtitude) {
		this.full_recommend_longtitude = fullRecommendLongtitude;
	}
}