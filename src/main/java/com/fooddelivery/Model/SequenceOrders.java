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
	
	@Column(name = "seqor_receive_status")
	private String seqor_receive_status;
	
	@Column(name = "seqor_mess_id")
	private long seqor_mess_id;
	
	@Column(name = "seqor_type_mess")
	private String seqor_type_mess;
	
	@NotNull
	@Column(name = "seqor_order_id")
	private long seqor_order_id;
	
	@NotNull
	@Column(name = "seqor_cook_status")
	private String seqor_cook_status;
	
	@Column(name = "seqor_cook_time")
	private int seqor_cook_time;
	
	@Column(name = "seqor_mer_distance")
	private double seqor_mer_distance;
		
	public SequenceOrders() {}
	
	public SequenceOrders(long sequenceOrderId) {
		this.seqor_id = sequenceOrderId;
	}
	
	public SequenceOrders(
		long sequenceOderMerchantId,
		String sequenceSort,
		String sequenceReceiveStatus,
		long sequenceMessengerId,
		String sequenceTypeMessenger,
		long sequenceOrderId,
		String sequenceCookStatus
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
	
	public void setSequenceReceiveStatus(String sequenceReceiveStatus) {
		this.seqor_receive_status = sequenceReceiveStatus;
	}
	
	public String getSequenceReceiveStatus() {
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
	
	public void setSequenceCookStatus(String sequenceCookStatus) {
		this.seqor_cook_status = sequenceCookStatus;
	}
	
	public String getSequenceCookStatus() {
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
	
}
