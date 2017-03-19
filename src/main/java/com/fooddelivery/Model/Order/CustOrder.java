package com.fooddelivery.Model.Order;


public class CustOrder {
	private String orderAddress;
	private String orderAddressLat;
	private String orderAddressLong;
	private String orderPrice;
	private String orderDeliveryPrice;
	private MerchantOrder[] merchantOrderArr;
	
	
	public String getOrderAddress() {
		return orderAddress;
	}
	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}
	public String getOrderAddressLat() {
		return orderAddressLat;
	}
	public void setOrderAddressLat(String orderAddressLat) {
		this.orderAddressLat = orderAddressLat;
	}
	public String getOrderAddressLong() {
		return orderAddressLong;
	}
	public void setOrderAddressLong(String orderAddressLong) {
		this.orderAddressLong = orderAddressLong;
	}
	public String getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getOrderDeliveryPrice() {
		return orderDeliveryPrice;
	}
	public void setOrderDeliveryPrice(String orderDeliveryPrice) {
		this.orderDeliveryPrice = orderDeliveryPrice;
	}
	public MerchantOrder[] getMerchantOrderArr() {
		return merchantOrderArr;
	}
	public void setMerchantOrderArr(MerchantOrder[] merchantOrderArr) {
		this.merchantOrderArr = merchantOrderArr;
	}
	
	
	
}
