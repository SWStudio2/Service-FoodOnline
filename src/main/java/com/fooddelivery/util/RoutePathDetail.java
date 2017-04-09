package com.fooddelivery.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.fooddelivery.controller.HomeController;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.Station;
import com.fooddelivery.Model.TimeAndDistanceDetail;

public class RoutePathDetail implements Comparator<RoutePathDetail>{
	private Station station;
	ArrayList<Merchants> merList;
	private String latitudeDelivery;
	private String longtitudeDelivery;
	private String duration;
	private String distance;
	

	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public Station getStation() {
		return station;
	}
	public void setStation(Station station) {
		this.station = station;
	}
	public ArrayList<Merchants> getMerList() {
		return merList;
	}
	public void setMerList(ArrayList<Merchants> merList) {
		this.merList = merList;
	}
	public String getLatitudeDelivery() {
		return latitudeDelivery;
	}
	public void setLatitudeDelivery(String latitudeDelivery) {
		this.latitudeDelivery = latitudeDelivery;
	}
	public String getLongtitudeDelivery() {
		return longtitudeDelivery;
	}
	public void setLongtitudeDelivery(String longtitudeDelivery) {
		this.longtitudeDelivery = longtitudeDelivery;
	}
	
	public static RoutePathDetail getBestNodeDetail(ArrayList<RoutePathDetail> arrNode) throws InterruptedException
	{
		RoutePathDetail tmpNode;
		HomeController home = new HomeController();
		final double durationAgree = 5;
		int bestTime = 9999;
		RoutePathDetail bestNode = null;
		int sumAll = 0;
		for(int i = 0;i<arrNode.size();i++)
		{
			double sumDistance = 0;
			double sumDuration = 0;
			tmpNode = (RoutePathDetail)arrNode.get(i);
			Merchants firstMerchant = tmpNode.merList.get(0);
			String[] arrDetail = home.getDistanceDuration(tmpNode.station.getStationLantitude(), tmpNode.station.getStationLongtitude(), firstMerchant.getMerLatitude(), firstMerchant.getMerLongtitude());
			BigDecimal tmpDuration = new BigDecimal(arrDetail[0]);
			BigDecimal tmpDistance = new BigDecimal(arrDetail[1]);
			if(firstMerchant.getCookingTime() < tmpDuration.doubleValue())
			{
				sumDuration = tmpDuration.doubleValue() + sumDuration;
			}
			else
			{
				sumDuration = firstMerchant.getCookingTime() + sumDuration;
			}
			sumDistance = tmpDistance.doubleValue() + sumDistance;
			if(tmpNode.merList.size() > 1)
			{
				for(int j = 1;j<tmpNode.merList.size();j++)
				{
					Merchants merStart = tmpNode.merList.get(j-1);
					Merchants merEnd = tmpNode.merList.get(j);
					
					arrDetail = (String[])home.getDistanceDuration(merStart.getMerLatitude(), merStart.getMerLongtitude(), merEnd.getMerLatitude(), merEnd.getMerLongtitude());
					Thread.sleep(500);
					tmpDuration = new BigDecimal(arrDetail[0]);
					tmpDistance = new BigDecimal(arrDetail[1]);

					sumDuration = tmpDuration.doubleValue() + sumDuration;				
					if(sumDuration < merEnd.getCookingTime())
					{
						sumDuration = merEnd.getCookingTime();
					}
					
					sumDistance = tmpDistance.doubleValue() + sumDistance;
				}								
			}
			Merchants endMerchant = tmpNode.merList.get(tmpNode.merList.size()-1);
			
			arrDetail = (String[])home.getDistanceDuration(endMerchant.getMerLatitude(), endMerchant.getMerLongtitude(), tmpNode.latitudeDelivery, tmpNode.longtitudeDelivery);
			sumDuration = sumDuration + Double.parseDouble(arrDetail[1]);
			sumDistance = tmpDistance.doubleValue() + sumDistance;
			
			tmpNode.distance = ""+sumDistance;
			tmpNode.duration = ""+sumDuration;

		}
		
		Collections.sort(arrNode, new RoutePathDetail());
		ArrayList<RoutePathDetail> arrTopByDuration = new ArrayList<RoutePathDetail>();
		RoutePathDetail bestNodeByDuration = arrNode.get(0);
		double durationBest = Double.parseDouble(bestNodeByDuration.getDuration());
		double rangeDuration = durationAgree + durationBest;
		for(int i = 0;i<arrNode.size();i++)
		{
			RoutePathDetail tmpPath = (RoutePathDetail)arrNode.get(i);
			if(Double.parseDouble(tmpPath.duration) < rangeDuration)
			{
				arrTopByDuration.add(tmpPath);
			}
			
		}
		bestNode = (RoutePathDetail)arrTopByDuration.get(0);
		for(int i = 1;i<arrTopByDuration.size();i++)
		{
			RoutePathDetail tmpNodeTop = (RoutePathDetail)arrTopByDuration.get(i);
			if(Double.parseDouble(bestNode.distance) > Double.parseDouble(tmpNodeTop.distance))
			{
				bestNode = tmpNodeTop;
			}
		}
		return bestNode;
	}
	@Override
	public int compare(RoutePathDetail o1, RoutePathDetail o2) {
		// TODO Auto-generated method stub
		if(Double.parseDouble(o1.duration) < Double.parseDouble(o2.duration))
		{
			return -1;
		}
		else if(Double.parseDouble(o1.duration) > Double.parseDouble(o2.duration))
		{
			return 1;
		}
		return 0;
	}
}
