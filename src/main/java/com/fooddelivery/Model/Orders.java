package com.fooddelivery.Model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "orders")
public class Orders {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "order_id")
	private long order_id;
	
	@NotNull
	@Column(name = "order_cus_id")
	private long order_cus_id;
	
	@NotNull
	@Column(name = "order_address")
	private String order_address;
	
	@NotNull
	@Column(name = "order_address_latitude")
	private String order_address_latitude;
	
	@NotNull
	@Column(name = "order_address_longtitude")
	private String order_address_longtitude;
	
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "order_created_datetime")
	private Date order_created_datetime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "order_receive_datetime")
	private Date order_receive_datetime;
	
	@NotNull
	@Column(name = "order_delivery_rate")
	private int order_delivery_rate;
	
	@NotNull
	@Column(name = "order_total_price")
	private double order_total_price;
	
	@NotNull
	@Column(name = "order_distance")
	private double order_distance;

	@NotNull
	@Column(name = "order_status")
	private String order_status;
	
	@NotNull
	@Column(name = "order_food_price")
	private double order_food_price;
	
	@NotNull
	@Column(name = "order_delivery_price")
	private double order_delivery_price;
	
	@Column(name = "order_confirm_code")
	private String order_confirm_code;
	
	@Column(name = "order_estimate_time")
	private int order_estimate_time;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "order_estimate_datetime")
	private Date order_estimate_datetime;
		
	public Orders() {}
	
	public Orders(long orderId) {
		this.order_id = orderId;
	}
	
	public Orders(
		long orderCusId,
		String orderAddress,
		String orderAddressLatitude,
		String orderAddressLogtitude,
		Date orderDatetime,
		Date orderDatetimeDelivery,
		int orderDeliveryRate,
		double orderPrice,
		double orderDistance
		
	) {
		this.order_cus_id = orderCusId;
		this.order_address = orderAddress;
		this.order_address_latitude = orderAddressLatitude;
		this.order_address_longtitude = orderAddressLogtitude;
		this.order_created_datetime = orderDatetime;
		this.order_receive_datetime = orderDatetimeDelivery;
		this.order_delivery_rate = orderDeliveryRate;
		this.order_total_price = orderPrice;
		this.order_distance = orderDistance;
	}
	
	public void setOrderId(long orderId) {
		this.order_id = orderId;
	}
	
	public long getOrderId() {
		return order_id;
	}
	
	public void setOrderCusId(long orderCusId) {
		this.order_cus_id = orderCusId;
	}
	
	public long getOrderCusId() {
		return order_cus_id;
	}
	
	public void setOrderAddress(String orderAddress) {
		this.order_address = orderAddress;
	}
	
	public String getOrderAddress() {
		return order_address;
	}
	
	public void setOrderAddressLatitude(String orderAddressLatitude) {
		this.order_address_latitude = orderAddressLatitude;
	}
	
	public String getOrderAddressLatitude() {
		return order_address_latitude;
	}
	
	public void setOrderAddressLongtitude(String orderAddressLongtitude) {
		this.order_address_longtitude = orderAddressLongtitude;
	}
	
	public String getOrderAddressLongtitude() {
		return order_address_longtitude;
	}
	
	public void setOrderCreatedDatetime(Date orderCreatedDatetime) {
		this.order_created_datetime = orderCreatedDatetime;
	}
	
	public Date getOderCreatedDatetime() {
		return order_created_datetime;
	}
	
	public void setOrderReceiveDatetime(Date orderReceiveDatetime) {
		this.order_receive_datetime = orderReceiveDatetime;
	}
	
	public Date getOrderReceiveDatetime() {
		return order_receive_datetime;
	}
	
	public void setOrderDeliveryRate(int orderDeliveryRate) {
		this.order_delivery_rate = orderDeliveryRate;
	} 
	
	public int getOrderDeliveryRate() {
		return order_delivery_rate;
	}
	
	public void setOrderTotalPrice(double orderTotalPrice) {
		this.order_total_price = orderTotalPrice;
	}
	
	public double getOrderTotalPrice() {
		return order_total_price;
	}
	
	public void setOrderDistance(double orderDistance) {
		this.order_distance = orderDistance;
	}
	
	public double getOrderDistance() {
		return order_distance;
	}
	
	public void setOrderStatus(String orderStatus) {
		this.order_status = orderStatus;
	}
	
	public String getOrderStatus() {
		return order_status;
	}
	
	public void setOrderFoodPrice(double orderFoodPrice) {
		this.order_food_price = orderFoodPrice;
	}
	
	public double getOrderFoodPrice() {
		return order_food_price;
	}
	
	public void setOrderDeliveryPrice(double orderDeliveryPrice) {
		this.order_delivery_price = orderDeliveryPrice;
	}
	
	public double getOrderDeliveryPrice() {
		return order_delivery_price;
	}
	
	public void setOrderConfirmCode(String orderConfirmCode) {
		this.order_confirm_code = orderConfirmCode;
	}
	
	public String getOrderConfirmCode() {
		return order_confirm_code;
	}
	
	public void setOrderEstimateTime(int orderEstimateTime) {
		this.order_estimate_time = orderEstimateTime;
	} 
	
	public int getOrderEstimateTime() {
		return order_estimate_time;
	}
	
	public void setOrderEstimateDatetime(Date orderEstimateDatetime) {
		this.order_estimate_datetime = orderEstimateDatetime;
	}
	
	public Date getOrderEstimateDatetime() {
		return order_estimate_datetime;
	}
}
