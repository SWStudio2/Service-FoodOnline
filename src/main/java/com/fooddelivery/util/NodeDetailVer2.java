package com.fooddelivery.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fooddelivery.Controller.HomeController;
import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.FullTimeMessenger;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.TimeAndDistanceDetail;

public class NodeDetailVer2 implements Comparator<NodeDetailVer2>{
	private int station;
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
	public int getStation() {
		return station;
	}
	public void setStation(int station) {
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
	
	public static NodeDetailVer2 getBestNodeDetail(ArrayList<NodeDetailVer2> arrNode,TimeAndDistanceDetail[] tmpTimeDisDetail)
	{
		NodeDetailVer2 tmpNode;
		HomeController home = new HomeController();
		TimeAndDistanceDetail tmpTimeAndDistance = new TimeAndDistanceDetail();
		int bestTime = 9999;
		NodeDetailVer2 bestNode = null;
		int sumAll = 0;
		for(int i = 0;i<arrNode.size();i++)
		{
			double sumDistance = 0;
			double sumDuration = 0;
			tmpNode = (NodeDetailVer2)arrNode.get(i);
			Merchants firstMerchant = tmpNode.merList.get(0);
			String[] arrDetail = tmpTimeAndDistance.getTimeAndDuration(tmpTimeDisDetail,tmpNode.station , firstMerchant.getMerID(), "bike");
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
					arrDetail = (String[])tmpTimeAndDistance.getTimeAndDuration(tmpTimeDisDetail, merStart.getMerID(), merEnd.getMerID(), "merchant");
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
		
		Collections.sort(arrNode, new NodeDetailVer2());
		ArrayList<NodeDetailVer2> arrTopByDuration = new ArrayList<NodeDetailVer2>();
		for(int i = 0;i<5;i++)
		{
			NodeDetailVer2 tmpNodeTop = (NodeDetailVer2)arrNode.get(i);
			arrTopByDuration.add(tmpNodeTop);
		}
		bestNode = (NodeDetailVer2)arrTopByDuration.get(0);
		for(int i = 1;i<arrTopByDuration.size();i++)
		{
			NodeDetailVer2 tmpNodeTop = (NodeDetailVer2)arrTopByDuration.get(i);
			if(Double.parseDouble(bestNode.distance) > Double.parseDouble(tmpNodeTop.distance))
			{
				bestNode = tmpNodeTop;
			}
		}
		return bestNode;
	}
	@Override
	public int compare(NodeDetailVer2 o1, NodeDetailVer2 o2) {
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
