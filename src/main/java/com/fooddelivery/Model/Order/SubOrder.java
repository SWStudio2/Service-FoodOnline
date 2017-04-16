package com.fooddelivery.Model.Order;

public class SubOrder {
	private String menuId;
	private String orderDetailAmount;
	private String menuPrice;
	private String remark;
	private SubOrderOption[] optionList;
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getOrderDetailAmount() {
		return orderDetailAmount;
	}
	public void setOrderDetailAmount(String orderDetailAmount) {
		this.orderDetailAmount = orderDetailAmount;
	}
	public String getMenuPrice() {
		return menuPrice;
	}
	public void setMenuPrice(String menuPrice) {
		this.menuPrice = menuPrice;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public SubOrderOption[] getOptionList() {
		return optionList;
	}
	public void setOptionList(SubOrderOption[] optionList) {
		this.optionList = optionList;
	}
	
	
	
}
