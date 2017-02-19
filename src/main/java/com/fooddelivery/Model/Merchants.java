package com.fooddelivery.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "merchants")
public class Merchants {
	private long merID;
	
	private String merName;
	
	private String merAddress;
	
	private String merLatitude;
	
	private String merLongtitude;
	
	private String merContractNumber;
	
	private int merPercentShare;
	
	private String merOpenStatus;
	
	private String merUserName;
	
	private String merPassword;
	
	private boolean merRegisFlag;

	public long getMerID() {
		return merID;
	}

	public void setMerID(long merID) {
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

	public String getMerContractNumber() {
		return merContractNumber;
	}

	public void setMerContractNumber(String merContractNumber) {
		this.merContractNumber = merContractNumber;
	}

	public int getMerPercentShare() {
		return merPercentShare;
	}

	public void setMerPercentShare(int merPercentShare) {
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

	public boolean isMerRegisFlag() {
		return merRegisFlag;
	}

	public void setMerRegisFlag(boolean merRegisFlag) {
		this.merRegisFlag = merRegisFlag;
	}
	
	
}
