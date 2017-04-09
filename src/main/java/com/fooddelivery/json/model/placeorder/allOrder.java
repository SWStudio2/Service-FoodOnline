package com.fooddelivery.json.model.placeorder;

import java.util.List;

public class allOrder {
	String orderAddress;
	String orderAddressLatitude;
	String orderAddressLongitude;
	double orderTotalPrice;
	double orderFoodPrice;
	double orderDeliveryPrice;
	double orderDistance;
	int orderEstimateTime;
	String orderEstimateDateTime;
	List<merchant> merchant;
	
	public String getOrderAddress() {
		return orderAddress;
	}
	
	public String getOrderAddressLatitude() {
		return orderAddressLatitude;
	}
	
	public String getOrderAddressLongitude() {
		return orderAddressLongitude;
	}
	
	public double getOrderTotalPrice() {
		return orderTotalPrice;
	}
	
	public double getOrderDeliveryPrice() {
		return orderDeliveryPrice;
	}
	
	public List<merchant> getMerchant() {
		return merchant;
	}
	
	public double getOrderFoodPrice() {
		return orderFoodPrice;
	}
	
	public double getOrderDistance() {
		return orderDistance;
	}
	
	public int getOrderEstimateTime() {
		return orderEstimateTime;
	}
	
	public String getOrderEstimateDateTime() {
		return orderEstimateDateTime;
	}
}
