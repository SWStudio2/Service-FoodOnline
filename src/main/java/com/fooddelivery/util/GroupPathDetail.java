package com.fooddelivery.util;

import java.util.ArrayList;

public class GroupPathDetail {
	private ArrayList<RoutePathDetail> allRoutePath = new ArrayList<RoutePathDetail>();
	private String totalDuration;
	private String totalDistance;

	public ArrayList<RoutePathDetail> getAllRoutePath() {
		return allRoutePath;
	}
	
	public void addRoutePathDetail(RoutePathDetail route){
		allRoutePath.add(route);
	}

	public String getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(String totalDuration) {
		this.totalDuration = totalDuration;
	}

	public String getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(String totalDistance) {
		this.totalDistance = totalDistance;
	}
	
}
