package com.fooddelivery.Model.Order.fulltimemessenger;

import java.util.Date;

public class SequenceOrders {
	
	private long seqorId;
	private long seqorMerId;
	private String seqorSort;
	private int seqorReceiveStatus;
	private long seqorMessId;
	private String seqorTypeMess;
	private long seqorOrderId;
	private int seqorCookStatus;
	private int seqorCookTime;
	private double seqorMerDistance;
	private String seqorConfirmCode;
	private Date seqorReceiveDatetime;
	
	//--------------RELATION----------------
	private FullTimeMessenger fullTimeMessenger;
		
	public SequenceOrders() {}
	
	public SequenceOrders(long sequenceOrderId) {
		this.seqorId = sequenceOrderId;
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
		this.seqorMerId = sequenceOderMerchantId;
		this.seqorSort = sequenceSort;
		this.seqorReceiveStatus = sequenceReceiveStatus;
		this.seqorMessId = sequenceMessengerId;
		this.seqorTypeMess = sequenceTypeMessenger;
		this.seqorOrderId = sequenceOrderId;
		this.seqorCookStatus = sequenceCookStatus;
	}
	
	public void setSequenceId(long sequenceOrderId) {
		this.seqorId = sequenceOrderId;
	}
	
	public long getSequenceId() {
		return seqorId;
	}
	
	public void setSequenceOrderMerchantId(long sequenceOderMerchantId) {
		this.seqorMerId = sequenceOderMerchantId;
	}
	
	public long getSequenceOrderMerchantId() {
		return seqorMerId;
	}
	
	public void setSequenceSort(String sequenceSort) {
		this.seqorSort = sequenceSort;
	}
	
	public String getSequenceSort() {
		return seqorSort;
	}
	
	public void setSequenceReceiveStatus(int sequenceReceiveStatus) {
		this.seqorReceiveStatus = sequenceReceiveStatus;
	}
	
	public int getSequenceReceiveStatus() {
		return seqorReceiveStatus;
	}
	
	public void setSequenceMessengerId(long sequenceMessengerId) {
		this.seqorMessId = sequenceMessengerId;
	}
	
	public long getSequenceMessengerId() {
		return seqorMessId;
	}
	
	public void setSequenceTypeMessenger(String sequenceTypeMessenger) {
		this.seqorTypeMess = sequenceTypeMessenger;
	}
	
	public String getSequenceTypeMessenger() {
		return seqorTypeMess;
	}
	
	public void setSequenceOrderId(long sequenceOrderId) {
		this.seqorOrderId = sequenceOrderId;
	}
	
	public long getSequenceOrderId() {
		return seqorOrderId;
	}
	
	public void setSequenceCookStatus(int sequenceCookStatus) {
		this.seqorCookStatus = sequenceCookStatus;
	}
	
	public int getSequenceCookStatus() {
		return seqorCookStatus;
	}
	
	public void setSequenceCookTime(int sequenceCookTime) {
		this.seqorCookTime = sequenceCookTime;
	}
	
	public int getSequenceCookTime() {
		return seqorCookTime;
	}
	
	public void setSequenceMerDistance(double sequenceMerDistance) {
		this.seqorMerDistance = sequenceMerDistance;
	}
	
	public double getSequenceMerDistance() {
		return seqorMerDistance;
	}
	
	public void setSequenceConfirmCode(String sequenceConfirmCode) {
		this.seqorConfirmCode = sequenceConfirmCode;
	}
	
	public String getSequenceConfirmCode() {
		return seqorConfirmCode;
	}
	
	public void setSequenceReceiveDatetime(Date sequenceReceiveDatetime) {
		this.seqorReceiveDatetime = sequenceReceiveDatetime;
	}
	
	public Date getSequenceReceiveDatetime() {
		return seqorReceiveDatetime;
	}
	
	//--------------RELATION----------------
	public FullTimeMessenger getFullTimeMessenger() {
		return fullTimeMessenger;
	}
	
	public void setFullTimeMessenger(FullTimeMessenger fullTimeMessenger) {
		this.fullTimeMessenger = fullTimeMessenger;
	}
	
	//------------MAP MODEL-------------------
	public void mapping(com.fooddelivery.Model.SequenceOrders sequenceOrders) {
		this.seqorId = sequenceOrders.getSequenceId();
		this.seqorMerId = sequenceOrders.getSequenceId();
		this.seqorSort = sequenceOrders.getSequenceSort();
		this.seqorReceiveStatus = sequenceOrders.getSequenceReceiveStatus();
		this.seqorMessId = sequenceOrders.getSequenceMessengerId();
		this.seqorTypeMess = sequenceOrders.getSequenceTypeMessenger();
		this.seqorOrderId = sequenceOrders.getSequenceOrderId();
		this.seqorCookStatus = sequenceOrders.getSequenceCookStatus();
		this.seqorCookTime = sequenceOrders.getSequenceCookTime();
		this.seqorMerDistance = sequenceOrders.getSequenceMerDistance();
		this.seqorConfirmCode = sequenceOrders.getSequenceConfirmCode();
		this.seqorReceiveDatetime = sequenceOrders.getSequenceReceiveDatetime();
	}
}
