package com.fooddelivery.Model.Order;

public class Order {
	private String custId;
	private CustOrder custOrder;
	
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public CustOrder getCustOrder() {
		return custOrder;
	}
	public void setCustOrder(CustOrder custOrder) {
		this.custOrder = custOrder;
	}
	
	
}
