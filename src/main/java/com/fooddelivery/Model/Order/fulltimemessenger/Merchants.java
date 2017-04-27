package com.fooddelivery.Model.Order.fulltimemessenger;

public class Merchants {

	private int merID;

	private String merName;
	private String merAddress;
	private String merLatitude;
	private String merLongtitude;
	private String merContactNumber;
	private Double merPercentShare;
	private String merOpenStatus;
	private String merUserName;
	private String merPassword;
	private Integer merRegisFlag;
	private String merPicName;
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
	
	//----------MAP MODEL---------------
	public void mapping(com.fooddelivery.Model.Merchants merchant) {
		this.merID = merchant.getMerID();
		this.merName = merchant.getMerName();
		this.merAddress = merchant.getMerAddress();
		this.merLatitude = merchant.getMerLatitude();
		this.merLongtitude = merchant.getMerLongtitude();
		this.merContactNumber = merchant.getMerContactNumber();
		this.merPercentShare = merchant.getMerPercentShare();
		this.merOpenStatus = merchant.getMerOpenStatus();
		this.merUserName = merchant.getMerUserName();
		this.merPassword = merchant.getMerPassword();
		this.merRegisFlag = merchant.getMerRegisFlag();
		this.merPicName = merchant.getMerPicName();
		this.cookingTime = merchant.getCookingTime();
	}
}
