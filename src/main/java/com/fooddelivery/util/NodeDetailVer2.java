package com.fooddelivery.util;

import java.util.ArrayList;

import com.fooddelivery.Controller.HomeController;
import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.FullTimeMessenger;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.TimeAndDistanceDetail;

public class NodeDetailVer2 {
	private String station;
	private ArrayList<Merchants> merList;
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
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
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
	
//	public static NodeDetailVer2 getBestNodeDetail(ArrayList<NodeDetailVer2> arrNode,TimeAndDistanceDetail[] tmpTimeDisDetail)
//	{
//		NodeDetailVer2 tmpNode;
//		HomeController home = new HomeController();
//		int bestTime = 9999;
//		NodeDetailVer2 bestNode = null;
//		int sumAll = 0;
//		for(int i = 0;i<arrNode.size();i++)
//		{
//			int firstNodeTime = 0;
//			sumAll = 0;
//			tmpNode = (NodeDetailVer2)arrNode.get(i);
//			Merchants firstMerchant = tmpNode.merList.get(0);
//			String[] 
//			firstNodeTime = Integer.parseInt(arrDetail[1]);
//			
//			int sumMerChantTime = 0;
//			if(tmpNode.merList.size() > 1)
//			{
//				for(int j = 1;j<tmpNode.merList.size();j++)
//				{
//					Merchants merStart = tmpNode.merList.get(j-1);
//					Merchants merEnd = tmpNode.merList.get(j);
//					arrDetail = (String[])home.getDistanceDuration(merStart.getMerLatitude(), merStart.getMerLongtitude(), merEnd.getMerLatitude(), merEnd.getMerLongtitude());
//					
//					sumMerChantTime = Integer.parseInt(arrDetail[1]) + sumMerChantTime;
//				}								
//			}
//			Merchants endMerchant = tmpNode.merList.get(tmpNode.merList.size()-1);
//			int merchantToCusTime = 0;
//			arrDetail = (String[])home.getDistanceDuration(endMerchant.getMerLatitude(), endMerchant.getMerLongtitude(), tmpNode.latitudeDelivery, tmpNode.longtitudeDelivery);
//			merchantToCusTime = Integer.parseInt(arrDetail[1]);
//			
//
//			sumAll = firstNodeTime + sumMerChantTime + merchantToCusTime;
//			if(bestTime > sumAll)
//			{
//				bestNode = tmpNode;
//				bestTime = sumAll;
//			}
//		}
//		System.out.println("bestTime " + bestTime);
//
//		return bestNode;
//	}
}
