package com.fooddelivery.util;

import java.util.ArrayList;

import com.fooddelivery.Model.FullTimeMessenger;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.controller.HomeController;
import com.fooddelivery.service.durationpath.TwoMessThreeMercService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeDetail {
	private static final Logger logger = LoggerFactory.getLogger(NodeDetail.class);
	private FullTimeMessenger fullMess;
	private ArrayList<Merchants> merList;
	private String latitudeDelivery;
	private String longtitudeDelivery;



	public FullTimeMessenger getFullMess() {
		return fullMess;
	}
	public void setFullMess(FullTimeMessenger fullMess) {
		this.fullMess = fullMess;
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
	
	public static NodeDetail getBestNodeDetail(ArrayList<NodeDetail> arrNode)
	{
		NodeDetail tmpNode;
		HomeController home = new HomeController();
		int bestTime = 9999;
		NodeDetail bestNode = null;
		int sumAll = 0;
		for(int i = 0;i<arrNode.size();i++)
		{
			int firstNodeTime = 0;
			sumAll = 0;
			tmpNode = (NodeDetail)arrNode.get(i);
			Merchants firstMerchant = tmpNode.merList.get(0);
			String[] arrDetail = (String[])home.getDistanceDuration(tmpNode.fullMess.getFullLatestLattitude(), tmpNode.fullMess.getFullLatestLongtitude(), firstMerchant.getMerLatitude(), firstMerchant.getMerLongtitude());
			firstNodeTime = Integer.parseInt(arrDetail[1]);
			
			int sumMerChantTime = 0;
			if(tmpNode.merList.size() > 1)
			{
				for(int j = 1;j<tmpNode.merList.size();j++)
				{
					Merchants merStart = tmpNode.merList.get(j-1);
					Merchants merEnd = tmpNode.merList.get(j);
					arrDetail = (String[])home.getDistanceDuration(merStart.getMerLatitude(), merStart.getMerLongtitude(), merEnd.getMerLatitude(), merEnd.getMerLongtitude());
					
					sumMerChantTime = Integer.parseInt(arrDetail[1]) + sumMerChantTime;
				}								
			}
			Merchants endMerchant = tmpNode.merList.get(tmpNode.merList.size()-1);
			int merchantToCusTime = 0;
			arrDetail = (String[])home.getDistanceDuration(endMerchant.getMerLatitude(), endMerchant.getMerLongtitude(), tmpNode.latitudeDelivery, tmpNode.longtitudeDelivery);
			merchantToCusTime = Integer.parseInt(arrDetail[1]);
			

			sumAll = firstNodeTime + sumMerChantTime + merchantToCusTime;
			if(bestTime > sumAll)
			{
				bestNode = tmpNode;
				bestTime = sumAll;
			}
		}
		logger.info("bestTime " + bestTime);

		return bestNode;
	}
}
