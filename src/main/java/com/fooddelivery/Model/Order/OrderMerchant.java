package com.fooddelivery.Model.Order;

public class OrderMerchant {
	private String merName;
	private String merLat;
	private String merLng;
	private Double seqMerDistance;
	private Double deliveryPrice;
	private Double foodPrice;
	
	public String getMerName() {
		return merName;
	}
	public void setMerName(String merName) {
		this.merName = merName;
	}
	public String getMerLat() {
		return merLat;
	}
	public void setMerLat(String merLat) {
		this.merLat = merLat;
	}
	public String getMerLng() {
		return merLng;
	}
	public void setMerLng(String merLng) {
		this.merLng = merLng;
	}
	
	public Double getSeqMerDistance() {
		return seqMerDistance;
	}
	public void setSeqMerDistance(Double seqMerDistance) {
		this.seqMerDistance = seqMerDistance;
	}
	public Double getDeliveryPrice() {
		return deliveryPrice;
	}
	public void setDeliveryPrice(Double deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}
	public Double getFoodPrice() {
		return foodPrice;
	}
	public void setFoodPrice(Double foodPrice) {
		this.foodPrice = foodPrice;
	}
	
	
}
