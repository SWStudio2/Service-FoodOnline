package com.fooddelivery.Model;


import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
	@Column(name = "order_datetime")
	private Date order_datetime;
	
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "order_datetime_delivery")
	private Date order_datetime_delivery;
	
	@NotNull
	@Column(name = "order_delivery_rate")
	private int order_delivery_rate;
	
	@NotNull
	@Column(name = "order_price")
	private double order_price;
	
	@NotNull
	@Column(name = "order_distance")
	private double order_distance;
	
	@NotNull
	@Column(name = "order_status")
	private String order_status;
		
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
		this.order_datetime = orderDatetime;
		this.order_datetime_delivery = orderDatetimeDelivery;
		this.order_delivery_rate = orderDeliveryRate;
		this.order_price = orderPrice;
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
	
	public void setOrderDatetime(Date orderDatetime) {
		this.order_datetime = orderDatetime;
	}
	
	public Date getOderDatetime() {
		return order_datetime;
	}
	
	public void setOrderDatetimeDelivery(Date orderDatetimeDelivery) {
		this.order_datetime_delivery = orderDatetimeDelivery;
	}
	
	public Date getOrderDatetimeDelivery() {
		return order_datetime_delivery;
	}
	
	public void setOrderDeliveryRate(int orderDeliveryRate) {
		this.order_delivery_rate = orderDeliveryRate;
	} 
	
	public int getOrderDeliveryRate() {
		return order_delivery_rate;
	}
	
	public void setOrderPrice(double orderPrice) {
		this.order_price = orderPrice;
	}
	
	public double getOrderPrice() {
		return order_price;
	}
	
	public void setOrderDistance(double orderDistance) {
		this.order_distance = orderDistance;
	}
	
	public double getOrderDistance() {
		return order_distance;
	}
	
	public void setOrderStatud(String orderStatus) {
		this.order_status = orderStatus;
	}
	
	public String getOrderStatus() {
		return order_status;
	}
}
