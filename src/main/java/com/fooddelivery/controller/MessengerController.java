package com.fooddelivery.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.Model.BikeStation;
import com.fooddelivery.Model.BikeStationDao;
import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.FullTimeMessenger;
import com.fooddelivery.Model.FullTimeMessengerDao;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsQuery;
import com.fooddelivery.Model.Station;
import com.fooddelivery.Model.StationQuery;
import com.fooddelivery.Model.TimeAndDistanceDetail;
import com.fooddelivery.Model.TimeAndDistanceDetailDao;
import com.fooddelivery.Model.User;
import com.fooddelivery.service.durationpath.OneMessengerOneMerchantService;
import com.fooddelivery.util.NodeDetail;
import com.fooddelivery.util.NodeDetailVer2;
import com.fooddelivery.util.Response;
import com.fooddelivery.util.RoutePathDetail;
import com.fooddelivery.util.Utils;
import com.google.api.client.json.Json;

@RestController
public class MessengerController {

	private FullTimeMessengerDao fullMessDao;
	@Autowired
	private BikeStationDao bikeDao;
	@RequestMapping(value="/fullTimeAvailable" , method=RequestMethod.GET)
	@ResponseBody
	public String getFullTimeMessengerAvailable(int[] merId,String cus_Latitude,String cus_Longtitude) {
		
		String merIdAdjust = "";
		for(int i = 0;i<merId.length;i++)
		{
			if (i != 0) {
				merIdAdjust += ",";
			}
			merIdAdjust += merId[i];
		}
		MerchantsQuery merDao = new MerchantsQuery();
		Merchants[] merList = merDao.queryMerChantByID(merIdAdjust);
		
		List<BikeStation> listStation = bikeDao.getBikeStationAvailable();
		
		List<Merchants> listMerchant = new ArrayList<Merchants>();
		for(int i = 0;i<merList.length;i++)
		{
			listMerchant.add(merList[i]);
		}

		String bestTimeOneMessOneService = "";
		String bestTimeTwoMessTwoService = "";
		String bestTimeOneMessThreeService = "";

		if(merId.length == 1)
		{
			OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService(merId, cus_Latitude, cus_Longtitude);
			List<NodeDetailVer2> bestNode = (List<NodeDetailVer2>)oneMess.oneMessengerForOneMerchants(listStation, listMerchant);
		}
		else if(merId.length == 2)
		{
			//YUI
			bestTimeOneMessThreeService = this.getFullTimeMessengerAvailable(merId, cus_Latitude, cus_Longtitude);
		}
		else if(merId.length == 3)
		{
			bestTimeOneMessThreeService = this.getFullTimeMessengerAvailable(merId, cus_Latitude, cus_Longtitude);
		}
		
		double chooseTime = Double.parseDouble(bestTimeOneMessThreeService);
		double diffValue = 0;
		if(!bestTimeTwoMessTwoService.equals(""))
		{
			diffValue = chooseTime - Double.parseDouble(bestTimeTwoMessTwoService);
			if(diffValue > 10)
			{
				chooseTime = Double.parseDouble(bestTimeTwoMessTwoService);
			}
		}	
		return "";
	}
	
