package com.fooddelivery.Model.Order;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OrderHeaderAndDetail {
	private long order_id;
	
	private long order_cus_id;
	
	private String order_address;
	
	private String order_address_latitude;
	
	private String order_address_longtitude;
	
	private Date order_created_datetime;
	
	private Date order_receive_datetime;
	
	private Integer order_delivery_rate;
	
	private Double order_total_price;
	
	private Double order_distance;

	private Integer order_status;
	
	private Double order_food_price;
	
	private Double order_delivery_price;
	
	private String order_confirm_code;
	
	private Integer order_estimate_time;
	
	private Date order_estimate_datetime;
	
	private Integer menu_id;
	
	private String menu_name;
	
	private Float menu_price;
	
	private Integer order_detail_amount;
	
	private String order_remark;
	
	private Double option_total_price;
	
	private Double menu_total_price;
	
	private List<MerchantOrder> merchantOrderList;
	
	private String payment_type;
	

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public Integer getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}

	public List<MerchantOrder> getMerchantOrderList() {
		return merchantOrderList;
	}

	public void setMerchantOrderList(List<MerchantOrder> merchantOrderList) {
		this.merchantOrderList = merchantOrderList;
	}

	public long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(long order_id) {
		this.order_id = order_id;
	}

	public long getOrder_cus_id() {
		return order_cus_id;
	}

	public void setOrder_cus_id(long order_cus_id) {
		this.order_cus_id = order_cus_id;
	}

	public String getOrder_address() {
		return order_address;
	}

	public void setOrder_address(String order_address) {
		this.order_address = order_address;
	}

	public String getOrder_address_latitude() {
		return order_address_latitude;
	}

	public void setOrder_address_latitude(String order_address_latitude) {
		this.order_address_latitude = order_address_latitude;
	}

	public String getOrder_address_longtitude() {
		return order_address_longtitude;
	}

	public void setOrder_address_longtitude(String order_address_longtitude) {
		this.order_address_longtitude = order_address_longtitude;
	}

	public Date getOrder_created_datetime() {
		return order_created_datetime;
	}

	public void setOrder_created_datetime(Date datetime) {
		this.order_created_datetime = datetime;
	}

	public Date getOrder_receive_datetime() {
		return order_receive_datetime;
	}

	public void setOrder_receive_datetime(Date datetime) {
		this.order_receive_datetime = datetime;
	}

	public Integer getOrder_delivery_rate() {
		return order_delivery_rate;
	}

	public void setOrder_delivery_rate(Integer order_delivery_rate) {
		this.order_delivery_rate = order_delivery_rate;
	}

	public Double getOrder_total_price() {
		return order_total_price;
	}

	public void setOrder_total_price(Double order_total_price) {
		this.order_total_price = order_total_price;
	}

	public Double getOrder_distance() {
		return order_distance;
	}

	public void setOrder_distance(Double order_distance) {
		this.order_distance = order_distance;
	}

	

	public Integer getOrder_status() {
		return order_status;
	}

	public void setOrder_status(Integer order_status) {
		this.order_status = order_status;
	}

	public Double getOrder_food_price() {
		return order_food_price;
	}

	public void setOrder_food_price(Double order_food_price) {
		this.order_food_price = order_food_price;
	}

	public Double getOrder_delivery_price() {
		return order_delivery_price;
	}

	public void setOrder_delivery_price(Double order_delivery_price) {
		this.order_delivery_price = order_delivery_price;
	}

	public String getOrder_confirm_code() {
		return order_confirm_code;
	}

	public void setOrder_confirm_code(String order_confirm_code) {
		this.order_confirm_code = order_confirm_code;
	}

	public Integer getOrder_estimate_time() {
		return order_estimate_time;
	}

	public void setOrder_estimate_time(Integer order_estimate_time) {
		this.order_estimate_time = order_estimate_time;
	}

	public Date getOrder_estimate_datetime() {
		return order_estimate_datetime;
	}

	public void setOrder_estimate_datetime(Date order_estimate_datetime) {
		this.order_estimate_datetime = order_estimate_datetime;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public Float getMenu_price() {
		return menu_price;
	}

	public void setMenu_price(Float menu_price) {
		this.menu_price = menu_price;
	}

	public Integer getOrder_detail_amount() {
		return order_detail_amount;
	}

	public void setOrder_detail_amount(Integer order_detail_amount) {
		this.order_detail_amount = order_detail_amount;
	}

	public String getOrder_remark() {
		return order_remark;
	}

	public void setOrder_remark(String order_remark) {
		this.order_remark = order_remark;
	}

	public Double getOption_total_price() {
		return option_total_price;
	}

	public void setOption_total_price(Double option_total_price) {
		this.option_total_price = option_total_price;
	}

	public Double getMenu_total_price() {
		return menu_total_price;
	}

	public void setMenu_total_price(Double menu_total_price) {
		this.menu_total_price = menu_total_price;
	}

	

	
	
	
}
