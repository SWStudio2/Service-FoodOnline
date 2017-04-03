package com.fooddelivery.json.model.placeorder;

import java.util.List;

public class merchant {
	long merId;
	String merLatitude;
	String merLongitude;
	List<order> order;
	String merDistance;
	
	public long getMerId() {
		return merId;
	}
	
	public String getMerLatitude() {
		return merLatitude;
	}
	
	public String getMerLongitude() {
		return merLongitude;
	}
	
	public List<order> getOrder() {
		return order;
	}
	
	public String getMerDistance() {
		return merDistance;
	}
}
