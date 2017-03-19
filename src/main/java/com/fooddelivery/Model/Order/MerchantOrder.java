package com.fooddelivery.Model.Order;



public class MerchantOrder {
	private String merid;
	private SubOrder[] subOrderList;
	
	public String getMerid() {
		return merid;
	}
	public void setMerid(String merid) {
		this.merid = merid;
	}
	public SubOrder[] getSubOrderList() {
		return subOrderList;
	}
	public void setSubOrderList(SubOrder[] subOrderList) {
		this.subOrderList = subOrderList;
	}

}
