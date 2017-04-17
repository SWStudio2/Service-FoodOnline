package com.fooddelivery.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


import com.fooddelivery.Model.BikeStation;
import com.fooddelivery.Model.BikeStationDao;
import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.FullTimeMessenger;
import com.fooddelivery.Model.FullTimeMessengerDao;
import com.fooddelivery.Model.FullTimeMessengerQuery;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsQuery;
import com.fooddelivery.Model.Station;
import com.fooddelivery.Model.StationQuery;
import com.fooddelivery.Model.TimeAndDistanceDetail;
import com.fooddelivery.Model.TimeAndDistanceDetailDao;
import com.fooddelivery.Model.User;
import com.fooddelivery.Model.UtilsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.fooddelivery.service.durationpath.OneMessengerOneMerchantService;
import com.fooddelivery.service.durationpath.TwoMessThreeMercService;
import com.fooddelivery.util.GroupPathDetail;
import com.fooddelivery.util.NodeDetail;
import com.fooddelivery.util.NodeDetailVer2;
import com.fooddelivery.util.Response;
import com.fooddelivery.util.RoutePathDetail;
import com.fooddelivery.util.Utils;
import com.google.api.client.json.Json;

@RestController
public class MessengerController {
	private static final Logger logger = LoggerFactory.getLogger(MessengerController.class);
	@Autowired
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
		GroupPathDetail bestTimeTwoMessTwoService = new GroupPathDetail();
		RoutePathDetail routePathOneMessThreeService = new RoutePathDetail();

