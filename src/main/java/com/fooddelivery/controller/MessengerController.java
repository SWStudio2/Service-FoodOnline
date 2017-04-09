package com.fooddelivery.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.fooddelivery.Model.UtilsQuery;
import com.fooddelivery.service.durationpath.OneMessengerOneMerchantService;
import com.fooddelivery.service.durationpath.TwoMessThreeMercService;
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

	
	@RequestMapping(value="/service/getestimatedtime", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<Response<Map<String, Object>>> getEstimatedTime(
	   @RequestBody Map<String, Object> mapRequest) {
	  
	  String cus_Latitude = (String) mapRequest.get("cusLatitude");
	  String cus_Longtitude = (String) mapRequest.get("cusLongitude");
	  List<Integer> list = new ArrayList<Integer>();
	  list = (List<Integer>) mapRequest.get("merchantList");
	  int[] merIDList = new int[list.size()];
	  for(int i = 0;i<list.size();i++)
	  {
		  merIDList[i] = list.get(i);
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
		RoutePathDetail routePathOneMessThreeService = new RoutePathDetail();
		
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
				routePathOneMessThreeService = this.searchFuncOneMessenger(list, cus_Latitude, cus_Longtitude);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(list.size() == 3)
		{
			try {
				//Mike
				routePathOneMessThreeService = this.searchFuncOneMessenger(list, cus_Latitude, cus_Longtitude);
				//YUI
				TwoMessThreeMercService twoMessService = new TwoMessThreeMercService();
				bestTimeTwoMessTwoService = twoMessService.TwoMessThreeMercService(list, cus_Latitude, cus_Longtitude);
				//MINT
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
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		double oneMessValue = 99d;
		double twoMessValue = 99d;
		double threeMessValue = 99d;		
		double chooseTime = 0;
		double diffValue = 0;
		
		if (routePathOneMessThreeService.getDuration() != null && !routePathOneMessThreeService.getDuration().equals("")){
			oneMessValue = Double.parseDouble(routePathOneMessThreeService.getDuration());
		}

		if (!bestTimeTwoMessTwoService.equals("")){
			twoMessValue = Double.parseDouble(bestTimeTwoMessTwoService);
		}
		
		if (!bestTimeOneMessOneService.equals("")){
			threeMessValue = Double.parseDouble(bestTimeOneMessOneService);
		}
		
		chooseTime = oneMessValue;

		diffValue = chooseTime - twoMessValue;
		if(diffValue > 10)
		{
			chooseTime = twoMessValue;
			diffValue = twoMessValue - threeMessValue;
			if(diffValue > 10)
			{
				chooseTime = threeMessValue;
			}
		}
			
	  
	  // Return response
	  String estimatedTime = "";
	  BigDecimal valueAdjust = new BigDecimal(chooseTime);
	  valueAdjust = valueAdjust.setScale(2, RoundingMode.HALF_UP);
	  Map<String, Object> dataMap = new HashMap<String, Object>();
	  estimatedTime = valueAdjust.toString();
	  dataMap.put("estimatedTime",estimatedTime);

	  return ResponseEntity.ok(new Response<Map<String, Object>>(HttpStatus.OK.value(),"Estimated time successfully", dataMap));

	 }	
	
	public RoutePathDetail searchFuncOneMessenger(List<Integer> merId,String cus_Latitude,String cus_Longtitude) throws InterruptedException {
		
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
				tmpNodeDetail.setLongtitudeDelivery(cus_Longtitude);
				
				arrNode.add(tmpNodeDetail);
			}
		}
		
		RoutePathDetail bestNode = new RoutePathDetail();
		bestNode = bestNode.getBestNodeDetail(arrNode);
		for(int i = 0;i<bestNode.getMerList().size();i++)
		{
			Merchants mer = bestNode.getMerList().get(i);
		}
		return bestNode;
	}
	
	//Service P'Boat
	@RequestMapping(value="/service/updateSequenceRoutePath/{orderId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	
	 public ResponseEntity<Response<String>> updateSequenceRoutePath(
			 @PathVariable("orderId") int orderId) {

	  UtilsQuery query = new UtilsQuery();
	  ArrayList<HashMap<String, Object>> seqOrderAndMerchant = new ArrayList<HashMap<String,Object>>();
	  // Return response
	  String estimatedTime = ""+orderId;
	  String isRecall = "N";
	  isRecall = query.checkRecallOrder(orderId);
	  HashMap<String, String> lantAndLong = new HashMap<String, String>();
	  if(isRecall.equals("Y"))
	  {
		  
		  seqOrderAndMerchant = query.getMerchantAndOrderSeqByOrderId(orderId);

		  lantAndLong = query.getLatitudeAndLongtitudeByOrderId(orderId);

	  }
	  return ResponseEntity.ok(new Response<String>(HttpStatus.OK.value(),"updateSequenceRoutePath successfully", estimatedTime));

	 }	
}


