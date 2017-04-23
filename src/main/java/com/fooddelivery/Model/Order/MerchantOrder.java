package com.fooddelivery.Model.Order;

import java.math.BigDecimal;
import java.util.List;

public class MerchantOrder {
	private Integer merid;
	private String merLatitude;
	private String merLongitude;
	private BigDecimal merDistance;
	private Double merFoodPrice;
	private BigDecimal merDeliveryPrice;
	private List<SubOrder> subOrderList;
	
	
	public Integer getMerid() {
		return merid;
	}
	public void setMerid(Integer merid) {
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
	
	
	public BigDecimal getMerDistance() {
		return merDistance;
	}
	public void setMerDistance(BigDecimal merDistance) {
		this.merDistance = merDistance;
	}
	public Double getMerFoodPrice() {
		return merFoodPrice;
	}
	public void setMerFoodPrice(Double merFoodPrice) {
		this.merFoodPrice = merFoodPrice;
	}
	
	public BigDecimal getMerDeliveryPrice() {
		return merDeliveryPrice;
	}
	public void setMerDeliveryPrice(BigDecimal merDeliveryPrice) {
		this.merDeliveryPrice = merDeliveryPrice;
	}
	public List<SubOrder> getSubOrderList() {
		return subOrderList;
	}
	public void setSubOrderList(List<SubOrder> subOrderList) {
		this.subOrderList = subOrderList;
	}

}