	@RequestMapping(value="/service/getestimatedtime", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<Response<Map<String, Object>>> getEstimatedTime(
	   @RequestBody Map<String, Object> mapRequest) {
	  // Get request
	  System.out.println("cusLatitude "+mapRequest.get("cusLatitude"));
	  System.out.println("cusLongitude "+mapRequest.get("cusLongitude"));
	  System.out.println("Merchant "+mapRequest.get("merchantList"));
	  
	  String cus_Latitude = (String) mapRequest.get("cusLatitude");
	  String cus_Longtitude = (String) mapRequest.get("cusLongitude");
	  List<Integer> list = new ArrayList<Integer>();
	  list = (List<Integer>) mapRequest.get("merchantList");
	  int[] merIDList = new int[list.size()];
	  for(int i = 0;i<list.size();i++)
	  {
		  merIDList[i] = list.get(i);
	  }
	  System.out.println("count "+list.size());
	  for (int i = 0; i<list.size();i++){
	   System.out.println(i+": "+list.get(i));
	  }

		String merIdAdjust = "";
		for(int i = 0;i<list.size();i++)
		{
			if (i != 0) {
				merIdAdjust += ",";
			}
			merIdAdjust += list.get(i);
		}
		MerchantsQuery merDao = new MerchantsQuery();
		Merchants[] merList = merDao.queryMerChantByID(merIdAdjust);
		
		List<BikeStation> listStation = bikeDao.getBikeStationAvailable();
		
		List<Merchants> listMerchant = new ArrayList<Merchants>();
		for(int i = 0;i<merList.length;i++)
		{
			listMerchant.add(merList[i]);
		}

		String bestTimeOneMessOneService = "";
		String bestTimeTwoMessTwoService = "";
		String bestTimeOneMessThreeService = "";

		if(list.size() == 1)
		{
			OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService(merIDList, cus_Latitude, cus_Longtitude);
			List<NodeDetailVer2> listNode = (List<NodeDetailVer2>)oneMess.oneMessengerForOneMerchants(listStation, listMerchant);
			double time = 99;
			for(int i = 0;i<listNode.size();i++)
			{
				NodeDetailVer2 node = (NodeDetailVer2)listNode.get(i);
				if(time > Double.parseDouble(node.getDuration()))
				{
					time = Double.parseDouble(node.getDuration());
				}	
			}
			bestTimeOneMessOneService = "" + time;
		}
		else if(list.size() == 2)
		{
			//YUI
			try {
				bestTimeOneMessThreeService = this.searchFuncOneMessenger(list, cus_Latitude, cus_Longtitude);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(list.size() == 3)
		{
			try {
				bestTimeOneMessThreeService = this.searchFuncOneMessenger(list, cus_Latitude, cus_Longtitude);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		double chooseTime = Double.parseDouble(bestTimeOneMessThreeService);
		double diffValue = 0;
		if(!bestTimeTwoMessTwoService.equals(""))
		{
			diffValue = chooseTime - Double.parseDouble(bestTimeTwoMessTwoService);
			if(diffValue > 10)
			{
				chooseTime = Double.parseDouble(bestTimeTwoMessTwoService);
			}
		}
		
		if(!bestTimeOneMessOneService.equals(""))
		{
			diffValue = chooseTime - Double.parseDouble(bestTimeOneMessOneService);
			if(diffValue > 10)
			{
				chooseTime = Double.parseDouble(bestTimeOneMessOneService);
			}
		}		
	  
	  // Return response
	  String estimatedTime = "1.2";
	  estimatedTime = ""+chooseTime;
	  Map<String, Object> dataMap = new HashMap<String, Object>();

	  dataMap.put("estimatedTime",estimatedTime);

	  return ResponseEntity.ok(new Response<Map<String, Object>>(HttpStatus.OK.value(),"Estimated time successfully", dataMap));

	 }	
	
	public String searchFuncOneMessenger(List<Integer> merId,String cus_Latitude,String cus_Longtitude) throws InterruptedException {
		
		//Define id Merchant from interface

		TimeAndDistanceDetailDao timeDisDao = new TimeAndDistanceDetailDao();
		String merIdAdjust = "";
		for(int i = 0;i<merId.size();i++)
		{
			if (i != 0) {
				merIdAdjust += ",";
			}
			merIdAdjust += merId.get(i);
		}
		MerchantsQuery merDao = new MerchantsQuery();
		Merchants[] merList = merDao.queryMerChantByID(merIdAdjust);
		ArrayList<Integer> indexPos = new ArrayList<Integer>();
		for(int i = 0;i<merId.size();i++)
		{
			indexPos.add(i);
		}
		Object[] resultSet = (Object[])Utils.listPermutations(indexPos).toArray();
		
		ArrayList<String> arrResult = new ArrayList<String>();
		for(int i = 0;i<resultSet.length;i++)
		{
			String tmpValue = resultSet[i].toString();
			tmpValue = tmpValue.replace("[", "");
			tmpValue = tmpValue.replace("]", "");
			arrResult.add(tmpValue);
		}
		StationQuery stationQue = new StationQuery();
		Station[] staList = stationQue.getStationAvailable();
		
		ArrayList<RoutePathDetail> arrNode = new ArrayList<RoutePathDetail>();
		for(int i = 0;i<staList.length;i++)
		{

			for(int j = 0;j<arrResult.size();j++)
			{
				RoutePathDetail tmpNodeDetail = new RoutePathDetail();
				tmpNodeDetail.setStation(staList[i]);
				ArrayList<Merchants> arrMerchant = new ArrayList<Merchants>();
				String[] postList = arrResult.get(j).split("\\,");
				for(int k = 0;k<postList.length;k++)
				{
					int pos = Integer.parseInt(postList[k].trim());
					arrMerchant.add(merList[pos]);
				}
				
				tmpNodeDetail.setMerList(arrMerchant);
				tmpNodeDetail.setLatitudeDelivery(cus_Latitude);
				tmpNodeDetail.setLatitudeDelivery(cus_Longtitude);
				
				arrNode.add(tmpNodeDetail);
			}
		}
		
		RoutePathDetail bestNode = new RoutePathDetail();
		bestNode = bestNode.getBestNodeDetail(arrNode);
		for(int i = 0;i<bestNode.getMerList().size();i++)
		{
			Merchants mer = bestNode.getMerList().get(i);
		}
		return bestNode.getDuration();
	}
	
	public static int[] getFullTimeIdAvailable(TimeAndDistanceDetail[] tmp)
	{
		ArrayList<Integer> arrMessId = new ArrayList<Integer>();
		for(int i = 0;i<tmp.length;i++)
		{
			if(tmp[i].getPathType().equals("bike") && !arrMessId.contains(tmp[i].getSourceId()))
			{
				arrMessId.add(tmp[i].getSourceId());
			}
		}
		
		int[] idList = new int[arrMessId.size()];
		for(int i = 0;i<arrMessId.size();i++)
		{
			idList[i] = arrMessId.get(i);
		}
		return idList;
	}
	
}


