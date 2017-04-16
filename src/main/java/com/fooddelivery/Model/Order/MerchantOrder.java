package com.fooddelivery.Model.Order;



public class MerchantOrder {
	private String merid;
	private String merLatitude;
	private String merLongitude;
	private float merDistance;
	private float merFoodPrice;
	private float merDeliveryPrice;
	private SubOrder[] subOrderList;
	
	public String getMerid() {
		return merid;
	}
	public void setMerid(String merid) {
		this.merid = merid;
	}
	public String getMerLatitude() {
		return merLatitude;
	}
	public void setMerLatitude(String merLatitude) {
		this.merLatitude = merLatitude;
	}
	public String getMerLongitude() {
		return merLongitude;
	}
	public void setMerLongitude(String merLongitude) {
		this.merLongitude = merLongitude;
	}
	public float getMerDistance() {
		return merDistance;
	}
	public void setMerDistance(float merDistance) {
		this.merDistance = merDistance;
	}
	public float getMerFoodPrice() {
		return merFoodPrice;
	}
	public void setMerFoodPrice(float merFoodPrice) {
		this.merFoodPrice = merFoodPrice;
	}
	public float getMerDeliveryPrice() {
		return merDeliveryPrice;
	}
	public void setMerDeliveryPrice(float merDeliveryPrice) {
		this.merDeliveryPrice = merDeliveryPrice;
	}
	public SubOrder[] getSubOrderList() {
		return subOrderList;
	}
	public void setSubOrderList(SubOrder[] subOrderList) {
		this.subOrderList = subOrderList;
	}
	
	

}
