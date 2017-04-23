package com.fooddelivery.Model.Order;

import java.util.List;

public class SubOrder {
	private Integer menuId;
	private String menuName;
	private Integer orderDetailAmount;
	private Integer orderDetailId;
	private Float menuPrice;
	private Double menuTotalPrice;
	private String remark;
	private List<OrderOptionDetail> optionList;
	
	
	public Double getMenuTotalPrice() {
		return menuTotalPrice;
	}
	public void setMenuTotalPrice(Double menuTotalPrice) {
		this.menuTotalPrice = menuTotalPrice;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public Integer getOrderDetailAmount() {
		return orderDetailAmount;
	}
	public void setOrderDetailAmount(Integer orderDetailAmount) {
		this.orderDetailAmount = orderDetailAmount;
	}
	
	public Float getMenuPrice() {
		return menuPrice;
	}
	public void setMenuPrice(Float menuPrice) {
		this.menuPrice = menuPrice;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<OrderOptionDetail> getOptionList() {
		return optionList;
	}
	public void setOptionList(List<OrderOptionDetail> optionList) {
		this.optionList = optionList;
	}
	public Integer getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	
	
	
	
}
