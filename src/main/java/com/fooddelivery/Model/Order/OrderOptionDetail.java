package com.fooddelivery.Model.Order;

public class OrderOptionDetail {
	private String orderDetailId ;
	private String optionName;
	private Double optionPrice;
	
	public String getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public Double getOptionPrice() {
		return optionPrice;
	}
	public void setOptionPrice(Double optionPrice) {
		this.optionPrice = optionPrice;
	}
	
	
}