		if(list.size() == 1)
		{
//			OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService(merIDList, cus_Latitude, cus_Longtitude);
//			List<NodeDetailVer2> listNode = (List<NodeDetailVer2>)oneMess.oneMessengerForOneMerchants(listStation, listMerchant);
//			double time = 99;
//			for(int i = 0;i<listNode.size();i++)
//			{
//				NodeDetailVer2 node = (NodeDetailVer2)listNode.get(i);
//				if(time > Double.parseDouble(node.getDuration()))
//				{
//					time = Double.parseDouble(node.getDuration());
//				}
//			}
//			bestTimeOneMessOneService = "" + time;
			try {
				routePathOneMessThreeService = this.searchFuncOneMessenger(list, cus_Latitude, cus_Longtitude);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		else if(list.size() == 2)
		{
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
				//bestTimeTwoMessTwoService = twoMessService.TwoMessThreeMercService(list, cus_Latitude, cus_Longtitude);
				//MINT
//				OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService(merIDList, cus_Latitude, cus_Longtitude);
//				List<NodeDetailVer2> listNode = (List<NodeDetailVer2>)oneMess.oneMessengerForOneMerchants(listStation, listMerchant);
//				double time = 99;
//				for(int i = 0;i<listNode.size();i++)
//				{
//					NodeDetailVer2 node = (NodeDetailVer2)listNode.get(i);
//					if(time > Double.parseDouble(node.getDuration()))
//					{
//						time = Double.parseDouble(node.getDuration());
//					}
//				}
//				bestTimeOneMessOneService = "" + time;
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

		if (bestTimeTwoMessTwoService.getTotalDuration() != null && !bestTimeTwoMessTwoService.equals("")){
			twoMessValue = Double.parseDouble(bestTimeTwoMessTwoService.getTotalDuration());
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

		
		if(list.size() == 1)
		{
			chooseTime = threeMessValue;
			chooseTime = oneMessValue;//Use this step temporary
		}
		else if(list.size() == 2)
		{
			chooseTime = oneMessValue;
			diffValue = chooseTime - threeMessValue;
			if(diffValue > 10)
			{
				chooseTime = threeMessValue;
			}
		}
		else if(list.size() == 3)
		{
			chooseTime = oneMessValue;
			diffValue = chooseTime - twoMessValue;
			if(diffValue > 10)
			{
				chooseTime = twoMessValue;
				diffValue = chooseTime - threeMessValue;
				if(diffValue > 10)
				{
					chooseTime = threeMessValue;
				}
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
		
		TimeAndDistanceDetailDao timeAndDistance = new TimeAndDistanceDetailDao();
		TimeAndDistanceDetail[] tmpTimeAndDistance = timeAndDistance.getTimeAndDistanceDetail(merIdAdjust);
		RoutePathDetail bestNode = new RoutePathDetail();
		bestNode = bestNode.getBestNodeDetail(arrNode,tmpTimeAndDistance);
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
	  String isRecall = "N";
	  isRecall = query.checkRecallOrder(orderId);
	  HashMap<String, String> lantAndLong = new HashMap<String, String>();
	  FullTimeMessengerQuery fullQuery = new FullTimeMessengerQuery();
	  if(isRecall.equals("Y"))
	  {

		  seqOrderAndMerchant = query.getMerchantAndOrderSeqByOrderId(orderId);
		  lantAndLong = query.getLatitudeAndLongtitudeByOrderId(orderId);

		  	List<Integer> list = new ArrayList<Integer>();
		  	for(int i = 0;i<seqOrderAndMerchant.size();i++)
		  	{
		  		HashMap<String, Object> tmpHashSeqOrder = seqOrderAndMerchant.get(i);
		  		list.add((Integer) tmpHashSeqOrder.get("SEQOR_MER_ID"));
		  	}

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

		  	String cus_Latitude = "";
		  	String cus_Longtitude = "";

		  	cus_Latitude = lantAndLong.get("ORDER_ADDRESS_LATITUDE");
		  	cus_Longtitude = lantAndLong.get("ORDER_ADDRESS_LONGTITUDE");

		  	String bestTimeOneMessOneServiceStr = "";
			GroupPathDetail bestTimeTwoMessTwoService = new GroupPathDetail();
			RoutePathDetail routePathOneMessThreeService = new RoutePathDetail();
			List<NodeDetailVer2> bestTimeOneMessOneService = new ArrayList<NodeDetailVer2>();

			if(list.size() == 1)
			{
				OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService(merIDList, cus_Latitude, cus_Longtitude);
				bestTimeOneMessOneService = (List<NodeDetailVer2>)oneMess.oneMessengerForOneMerchants(listStation, listMerchant);
				double time = 99;
				for(int i = 0;i<bestTimeOneMessOneService.size();i++)
				{
					NodeDetailVer2 node = (NodeDetailVer2)bestTimeOneMessOneService.get(i);
					if(time > Double.parseDouble(node.getDuration()))
					{
						time = Double.parseDouble(node.getDuration());
					}
				}
				bestTimeOneMessOneServiceStr = "" + time;
			}
			else if(list.size() == 2)
			{
				//1 mess to one merchant
				OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService(merIDList, cus_Latitude, cus_Longtitude);
				bestTimeOneMessOneService = (List<NodeDetailVer2>)oneMess.oneMessengerForOneMerchants(listStation, listMerchant);
				double time = 99;
				for(int i = 0;i<bestTimeOneMessOneService.size();i++)
				{
					NodeDetailVer2 node = (NodeDetailVer2)bestTimeOneMessOneService.get(i);
					if(time > Double.parseDouble(node.getDuration()))
					{
						time = Double.parseDouble(node.getDuration());
					}
				}
				bestTimeOneMessOneServiceStr = "" + time;

				//1 mess to many merchant
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
					bestTimeTwoMessTwoService = twoMessService.twoMessThreeMercService(list, cus_Latitude, cus_Longtitude);
					//MINT
					OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService(merIDList, cus_Latitude, cus_Longtitude);
					bestTimeOneMessOneService = (List<NodeDetailVer2>)oneMess.oneMessengerForOneMerchants(listStation, listMerchant);
					double time = 99;
					for(int i = 0;i<bestTimeOneMessOneService.size();i++)
					{
						NodeDetailVer2 node = (NodeDetailVer2)bestTimeOneMessOneService.get(i);
						if(time > Double.parseDouble(node.getDuration()))
						{
							time = Double.parseDouble(node.getDuration());
						}
					}
					bestTimeOneMessOneServiceStr = "" + time;
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

			String chooseWay = "";

			if (routePathOneMessThreeService.getDuration() != null && !routePathOneMessThreeService.getDuration().equals("")){
				oneMessValue = Double.parseDouble(routePathOneMessThreeService.getDuration());
			}

			if (bestTimeTwoMessTwoService.getTotalDuration() != null && !bestTimeTwoMessTwoService.equals("")){
				twoMessValue = Double.parseDouble(bestTimeTwoMessTwoService.getTotalDuration());
			}

			//find the longest duration
			if (bestTimeOneMessOneService.size() != 0) {
				double longestDuration = 0;
				for (int i=0; i<bestTimeOneMessOneService.size(); i++) {
					if (i==0) {
						longestDuration = Double.parseDouble(bestTimeOneMessOneService.get(i).getDuration());
					}
					else {
						if (longestDuration < Double.parseDouble(bestTimeOneMessOneService.get(i).getDuration())) {
							longestDuration = Double.parseDouble(bestTimeOneMessOneService.get(i).getDuration());
						}
					}
				}
				threeMessValue = longestDuration;
			}

			if(list.size() == 1)
			{
				chooseWay = "3Messenger";
			}
			else if(list.size() == 2)
			{
				chooseWay = "1Messenger";
				chooseTime = oneMessValue;
				diffValue = chooseTime - threeMessValue;
				if(diffValue > 10)
				{
					chooseWay = "3Messenger";
				}
			}
			else if(list.size() == 3)
			{
				chooseTime = oneMessValue;
				diffValue = chooseTime - twoMessValue;
				if(diffValue > 10)
				{
					chooseWay = "2Messenger";
					chooseTime = twoMessValue;
					diffValue = chooseTime - threeMessValue;
					if(diffValue > 10)
					{
						chooseTime = threeMessValue;
						chooseWay = "3Messenger";
					}
				}
			}

			if(chooseWay.equals("1Messenger"))
			{
				Station tmpStation = routePathOneMessThreeService.getStation();
				ArrayList<Integer> arrFullId = fullQuery.getFulltimeMessengerFreeByStationID(tmpStation.getStationId());
				if(arrFullId.size() > 0)
				{
					int idMessenger = arrFullId.get(0);
					int runningNo = 1;
					for(int i = 0;i<routePathOneMessThreeService.getMerList().size();i++)
					{
						Merchants tmpMerchant = routePathOneMessThreeService.getMerList().get(i);
						for(int j = 0;j<seqOrderAndMerchant.size();j++)
						{
							HashMap<String, Object> tmpSeqOrderMerchant = seqOrderAndMerchant.get(j);
							int seqOrderMerID = (Integer)tmpSeqOrderMerchant.get("SEQOR_MER_ID");
							int seqOrderID = (Integer)tmpSeqOrderMerchant.get("SEQOR_ID");
							if(tmpMerchant.getMerID() == seqOrderMerID)
							{
								query.updateSequenceOrder(seqOrderID, idMessenger, runningNo);
								fullQuery.updateFulltimeMessengerStatus(orderId, idMessenger);
								runningNo++;
								break;
							}
						}
					}
					BigDecimal value = new BigDecimal(routePathOneMessThreeService.getDuration());
					int estimateTime = value.intValue();
					query.updateEstimateTimeToOrder(orderId, estimateTime);
					logger.info("esimate " + estimateTime);
				}

			}
			else if(chooseWay.equals("2Messenger"))
			{
				for(int i = 0;i<bestTimeTwoMessTwoService.getAllRoutePath().size();i++)
				{
					RoutePathDetail tmpPath = bestTimeTwoMessTwoService.getAllRoutePath().get(i);
					if(tmpPath.getMerList().size() == 1)
					{
						Station tmpStation = tmpPath.getStation();
						ArrayList<Integer> arrFullId = fullQuery.getFulltimeMessengerFreeByStationID(tmpStation.getStationId());
						if(arrFullId.size() > 0)
						{
							int idMessenger = arrFullId.get(0);
							int runningNo = 1;
							Merchants tmpMerchant = tmpPath.getMerList().get(0);
							for(int j = 0;j<seqOrderAndMerchant.size();j++)
							{
								HashMap<String, Object> tmpSeqOrderMerchant = seqOrderAndMerchant.get(j);
								int seqOrderMerID = (Integer)tmpSeqOrderMerchant.get("SEQOR_MER_ID");
								int seqOrderID = (Integer)tmpSeqOrderMerchant.get("SEQOR_ID");
								if(tmpMerchant.getMerID() == seqOrderMerID)
								{
									query.updateSequenceOrder(seqOrderID, idMessenger, runningNo);
									fullQuery.updateFulltimeMessengerStatus(orderId, idMessenger);
									runningNo++;
									break;
								}
							}
						}

					}
					else if(tmpPath.getMerList().size() == 2)
					{
						Station tmpStation = tmpPath.getStation();
						ArrayList<Integer> arrFullId = fullQuery.getFulltimeMessengerFreeByStationID(tmpStation.getStationId());
						if(arrFullId.size() > 0)
						{
							int idMessenger = arrFullId.get(0);
							int runningNo = 1;
							for(int j = 0;j<tmpPath.getMerList().size();j++)
							{
								Merchants tmpMerchant = tmpPath.getMerList().get(j);
								for(int k = 0;k<seqOrderAndMerchant.size();k++)
								{
									HashMap<String, Object> tmpSeqOrderMerchant = seqOrderAndMerchant.get(k);
									int seqOrderMerID = (Integer)tmpSeqOrderMerchant.get("SEQOR_MER_ID");
									int seqOrderID = (Integer)tmpSeqOrderMerchant.get("SEQOR_ID");
									if(tmpMerchant.getMerID() == seqOrderMerID)
									{
										query.updateSequenceOrder(seqOrderID, idMessenger, runningNo);
										fullQuery.updateFulltimeMessengerStatus(orderId, idMessenger);
										runningNo++;
										break;
									}
								}
							}
						}
					}
				}
				int estimateTime = 99;
				BigDecimal value = new BigDecimal(bestTimeTwoMessTwoService.getTotalDuration());
				estimateTime = value.intValue();
				query.updateEstimateTimeToOrder(orderId, estimateTime);
				logger.info("esimate " + estimateTime);
			}
			else if(chooseWay.equals("3Messenger"))
			{
				for(int i=0; i<bestTimeOneMessOneService.size(); i++)
				{
					NodeDetailVer2 nodeDetailVer2 = bestTimeOneMessOneService.get(i);
					int stationId = nodeDetailVer2.getStation();
					ArrayList<Integer> arrFullId = fullQuery.getFulltimeMessengerFreeByStationID(stationId);
					if(arrFullId.size() > 0)
					{
						int idMessenger = arrFullId.get(0);
						int runningNo = 1;
						Merchants tmpMerchant = nodeDetailVer2.getMerList().get(0);
						for(int j = 0;j<seqOrderAndMerchant.size();j++)
						{
							HashMap<String, Object> tmpSeqOrderMerchant = seqOrderAndMerchant.get(j);
							int seqOrderMerID 	= (Integer)tmpSeqOrderMerchant.get("SEQOR_MER_ID");
							int seqOrderID 		= (Integer)tmpSeqOrderMerchant.get("SEQOR_ID");
							if(tmpMerchant.getMerID() == seqOrderMerID)
							{
								query.updateSequenceOrder(seqOrderID, idMessenger, runningNo);
								//runningNo++;
								break;
							}
						}
					}
				}
				int estimateTime = 99;
				BigDecimal value = new BigDecimal(threeMessValue);
				estimateTime = value.intValue();
				query.updateEstimateTimeToOrder(orderId, estimateTime);
				logger.info("esimate " + estimateTime);
			}

	  }
	  return ResponseEntity.ok(new Response<String>(HttpStatus.OK.value(),"updateSequenceRoutePath successfully", "Success"));

	 }
	@RequestMapping(value={"service/fulltime/auth"} ,method=RequestMethod.POST)
	public ResponseEntity<Response<HashMap>> authen(@RequestBody FullTimeMessenger full){
		List<FullTimeMessenger> fullMess = null;
		HashMap<String,String> resultList = new HashMap<String, String>();

		try {
			logger.info("Check point 0");

			fullMess = fullMessDao.findByFullEmail(full.getFullEmail(),full.getFullPassword());

			logger.info(""+fullMess);

			if(fullMess.size() != 0){
				FullTimeMessenger fullMess1 = fullMess.get(0);
				resultList.put("full_id", String.valueOf(fullMess1.getFullId()));
				resultList.put("full_name", fullMess1.getFullName());
				resultList.put("full_contact_number", fullMess1.getFullContactNumber());
				resultList.put("full_email", fullMess1.getFullEmail());
				resultList.put("full_status_id", String.valueOf(fullMess1.getFullStatusId()));
				resultList.put("full_recommend_lattitude", fullMess1.getFullRecommendLattitude());
				resultList.put("full_recommend_longtitude", fullMess1.getFullRecommendLongtitude());


				logger.info(""+resultList);
			}else{
				logger.info("Check point 1");
				return null;
			}


			return ResponseEntity.ok(new Response<HashMap>(HttpStatus.OK.value(),
					"Login successfully", resultList));

		}
		catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			logger.info("Check point 2");
			return null;
		}


	}
}

