package com.fooddelivery.util;

import java.util.ArrayList;

public class GroupPathDetail implements Comparable{
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
	
//	public int compareDistTo(GroupPathDetail compareGP) {
//
//		int compareTTDist = Integer.valueOf(compareGP.getTotalDistance());
//
//		//ascending order
//		return Integer.valueOf(this.totalDistance) - compareTTDist;
//
//		//descending order
//		//return compareQuantity - this.quantity;
//
//	}

	@Override
	public int compareTo(Object o) {
		
		if(o instanceof GroupPathDetail){
			GroupPathDetail oGP = (GroupPathDetail) o;
			int compareTTDura = Integer.valueOf(oGP.getTotalDuration());

			//ascending order
			return Integer.valueOf(this.totalDuration) - compareTTDura;
		}else{
			return 2;
		}

		//descending order
		//return compareQuantity - this.quantity;
	}

}
