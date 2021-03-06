package com.fooddelivery.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	private Double order_total_price;

	@NotNull
	@Column(name = "order_distance")
	private double order_distance;

	@NotNull
	@Column(name = "order_status")
	private int order_status;

	@NotNull
	@Column(name = "order_food_price")
	private Double order_food_price;

	@NotNull
	@Column(name = "order_delivery_price")
	private Double order_delivery_price;

	@Column(name = "order_confirm_code")
	private String order_confirm_code;

	@Column(name = "order_estimate_time")
	private int order_estimate_time;

	@Column(name = "order_payment_type")
	private String order_payment_type;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "order_estimate_datetime")
	private Date order_estimate_datetime;

	//--------------RELATION----------------
	@OneToMany(mappedBy="order_id",targetEntity=OrderDetail.class,fetch=FetchType.LAZY) //EAGER
	private List<OrderDetail> orderDetails;

	@OneToMany//(mappedBy="order_id",targetEntity=SequenceOrders.class,fetch=FetchType.LAZY) //EAGER
	@JoinColumn(name="seqor_order_id", referencedColumnName="order_id", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	private List<SequenceOrders> sequenceOrders;

	@ManyToOne
	@JoinColumn(name = "order_cus_id", referencedColumnName="cus_id", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	private Customer customer;


	@ManyToOne
	@JoinColumn(name = "order_status", referencedColumnName="status_id", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	private StatusConfig statusConfig;


	public Orders() {}

	public Orders(long orderId) {
		this.order_id = orderId;
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
			Date orderEstimateDatetime,
			String orderPaymentType
	) {
		this.order_cus_id = orderCusId;
		this.order_address = orderAddress;
		this.order_address_latitude = orderAddressLatitude;
		this.order_address_longtitude = orderAddressLogtitude;
		this.order_created_datetime = orderCreatedDatetime;
		this.order_receive_datetime = orderReceiveDateTime;
		this.order_delivery_rate = orderDeliveryRate;
		this.order_total_price = orderPrice;
		this.order_distance = orderDistance;
		this.order_estimate_time = orderEstimateTime;
		this.order_estimate_datetime = orderEstimateDatetime;
		this.order_payment_type = orderPaymentType;
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
	/*
        public Date getOrderCreatedDatetime() {
            return order_created_datetime;
        }
    */
	public void setOrderReceiveDatetime(Date orderReceiveDatetime) {
		this.order_receive_datetime = orderReceiveDatetime;
	}

	public Date getOrderReceiveDatetime() {
		return order_receive_datetime;
	}

	public Date getOrderCreatedDatetime() {
		return order_created_datetime;
	}


	public Integer getOrderEstimateTime() {
		return  order_estimate_time;
	}

	public void setOrderEstimateTime(Integer order_estimate_time) {
		this.order_estimate_time = order_estimate_time;
	}

	public Date getOrderEstimateDatetime() {
		return order_estimate_datetime ;
	}

	public void setOrderEstimateDatetime(Date order_estimate_datetime) {
		this.order_estimate_datetime = order_estimate_datetime;
	}

	public void setOrderDeliveryRate(int orderDeliveryRate) {
		this.order_delivery_rate = orderDeliveryRate;
	}

	public int getOrderDeliveryRate() {
		return order_delivery_rate;
	}

	public void setOrderTotalPrice(Double orderTotalPrice) {
		this.order_total_price = orderTotalPrice;
	}

	public Double getOrderTotalPrice() {
		return order_total_price;
	}

	public void setOrderDistance(double orderDistance) {
		this.order_distance = orderDistance;
	}

	public double getOrderDistance() {
		return order_distance;
	}

	public void setOrderStatus(int orderStatus) {
		this.order_status = orderStatus;
	}

	public int getOrderStatus() {
		return order_status;
	}

	public void setOrderFoodPrice(Double orderFoodPrice) {
		this.order_food_price = orderFoodPrice;
	}

	public Double getOrderFoodPrice() {
		return order_food_price;
	}

	public void setOrderDeliveryPrice(Double orderDeliveryPrice) {
		this.order_delivery_price = orderDeliveryPrice;
	}

	public Double getOrderDeliveryPrice() {
		return order_delivery_price;
	}

	public void setOrderConfirmCode(String orderConfirmCode) {
		this.order_confirm_code = orderConfirmCode;
	}

	public String getOrderConfirmCode() {
		return order_confirm_code;
	}


	public String getOrderPaymentType() {
		return order_payment_type;
	}

	public void setOrderPaymentType(String orderPaymentType) {
		this.order_payment_type = orderPaymentType;
	}


	//-------------RELATION--------------
	public List<OrderDetail> getOrderDetails(){
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails){
		this.orderDetails = orderDetails;
	}

	public List<SequenceOrders> getSequenceOrders() {
		return sequenceOrders;
	}

	public void setSequenceOrders(List<SequenceOrders> sequenceOrders) {
		this.sequenceOrders = sequenceOrders;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public StatusConfig getStatusConfig() {
		return statusConfig;
	}

	public void setStatusConfig(StatusConfig statusConfig) {
		this.statusConfig = statusConfig;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "ERROR";
		}

	}
}
