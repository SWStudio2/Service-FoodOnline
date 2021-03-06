package com.fooddelivery.Model.Order;

import java.util.List;

public class CustOrder {
	private String orderAddress;
	private String orderAddressLatitude;
	private String orderAddressLongitude;
	private double orderTotalPrice;
	private double orderFoodPrice;
	private double orderDeliveryPrice;
	private double orderDistance;
	private String confirmCode;
	private String orderNo;
	private int orderStatus;
	private String orderCreatedDateTime;
	private int estimatedTime;
	private String estimatedDateTime;
	private String orderPaymentType;
	private List<MerchantOrder> merchantOrderList;

	public String getEstimatedDateTime() {
		return estimatedDateTime;
	}
	public void setEstimatedDateTime(String estimatedDateTime) {
		this.estimatedDateTime = estimatedDateTime;
	}
	public String getOrderAddress() {
		return orderAddress;
	}
	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}
	public String getOrderAddressLatitude() {
		return orderAddressLatitude;
	}
	public void setOrderAddressLatitude(String orderAddressLatitude) {
		this.orderAddressLatitude = orderAddressLatitude;
	}
	public String getOrderAddressLongitude() {
		return orderAddressLongitude;
	}
	public void setOrderAddressLongitude(String orderAddressLongitude) {
		this.orderAddressLongitude = orderAddressLongitude;
	}
	public double getOrderTotalPrice() {
		return orderTotalPrice;
	}
	public void setOrderTotalPrice(double orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}
	public double getOrderFoodPrice() {
		return orderFoodPrice;
	}
	public void setOrderFoodPrice(double orderFoodPrice) {
		this.orderFoodPrice = orderFoodPrice;
	}
	public double getOrderDeliveryPrice() {
		return orderDeliveryPrice;
	}
	public void setOrderDeliveryPrice(double orderDeliveryPrice) {
		this.orderDeliveryPrice = orderDeliveryPrice;
	}

	public double getOrderDistance() {
		return orderDistance;
	}
	public void setOrderDistance(double orderDistance) {
		this.orderDistance = orderDistance;
	}
	public void setOrderDistance(float orderDistance) {
		this.orderDistance = orderDistance;
	}
	public String getConfirmCode() {
		return confirmCode;
	}
	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderCreatedDateTime() {
		return orderCreatedDateTime;
	}
	public void setOrderCreatedDateTime(String orderCreatedDateTime) {
		this.orderCreatedDateTime = orderCreatedDateTime;
	}

	public List<MerchantOrder> getMerchantOrderList() {
		return merchantOrderList;
	}
	public void setMerchantOrderList(List<MerchantOrder> merchantOrderList) {
		this.merchantOrderList = merchantOrderList;
	}
	public int getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(int estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	public String getOrderPaymentType() {
		return orderPaymentType;
	}

	public void setOrderPaymentType(String orderPaymentType) {
		this.orderPaymentType = orderPaymentType;
	}






}
