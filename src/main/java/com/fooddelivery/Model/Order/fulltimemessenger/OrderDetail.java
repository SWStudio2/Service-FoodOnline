package com.fooddelivery.Model.Order.fulltimemessenger;

public class OrderDetail {
	
	private long orderDetailId;
	private long orderId;
	private int orderDetailAmount;
	private String orderRemark;
	private long menuId;
	private long merId;
	private Merchants merchant;
		
	public OrderDetail() {}
	
	public OrderDetail(long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	
	public OrderDetail(
		long orderId,
		int orderDetailAmount,
		String orderRemark,
		long menuId,
		long merId
		
	) {
		this.orderId = orderId;
		this.orderDetailAmount = orderDetailAmount;
		this.orderRemark = orderRemark;
		this.menuId = menuId;
		this.merId = merId;
	}
	
	public void setOrderDetailId(long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	
	public long getOrderDetailId() {
		return orderDetailId;
	}
	
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	
	public long getOrderId() {
		return orderId;
	}
	
	public void setOrderDetailAmount(int orderDetailAmount) {
		this.orderDetailAmount = orderDetailAmount;
	}
	
	public int getOrderDetailAmount() {
		return orderDetailAmount;
	}
	
	public void setOrderRemark(String orderReamrk) {
		this.orderRemark = orderReamrk;
	}
	
	public String getOrderRemark() {
		return orderRemark;
	}
	
	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}
	
	public long getMenuId() {
		return menuId;
	}
	
	public void setMerId(long merId) {
		this.merId = merId;
	}
	
	public long getMerId() {
		return merId;
	}
	//-----------MAP MODEL--------------
	public void mapping(com.fooddelivery.Model.OrderDetail orderDetail) {
		this.orderDetailId = orderDetail.getOrderDetailId();
		this.orderId = orderDetail.getOrderId();
		this.orderDetailAmount = orderDetail.getOrderDetailAmount();
		this.orderRemark = orderDetail.getOrderRemark();
		this.menuId = orderDetail.getMenuId();
		this.merId = orderDetail.getMerId();
	}
	
	public void setMerchant(Merchants merchant) {
		this.merchant = merchant;
	}
	
	public Merchants getMerchant() {
		return merchant;
	}
}
