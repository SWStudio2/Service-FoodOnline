package com.fooddelivery.json.model.placeorder;

public class order {
	long menuid;
	int orderDetailAmount;
	String remark;
	int[] optionid;
	
	public long getMenuId() {
		return menuid;
	}
	
	public int getOrderDetailAmount() {
		return orderDetailAmount;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public int[] getOptionId() {
		return optionid;
	}
}
