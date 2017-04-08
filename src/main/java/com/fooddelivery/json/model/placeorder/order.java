package com.fooddelivery.json.model.placeorder;

import java.util.List;

public class order {
	long menuId;
	int orderDetailAmount;
	String remark;
	List<option> option;
	double menuPrice;
	
	public long getMenuId() {
		return menuId;
	}
	
	public int getOrderDetailAmount() {
		return orderDetailAmount;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public List<option> getOption() {
		return option;
	}
	
	public double getMenuPrice() {
		return menuPrice;
	}
}
