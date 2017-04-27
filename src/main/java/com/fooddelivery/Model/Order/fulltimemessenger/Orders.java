package com.fooddelivery.Model.Order.fulltimemessenger;

import java.util.Date;
import java.util.List;

public class Orders {

	private long orderId;
	private long orderCusId;
	private String orderAddress;
	private String orderAddressLatitude;
	private String orderAddressLongtitude;
	private Date orderCreatedDatetime;
	private Date orderReceiveDatetime;
	private int orderDeliveryRate;
	private Double orderTotalPrice;
	private double orderDistance;
	private int orderStatus;
	private Double orderFoodPrice;
	private Double orderDeliveryPrice;
	private String orderConfirmCode;
	private int orderEstimateTime;
	private Date orderEstimateDatetime;
	
	//--------------RELATION----------------
	private List<OrderDetail> orderDetails;
	private Customer customers;
	private List<SequenceOrders> sequenceOrders;
	
	public Orders() {}
	
	public Orders(long orderId) {
		this.orderId = orderId;
	}
	
	public Orders(
		long orderCusId,
		String orderAddress,
		String orderAddressLatitude,
		String orderAddressLogtitude,
		Date orderCreatedDatetime,
		Date orderReceiveDateTime,
		int orderDeliveryRate,
		double orderPrice,
		double orderDistance,
		int orderEstimateTime,
		Date orderEstimateDatetime
	) {
		this.orderCusId = orderCusId;
		this.orderAddress = orderAddress;
		this.orderAddressLatitude = orderAddressLatitude;
		this.orderAddressLongtitude = orderAddressLogtitude;
		this.orderCreatedDatetime = orderCreatedDatetime;
		this.orderReceiveDatetime = orderReceiveDateTime;
		this.orderDeliveryRate = orderDeliveryRate;
		this.orderTotalPrice = orderPrice;
		this.orderDistance = orderDistance;
		this.orderEstimateTime = orderEstimateTime;
		this.orderEstimateDatetime = orderEstimateDatetime;
	}
	
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	
	public long getOrderId() {
		return orderId;
	}
	
	public void setOrderCusId(long orderCusId) {
		this.orderCusId = orderCusId;
	}
	
	public long getOrderCusId() {
		return orderCusId;
	}
	
	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}
	
	public String getOrderAddress() {
		return orderAddress;
	}
	
	public void setOrderAddressLatitude(String orderAddressLatitude) {
		this.orderAddressLatitude = orderAddressLatitude;
	}
	
	public String getOrderAddressLatitude() {
		return orderAddressLatitude;
	}
	
	public void setOrderAddressLongtitude(String orderAddressLongtitude) {
		this.orderAddressLongtitude = orderAddressLongtitude;
	}
	
	public String getOrderAddressLongtitude() {
		return orderAddressLongtitude;
	}
	
	public void setOrderCreatedDatetime(Date orderCreatedDatetime) {
		this.orderCreatedDatetime = orderCreatedDatetime;
	}
	
	public Date getOderCreatedDatetime() {
		return orderCreatedDatetime;
	}
	
	public void setOrderReceiveDatetime(Date orderReceiveDatetime) {
		this.orderReceiveDatetime = orderReceiveDatetime;
	}
	
	public Date getOrderReceiveDatetime() {
		return orderReceiveDatetime;
	}
	
	public Date getOrderCreatedDatetime() {
		return orderCreatedDatetime;
	}


	public Integer getOrderEstimateTime() {
		return orderEstimateTime;
	}

	public void setOrderEstimateTime(Integer order_estimate_time) {
		this.orderEstimateTime = order_estimate_time;
	}

	public Date getOrderEstimateDatetime() {
		return orderEstimateDatetime;
	}

	public void setOrderEstimateDatetime(Date order_estimate_datetime) {
		this.orderEstimateDatetime = order_estimate_datetime;
	}

	public void setOrderDeliveryRate(int orderDeliveryRate) {
		this.orderDeliveryRate = orderDeliveryRate;
	} 
	
	public int getOrderDeliveryRate() {
		return orderDeliveryRate;
	}
	
	public void setOrderTotalPrice(Double orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}
	
	public Double getOrderTotalPrice() {
		return orderTotalPrice;
	}
	
	public void setOrderDistance(double orderDistance) {
		this.orderDistance = orderDistance;
	}
	
	public double getOrderDistance() {
		return orderDistance;
	}
	
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public int getOrderStatus() {
		return orderStatus;
	}
	
	public void setOrderFoodPrice(Double orderFoodPrice) {
		this.orderFoodPrice = orderFoodPrice;
	}
	
	public Double getOrderFoodPrice() {
		return orderFoodPrice;
	}
	
	public void setOrderDeliveryPrice(Double orderDeliveryPrice) {
		this.orderDeliveryPrice = orderDeliveryPrice;
	}
	
	public Double getOrderDeliveryPrice() {
		return orderDeliveryPrice;
	}
	
	public void setOrderConfirmCode(String orderConfirmCode) {
		this.orderConfirmCode = orderConfirmCode;
	}
	
	public String getOrderConfirmCode() {
		return orderConfirmCode;
	}
	
	//-------------RELATION--------------
	public List<OrderDetail> getOrderDetails(){
		return orderDetails;
	}
	
	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	public Customer getCustomer() {
		return customers;
	}
	
	public void setCustomer(Customer customer) {
		this.customers = customer;
	}
	
	public List<SequenceOrders> getSequenceOrders() {
		return sequenceOrders;
	}
	
	public void setSequenceOrders(List<SequenceOrders> sequenceOrders) {
		this.sequenceOrders = sequenceOrders;
	}
	
	//-------------MAP MODEL-------------
	public void mapping(com.fooddelivery.Model.Orders order) {
		this.orderId = order.getOrderId();
		this.orderCusId = order.getOrderCusId();
		this.orderAddress = order.getOrderAddress();
		this.orderAddressLatitude = order.getOrderAddressLatitude();
		this.orderAddressLongtitude = order.getOrderAddressLongtitude();
		this.orderCreatedDatetime = order.getOderCreatedDatetime();
		this.orderReceiveDatetime = order.getOrderReceiveDatetime();
		this.orderDeliveryRate = order.getOrderDeliveryRate();
		this.orderTotalPrice = order.getOrderTotalPrice();
		this.orderDistance = order.getOrderDistance();
		this.orderStatus = order.getOrderStatus();
		this.orderFoodPrice = order.getOrderFoodPrice();
		this.orderDeliveryPrice = order.getOrderDeliveryPrice();
		this.orderConfirmCode = order.getOrderConfirmCode();
		this.orderEstimateTime = order.getOrderEstimateTime();
		this.orderEstimateDatetime = order.getOrderEstimateDatetime();
	}
}
