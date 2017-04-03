package com.fooddelivery.Model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "order_detail")
public class OrderDetail {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "order_detail_id")
	private long order_detail_id;
	
	@NotNull
	@Column(name = "order_id")
	private long order_id;
	
	@NotNull
	@Column(name = "order_detail_amount")
	private int order_detail_amount;
	
	@Column(name = "order_remark")
	private String order_remark;
	
	@NotNull
	@Column(name = "menu_id")
	private long menu_id;
	
	@NotNull
	@Column(name = "mer_id")
	private long mer_id;
		
	public OrderDetail() {}
	
	public OrderDetail(long orderDetailId) {
		this.order_detail_id = orderDetailId;
	}
	
	public OrderDetail(
		long orderId,
		int orderDetailAmount,
		String orderRemark,
		long menuId,
		long merId
		
	) {
		this.order_id = orderId;
		this.order_detail_amount = orderDetailAmount;
		this.order_remark = orderRemark;
		this.menu_id = menuId;
		this.mer_id = merId;
	}
	
	public void setOrderDetailId(long orderDetailId) {
		this.order_detail_id = orderDetailId;
	}
	
	public long getOrderDetailId() {
		return order_detail_id;
	}
	
	public void setOrderId(long orderId) {
		this.order_id = orderId;
	}
	
	public long getOrderId() {
		return order_id;
	}
	
	public void setOrderDetailAmount(int orderDetailAmount) {
		this.order_detail_amount = orderDetailAmount;
	}
	
	public int getOrderDetailAmount() {
		return order_detail_amount;
	}
	
	public void setOrderRemark(String orderReamrk) {
		this.order_remark = orderReamrk;
	}
	
	public String getOrderRemark() {
		return order_remark;
	}
	
	public void setMenuId(long menuId) {
		this.menu_id = menuId;
	}
	
	public long getMenuId() {
		return menu_id;
	}
	
	public void setMerId(long merId) {
		this.mer_id = merId;
	}
	
	public long getMerId() {
		return mer_id;
	}
	
}
