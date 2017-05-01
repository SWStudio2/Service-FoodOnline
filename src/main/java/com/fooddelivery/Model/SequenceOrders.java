package com.fooddelivery.Model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.persistence.criteria.Order;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "sequence_orders")
public class SequenceOrders {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "seqor_id")
	private long seqor_id;

	@NotNull
	@Column(name = "seqor_mer_id")
	private long seqor_mer_id;

	@Column(name = "seqor_sort")
	private String seqor_sort;

	//@NotNull
	@Column(name = "seqor_receive_status")
	private int seqor_receive_status;

	@Column(name = "seqor_mess_id")
	private long seqor_mess_id;

	@Column(name = "seqor_type_mess")
	private String seqor_type_mess;

	@NotNull
	@Column(name = "seqor_order_id")
	private long seqor_order_id;

	@NotNull
	@Column(name = "seqor_cook_status")
	private int seqor_cook_status;

	@Column(name = "seqor_cook_time")
	private int seqor_cook_time;

	@Column(name = "seqor_mer_distance")
	private double seqor_mer_distance;

	@Column(name = "seqor_confirm_code")
	private String seqor_confirm_code;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "seqor_receive_datetime")
	private Date seqor_receive_datetime;

	//--------------RELATION----------------
	/*
	@OneToOne
	@JoinColumn(name="seqor_mess_id", insertable = false, updatable = false)
	@NotFound(action=NotFoundAction.IGNORE)
	private FullTimeMessenger fullTimeMessenger;*/
/*
	@OneToOne
	@JoinColumn(name="seqor_order_id", insertable = false, updatable = false)
	private Orders orders;
*/	//--------------RELATION----------------
	@ManyToOne
	@JoinColumn(name="seqor_mer_id", referencedColumnName="mer_id", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	private Merchants merchants;


	@ManyToOne
	@JoinColumn(name = "seqor_receive_status", referencedColumnName="status_id", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	private StatusConfig statusConfigReceive;

	@ManyToOne
	@JoinColumn(name = "seqor_cook_status", referencedColumnName="status_id", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	private StatusConfig statusConfigCook;

	@Transient
	public List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();

	public SequenceOrders() {}

	public SequenceOrders(long sequenceOrderId) {
		this.seqor_id = sequenceOrderId;
	}

	public SequenceOrders(
			long sequenceOderMerchantId,
			String sequenceSort,
			int sequenceReceiveStatus,
			long sequenceMessengerId,
			String sequenceTypeMessenger,
			long sequenceOrderId,
			int sequenceCookStatus
	) {
		this.seqor_mer_id = sequenceOderMerchantId;
		this.seqor_sort = sequenceSort;
		this.seqor_receive_status = sequenceReceiveStatus;
		this.seqor_mess_id = sequenceMessengerId;
		this.seqor_type_mess = sequenceTypeMessenger;
		this.seqor_order_id = sequenceOrderId;
		this.seqor_cook_status = sequenceCookStatus;
	}

	public void setSequenceId(long sequenceOrderId) {
		this.seqor_id = sequenceOrderId;
	}

	public long getSequenceId() {
		return seqor_id;
	}

	public void setSequenceOrderMerchantId(long sequenceOderMerchantId) {
		this.seqor_mer_id = sequenceOderMerchantId;
	}

	public long getSequenceOrderMerchantId() {
		return seqor_mer_id;
	}

	public void setSequenceSort(String sequenceSort) {
		this.seqor_sort = sequenceSort;
	}

	public String getSequenceSort() {
		return seqor_sort;
	}

	public void setSequenceReceiveStatus(int sequenceReceiveStatus) {
		this.seqor_receive_status = sequenceReceiveStatus;
	}

	public int getSequenceReceiveStatus() {
		return seqor_receive_status;
	}

	public void setSequenceMessengerId(long sequenceMessengerId) {
		this.seqor_mess_id = sequenceMessengerId;
	}

	public long getSequenceMessengerId() {
		return seqor_mess_id;
	}

	public void setSequenceTypeMessenger(String sequenceTypeMessenger) {
		this.seqor_type_mess = sequenceTypeMessenger;
	}

	public String getSequenceTypeMessenger() {
		return seqor_type_mess;
	}

	public void setSequenceOrderId(long sequenceOrderId) {
		this.seqor_order_id = sequenceOrderId;
	}

	public long getSequenceOrderId() {
		return seqor_order_id;
	}

	public void setSequenceCookStatus(int sequenceCookStatus) {
		this.seqor_cook_status = sequenceCookStatus;
	}

	public int getSequenceCookStatus() {
		return seqor_cook_status;
	}

	public void setSequenceCookTime(int sequenceCookTime) {
		this.seqor_cook_time = sequenceCookTime;
	}

	public int getSequenceCookTime() {
		return seqor_cook_time;
	}

	public void setSequenceMerDistance(double sequenceMerDistance) {
		this.seqor_mer_distance = sequenceMerDistance;
	}

	public double getSequenceMerDistance() {
		return seqor_mer_distance;
	}

	public void setSequenceConfirmCode(String sequenceConfirmCode) {
		this.seqor_confirm_code = sequenceConfirmCode;
	}

	public String getSequenceConfirmCode() {
		return seqor_confirm_code;
	}

	public void setSequenceReceiveDatetime(Date sequenceReceiveDatetime) {
		this.seqor_receive_datetime = sequenceReceiveDatetime;
	}

	public Date getSequenceReceiveDatetime() {
		return seqor_receive_datetime;
	}


	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	//--------------RELATION----------------
/*	public FullTimeMessenger getFullTimeMessenger() {
		return fullTimeMessenger;
	}
*/
	/*public List<Orders> getOrders() {
		return orders;
	}*/

	public StatusConfig getStatusConfigReceive() {
		return statusConfigReceive;
	}

	public void setStatusConfigReceive(StatusConfig statusConfigReceive) {
		this.statusConfigReceive = statusConfigReceive;
	}

	public StatusConfig getStatusConfigCook() {
		return statusConfigCook;
	}

	public void setStatusConfigCook(StatusConfig statusConfigCook) {
		this.statusConfigCook = statusConfigCook;
	}

	public Merchants getMerchants() {
		return merchants;
	}

	public void setMerchants(Merchants merchants) {
		this.merchants = merchants;
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
