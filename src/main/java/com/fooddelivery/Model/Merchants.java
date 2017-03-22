package com.fooddelivery.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "merchants")
public class Merchants {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "MER_ID")
	private int merID;
	
	@Column(name = "MER_NAME")
	private String merName;
	
	@Column(name = "MER_ADDRESS")
	private String merAddress;
	
	@Column(name = "MER_LATITUDE")
	private String merLatitude;
	
	@Column(name = "MER_LONGTITUDE") 
	private String merLongtitude;
	
	@Column(name = "MER_CONTACT_NUMBER")
	private String merContactNumber;
	
	@Column(name = "MER_PERCENT_SHARE")
	private Double merPercentShare;
	
	@Column(name = "MER_OPEN_STATUS")
	private String merOpenStatus;
	
	@Column(name = "MER_USERNAME")
	private String merUserName;
	
	@Column(name = "MER_PASSWORD")
	private String merPassword;
	
	@Column(name = "MER_REGIS_FLAG")
	private Integer merRegisFlag;
	
	@Column(name = "MER_PIC_NAME")
	private String merPicName;
	
	@Column(name = "MER_COOKTIME")
	private Integer cookingTime;

	public int getCookingTime() {
		return cookingTime;
	}

	public void setCookingTime(int cookingTime) {
		this.cookingTime = cookingTime;
	}

	public int getMerID() {
		return merID;
	}

	public void setMerID(int merID) {
		this.merID = merID;
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public String getMerAddress() {
		return merAddress;
	}

	public void setMerAddress(String merAddress) {
		this.merAddress = merAddress;
	}

	public String getMerLatitude() {
		return merLatitude;
	}

	public void setMerLatitude(String merLatitude) {
		this.merLatitude = merLatitude;
	}

	public String getMerLongtitude() {
		return merLongtitude;
	}

	public void setMerLongtitude(String merLongtitude) {
		this.merLongtitude = merLongtitude;
	}

	public String getMerContactNumber() {
		return merContactNumber;
	}

	public void setMerContactNumber(String merContactNumber) {
		this.merContactNumber = merContactNumber;
	}

	public Double getMerPercentShare() {
		return merPercentShare;
	}

	public void setMerPercentShare(Double merPercentShare) {
		this.merPercentShare = merPercentShare;
	}

	public String getMerOpenStatus() {
		return merOpenStatus;
	}

	public void setMerOpenStatus(String merOpenStatus) {
		this.merOpenStatus = merOpenStatus;
	}

	public String getMerUserName() {
		return merUserName;
	}

	public void setMerUserName(String merUserName) {
		this.merUserName = merUserName;
	}

	public String getMerPassword() {
		return merPassword;
	}

	public void setMerPassword(String merPassword) {
		this.merPassword = merPassword;
	}

	public Integer getMerRegisFlag() {
		return merRegisFlag;
	}

	public void setMerRegisFlag(Integer merRegisFlag) {
		this.merRegisFlag = merRegisFlag;
	}

	public String getMerPicName() {
		return merPicName;
	}

	public void setMerPicName(String merPicName) {
		this.merPicName = merPicName;
	}

	public void setCookingTime(Integer cookingTime) {
		this.cookingTime = cookingTime;
	}

	
	
	
}
