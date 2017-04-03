package com.fooddelivery.json.model.placeorder;

import java.util.List;

public class allOrder {
	String orderAddress;
	String orderAddressLatitude;
	String orderAddressLongitude;
	double orderPrice;
	double orderDeliveryPrice;
	List<merchant> merchant;
	double orderFoodPrice;
	
	public String getOrderAddress() {
		return orderAddress;
	}
	
	public String getOrderAddressLatitude() {
		return orderAddressLatitude;
	}
	
	public String getOrderAddressLongitude() {
		return orderAddressLongitude;
	}
	
	public double getOrderPrice() {
		return orderPrice;
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
}
