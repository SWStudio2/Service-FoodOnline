package com.fooddelivery.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


import com.fooddelivery.Model.BikeStation;
import com.fooddelivery.Model.BikeStationDao;
import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.FullTimeMessenger;
import com.fooddelivery.Model.FullTimeMessengerDao;
import com.fooddelivery.Model.FullTimeMessengerQuery;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsDao;
import com.fooddelivery.Model.MerchantsQuery;
import com.fooddelivery.Model.OrderDetail;
import com.fooddelivery.Model.Orders;
import com.fooddelivery.Model.OrdersDao;
import com.fooddelivery.Model.SequenceOrders;
import com.fooddelivery.Model.SequenceOrdersDao;
import com.fooddelivery.Model.Station;
import com.fooddelivery.Model.StationQuery;
import com.fooddelivery.Model.TimeAndDistanceDetail;
import com.fooddelivery.Model.TimeAndDistanceDetailDao;
import com.fooddelivery.Model.UtilsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.fooddelivery.service.durationpath.OneMessengerOneMerchantService;
import com.fooddelivery.service.durationpath.TwoMessThreeMercService;
import com.fooddelivery.util.DateTime;
import com.fooddelivery.util.GroupPathDetail;
import com.fooddelivery.util.NodeDetailVer2;
import com.fooddelivery.util.Response;
import com.fooddelivery.util.RoutePathDetail;
import com.fooddelivery.util.Utils;
import com.fooddelivery.util.VariableText;

@RestController
public class MessengerController {
	private static final Logger logger = LoggerFactory.getLogger(MessengerController.class);
	@Autowired
	private FullTimeMessengerDao fullMessDao;
	@Autowired
	private BikeStationDao bikeStationDao;
	@Autowired
	private MerchantsDao merchantsDao;
	
	@Autowired
	private SequenceOrdersDao seqOrderDao;
	@Autowired
	private OrdersDao ordersDao;


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

		List<BikeStation> listStation = bikeStationDao.getBikeStationAvailable();

		List<Merchants> listMerchant = new ArrayList<Merchants>();
		for(int i = 0;i<merList.length;i++)
		{
			listMerchant.add(merList[i]);
			
		}

		//String bestTimeOneMessOneService = "";
		GroupPathDetail bestTimeOneMessOneMerchantService = new GroupPathDetail();
		GroupPathDetail bestTimeTwoMessTwoService = new GroupPathDetail();
		RoutePathDetail routePathOneMessThreeService = new RoutePathDetail();

		if(list.size() == 1)
		{
			try {
				TimeAndDistanceDetail[] timeAndDistanceDetail = getBikePathByMerchants(merIDList);
				List<Merchants> merchants = getMerchantsByMerchantsId(merIDList);
				List<Station> stations = getStationAvailable();
				OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService(merIDList, 
						cus_Latitude, cus_Longtitude, timeAndDistanceDetail, merchants, stations);
				bestTimeOneMessOneMerchantService = oneMess.oneMessengerForOneMerchants(listStation);
				
				routePathOneMessThreeService = this.searchFuncOneMessenger(list, cus_Latitude, cus_Longtitude);
			} catch (InterruptedException e) {
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
				//bestTimeTwoMessTwoService = twoMessService.twoMessThreeMercService(list, cus_Latitude, cus_Longtitude);
				//MINT
				TimeAndDistanceDetail[] timeAndDistanceDetail = getBikePathByMerchants(merIDList);
				List<Merchants> merchants = getMerchantsByMerchantsId(merIDList);
				List<Station> stations = getStationAvailable();
				OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService(merIDList, 
						cus_Latitude, cus_Longtitude, timeAndDistanceDetail, merchants, stations);
				bestTimeOneMessOneMerchantService = oneMess.oneMessengerForOneMerchants(listStation);
				routePathOneMessThreeService = this.searchFuncOneMessenger(list, cus_Latitude, cus_Longtitude);
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

		if (bestTimeOneMessOneMerchantService.getTotalDuration() != null && !bestTimeOneMessOneMerchantService.equals("")){
			threeMessValue = Double.parseDouble(bestTimeOneMessOneMerchantService.getTotalDuration());
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
	  String msg = "";
	  if(isRecall.equals("Y"))
	  {
		  
		  seqOrderAndMerchant = query.getMerchantAndOrderSeqByOrderId(orderId);
		  lantAndLong = query.getLatitudeAndLongtitudeByOrderId(orderId);

		  	List<Integer> list = new ArrayList<Integer>();
		  	System.out.println("SIZE : " + seqOrderAndMerchant.size());
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

			MerchantsQuery merQuery = new MerchantsQuery();
			//Merchants[] merList = merQuery.queryMerChantByID(merIdAdjust);
			System.out.println(""+list);
			List<Merchants> merList = merchantsDao.getMerchantsByMerIds(list);
			List<BikeStation> listStation = bikeStationDao.getBikeStationAvailable();

			List<Merchants> listMerchant = new ArrayList<Merchants>();
			for(int i = 0;i<merList.size();i++)
			{
				listMerchant.add(merList.get(i));
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
				TimeAndDistanceDetail[] timeAndDistanceDetail = getBikePathByMerchants(merIDList);
				List<Merchants> merchants = getMerchantsByMerchantsId(merIDList);
				List<Station> stations = getStationAvailable();
				OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService(merIDList, 
						cus_Latitude, cus_Longtitude, timeAndDistanceDetail, merchants, stations);
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
				TimeAndDistanceDetail[] timeAndDistanceDetail = getBikePathByMerchants(merIDList);
				List<Merchants> merchants = getMerchantsByMerchantsId(merIDList);
				List<Station> stations = getStationAvailable();
				OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService(merIDList, 
						cus_Latitude, cus_Longtitude, timeAndDistanceDetail, merchants, stations);
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
					TimeAndDistanceDetail[] timeAndDistanceDetail = getBikePathByMerchants(merIDList);
					List<Merchants> merchants = getMerchantsByMerchantsId(merIDList);
					List<Station> stations = getStationAvailable();
					OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService(merIDList, 
							cus_Latitude, cus_Longtitude, timeAndDistanceDetail, merchants, stations);
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
				chooseWay = "1Messenger";
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
			logger.info("chooseWay." + chooseWay);
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
								logger.info("1mess. idMessenger" + idMessenger);
								query.updateSequenceOrder(seqOrderID, idMessenger, runningNo);
								fullQuery.updateFulltimeMessengerStatus(orderId, idMessenger);
								runningNo++;
								break;
							}
						}
					}
					BigDecimal value = new BigDecimal(routePathOneMessThreeService.getDuration());
					int estimateTime = value.intValue();
					estimateTime = value.intValue();
					Date currentDateTime = DateTime.getCurrentDateTime();
					Calendar cal = Calendar.getInstance();
					cal.setTime(currentDateTime);
					cal.add(Calendar.MINUTE, estimateTime);
					currentDateTime = cal.getTime();
					query.updateEstimateTimeToOrder(orderId, estimateTime,currentDateTime);
					logger.info("esimate " + estimateTime);
					msg = "updateSequenceRoutePath successfully";
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
									logger.info("2mess. idMessenger" + idMessenger);
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
										logger.info("3mess. idMessenger" + idMessenger);
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
				Date currentDateTime = DateTime.getCurrentDateTime();
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDateTime);
				cal.add(Calendar.MINUTE, estimateTime);
				currentDateTime = cal.getTime();
				query.updateEstimateTimeToOrder(orderId, estimateTime,currentDateTime);
				logger.info("esimate " + estimateTime);
				msg = "updateSequenceRoutePath successfully";
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
								fullQuery.updateFulltimeMessengerStatus(orderId, idMessenger);
								//runningNo++;
								break;
							}
						}
					}
				}
				int estimateTime = 99;
				BigDecimal value = new BigDecimal(threeMessValue);
				estimateTime = value.intValue();
				Date currentDateTime = DateTime.getCurrentDateTime();
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDateTime);
				cal.add(Calendar.MINUTE, estimateTime);
				currentDateTime = cal.getTime();
				query.updateEstimateTimeToOrder(orderId, estimateTime,currentDateTime);
				logger.info("esimate " + estimateTime);
				msg = "updateSequenceRoutePath successfully";
			}

	  }
	  else
	  {
		  msg = "No update";
	  }
	  return ResponseEntity.ok(new Response<String>(HttpStatus.OK.value(),msg, "Success"));

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
	
	private TimeAndDistanceDetail[] getBikePathByMerchants(int[] merchantsId) {
		TimeAndDistanceDetail[] result = null;
		try {
			// merchantsId --> '1,2,3'
			String merchantsIdStr = "";
			for (int i=0; i<merchantsId.length; i++) {
				if (i != 0) {
					merchantsIdStr += ",";
				}
				merchantsIdStr += merchantsId[i];
			}
			TimeAndDistanceDetailDao timeAndDistance = new TimeAndDistanceDetailDao();
			result = timeAndDistance.getTimeAndDistanceDetail(merchantsIdStr);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			return null;
		}
		return result;
	}
	
	private List<Merchants> getMerchantsByMerchantsId(int[] merchantsId) {
		List<Merchants> result = null;
		try {
			// merchantsId --> 1,2,3 <--List<Integer>
			List<Integer> merchantsIdList = new ArrayList<Integer>();
			for (int i=0; i<merchantsId.length; i++) {
				merchantsIdList.add(Integer.valueOf(merchantsId[i]));
			}
			result = merchantsDao.getMerchantsByMerIds(merchantsIdList);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			return null;
		}
		return result;
	}
	
	private List<Station> getStationAvailable() {
		StationQuery stationQue = new StationQuery();
		Station[] staList = stationQue.getStationAvailable();
		List<Station> result = new ArrayList<Station>();
		for (int i=0; i<staList.length; i++) {
			result.add(staList[i]);
		}
		
		return result;
	}
	
	@RequestMapping(value={"service/fulltime/updateloc"} ,method=RequestMethod.POST)
	public ResponseEntity<Response<HashMap>> updateLocation(@RequestBody FullTimeMessenger fullTimeMess){
		try {
			FullTimeMessenger fullTimeMessenger = fullMessDao.findById(fullTimeMess.getFullId());
			
			if (fullTimeMessenger != null) {
				fullTimeMessenger.setFullLastestLattitude(fullTimeMess.getFullLatestLattitude());
				fullTimeMessenger.setFullLastestLongtitude(fullTimeMess.getFullLatestLongtitude());
				fullMessDao.save(fullTimeMessenger);
			}
			
			return ResponseEntity.ok(new Response<HashMap>(HttpStatus.OK.value(),"Get data successfully",null));
		}
		catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			return null;
		}
	}
	
	@RequestMapping(value={"service/fulltime/{fullId}"} ,method=RequestMethod.GET)
	public ResponseEntity<Response<com.fooddelivery.Model.Order.fulltimemessenger.Orders>> 
	getOrderByFullTimeMessenger(@PathVariable("fullId") String fullId){
		try {
			long fullTimeMessengerId = Long.parseLong(fullId);
			
			List<SequenceOrders> seqOrdersList = seqOrderDao.getSequenceOrderByMessId(fullTimeMessengerId,
					 VariableText.ORDER_COOKING_STATUS);
			
			Orders order = new Orders();
			com.fooddelivery.Model.Order.fulltimemessenger.Orders ordersResult = new 
					com.fooddelivery.Model.Order.fulltimemessenger.Orders();
			com.fooddelivery.Model.Order.fulltimemessenger.Customer customerResult = new
					com.fooddelivery.Model.Order.fulltimemessenger.Customer();
			List<com.fooddelivery.Model.Order.fulltimemessenger.SequenceOrders> sequenceOrdersList = new 
					ArrayList<com.fooddelivery.Model.Order.fulltimemessenger.SequenceOrders>();
			List<com.fooddelivery.Model.Order.fulltimemessenger.OrderDetail> orderDetailList = new 
					ArrayList<com.fooddelivery.Model.Order.fulltimemessenger.OrderDetail>();
			/*List<com.fooddelivery.Model.Order.fulltimemessenger.Merchants> merchantResult = new 
					ArrayList<com.fooddelivery.Model.Order.fulltimemessenger.Merchants>();*/
			/*com.fooddelivery.Model.Order.fulltimemessenger.FullTimeMessenger fullTimeMessengerResult = new 
			com.fooddelivery.Model.Order.fulltimemessenger.FullTimeMessenger();*/
			if (seqOrdersList != null) {
				long seqOrderId = seqOrdersList.get(0).getSequenceOrderId();
				List<Long> merchantIds = setMerchantsIdsList(seqOrdersList);
				//map sequenceOrders
				for (int i=0; i<seqOrdersList.size(); i++) {
					com.fooddelivery.Model.Order.fulltimemessenger.SequenceOrders sequenceOrder = new 
							com.fooddelivery.Model.Order.fulltimemessenger.SequenceOrders();
					sequenceOrder.mapping(seqOrdersList.get(i));
					sequenceOrdersList.add(sequenceOrder);
				}
				//map fullTimeMessenger
				/*FullTimeMessenger fullTimeMessenger = seqOrdersList.get(0).getFullTimeMessenger();
				fullTimeMessengerResult.mapping(fullTimeMessenger);
				*/
				
				order = ordersDao.getOrderByOrderId(seqOrderId);
				if (order != null) {
					//map orderDetail & merchant
					List<OrderDetail> orderDetail = order.getOrderDetails();
					orderDetailList = mappingOrderDetailOfFullTimeMessenger(orderDetail, merchantIds);
					
					//mapping customer
					Customer customer = order.getCustomer();
					customerResult.mapping(customer);
					
					ordersResult.mapping(order);
					ordersResult.setCustomer(customerResult);
					ordersResult.setSequenceOrders(sequenceOrdersList);
					ordersResult.setOrderDetails(orderDetailList);
				}
			}
			
			return ResponseEntity.ok(new Response<com.fooddelivery.Model.Order.fulltimemessenger.Orders>
			(HttpStatus.OK.value(),"Get data successfully",ordersResult));
		}
		catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			return null;
		}
	}
	
	private List<Long> setMerchantsIdsList(List<SequenceOrders> seqOrdersList) {
		List<Long> result = new ArrayList<Long>();
		for (int i=0; i<seqOrdersList.size(); i++) {
			result.add(seqOrdersList.get(i).getSequenceOrderMerchantId());
		}
		return result;
	}
	
	private List<com.fooddelivery.Model.Order.fulltimemessenger.OrderDetail> mappingOrderDetailOfFullTimeMessenger(
			List<OrderDetail> orderDetails, List<Long> merchantIds) {
		List<com.fooddelivery.Model.Order.fulltimemessenger.OrderDetail> result = 
				new ArrayList<com.fooddelivery.Model.Order.fulltimemessenger.OrderDetail>();
		
		for (int i=0; i<orderDetails.size(); i++) {
			for (int j=0; j<merchantIds.size(); j++) {
				if (orderDetails.get(i).getMerId() == merchantIds.get(j)) {
					//map merchant
					com.fooddelivery.Model.Order.fulltimemessenger.Merchants merchant = new
							com.fooddelivery.Model.Order.fulltimemessenger.Merchants();
					merchant.mapping(orderDetails.get(i).getMerchant());
					
					//map orderDetail
					com.fooddelivery.Model.Order.fulltimemessenger.OrderDetail orderDetail = new 
							com.fooddelivery.Model.Order.fulltimemessenger.OrderDetail();
					orderDetail.mapping(orderDetails.get(i));
					
					orderDetail.setMerchant(merchant);
					result.add(orderDetail);
				}
			}
		}
		
		return result;
	}
	
	//************คำนวณหาจุดจอดใหม่***********************
	/*
	 * ให้เอาที่อยู่ลูกค้าสุดท้าย มาคิดหาระยะทางกับจุดจอดทั้งหมด 
	 * แล้วเปรียบเทียบกับ 5 กิโลเมตร
	 * ภายในจุดจอดที่อยู่ใน 5 กิโล ให้ดูที่ จน. แมสว่ามีน้อยไหม
	 * ถ้าเท่ากัน ก็เลือกระยะที่อยู่น้อยที่สุด
	 * แต่ถ้าระยะทางห่างเกิน 5 กิโล ก็ไม่ต้องเข้าข้างบน 
	 * ให้หาจุดจอดที่ใกล้ที่สุด
	 * update messenger ด้วย
	 * return station 
	 * - station id
	 * - latitude
	 * - long
	 * - station name
	 */
	/*@RequestMapping(value={"service/fulltime/calculateNewStation/{lastestLatitude}"}, method=RequestMethod.GET)*/
	//public ResponseEntity<Response<BikeStation>> calculateNewStation(@PathVariable("lastestLatitude") 
	public BikeStation calculateNewStation(
		String lastestLatitude, String latestLongtitude) {
		//สมมติค่า lastestLatitude , latestLongtitude
		//lastestLatitude = "13.7324056";//"13.9038336";
		//latestLongtitude = "100.5304452";//"100.621662";
		BikeStation result = new BikeStation();
		
		
		//คำนวณจุดจอดใกล้-ไกล
		HomeController homeController = new HomeController();
		List<BikeStation> bikeStationList = bikeStationDao.getBikeStationAll();
		HashMap<String, BikeStation> bikeStationHashMap = new HashMap<String, BikeStation>();
		bikeStationHashMap = convertBikeStationListToHashMap(bikeStationList);
		//HashMap<String, String[]> bikeStationDistanceHash = new HashMap<String, String[]>();
		List<Object[]> fullTimeMessengerInStation = fullMessDao.getNumberOfMessengerInStation();
		/*for (int j=0; j<fullTimeMessengerInStation.size(); j++) {
			System.out.println("station: " + fullTimeMessengerInStation.get(j)[0] + 
					" available: " + fullTimeMessengerInStation.get(j)[1]);
		}*/
		List<double[]> bikeStationDistanceList = new ArrayList<double[]>();
		HashMap<String, String[]> numberFullTimeAvailableInStationHash = convertNumberMessengerInStationListToHashMap(
				fullTimeMessengerInStation);
		for(int i=0; i<bikeStationList.size(); i++) {
			BikeStation bikeStation = bikeStationList.get(i);
			try {
				Thread.sleep(600);
				String[] detailArray = (String[]) homeController.getDistanceDuration(
						lastestLatitude,
						latestLongtitude, 
						bikeStation.getBikeStationLatitude(), 
						bikeStation.getBikeStationLongitude());
				Double numberFullTimeInStation = Double.valueOf(numberFullTimeAvailableInStationHash.get(String.valueOf(
						bikeStation.getBikeStationId()))[1]);
				//bikeStationDistanceHash = sortBikeStationDistanceHash();
				if (numberFullTimeInStation != 0) {
					bikeStationDistanceList.add(new double[] { bikeStation.getBikeStationId(), 
						Double.valueOf(detailArray[0]),
						numberFullTimeInStation});
					/*bikeStationDistanceHash.put(String.valueOf(bikeStation.getBikeStationId()), 
							new String[]{detailArray[0], String.valueOf(numberFullTimeInStation)});*/
				}
				else {
					bikeStationDistanceList.add(new double[] { bikeStation.getBikeStationId(), 
							Double.valueOf(detailArray[0]),
							100});
					/*bikeStationDistanceHash.put(String.valueOf(bikeStation.getBikeStationId()), 
							new String[]{"100","100"});*/
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//sort List
		List<double[]> bikeStationDistanceSortedList = sortBikeStationDistanceHash(bikeStationDistanceList);
		for (int i=0; i<bikeStationDistanceSortedList.size(); i++) {
			System.out.println("Station ID: " + bikeStationDistanceSortedList.get(i)[0] +
					" | distance: " + bikeStationDistanceSortedList.get(i)[1] +
					" | number: " + bikeStationDistanceSortedList.get(i)[2]);
		}
		
		if ((double) bikeStationDistanceSortedList.get(0)[1] < 5.00) {
			for (int i=0; i<bikeStationDistanceSortedList.size(); i++) {
				if (bikeStationDistanceSortedList.get(i)[1] > 5.00) {
					bikeStationDistanceSortedList.remove(i);
				}
			}
			//หา ระยะทางน้อยที่สุดที่มี messenger น้อยที่สุดเช่นกัน
			double[] temp = bikeStationDistanceSortedList.get(0);
			int initialNumberOfMessenger = (int) temp[2];
			if (bikeStationDistanceSortedList.size() > 1) {
				for (int i=1; i<bikeStationDistanceSortedList.size(); i++) {
					if (initialNumberOfMessenger > (int) bikeStationDistanceSortedList.get(i)[2]) {
						initialNumberOfMessenger = (int) bikeStationDistanceSortedList.get(i)[2];
						temp = bikeStationDistanceSortedList.get(i);
					}
				}
				result = bikeStationHashMap.get(String.valueOf( (int) temp[0] ) );
			}
			else {
				result = bikeStationHashMap.get(String.valueOf( (int) bikeStationDistanceSortedList.get(0)[0] ) );
			}
		}
		else {
			result = bikeStationHashMap.get(String.valueOf( (int) bikeStationDistanceSortedList.get(0)[0] ) );
		}
		/*return ResponseEntity.ok(new Response<BikeStation>(HttpStatus.OK.value(),"Get data successfully",
				result));*/
		return result;
	}
	
	private HashMap<String, String[]> convertNumberMessengerInStationListToHashMap(List<Object[]> numberMessengerInStation) {
		HashMap<String, String[]> result = new HashMap<String, String[]>();
		for (int i=0; i<numberMessengerInStation.size(); i++) {
			String[] intArray = new String[]{numberMessengerInStation.get(i)[0] + "", 
					numberMessengerInStation.get(i)[1] + ""};
			result.put(String.valueOf(numberMessengerInStation.get(i)[0]), intArray);
		}
		return result;
	}
	
	private HashMap<String, BikeStation> convertBikeStationListToHashMap(List<BikeStation> bikeStationList) {
		HashMap<String, BikeStation> result = new HashMap<String, BikeStation>();
		for (int i=0; i<bikeStationList.size(); i++) {
			result.put(String.valueOf(bikeStationList.get(i).getBikeStationId()), bikeStationList.get(i));
		}
		return result;
	}
	
	private List<double[]> sortBikeStationDistanceHash(List<double[]> bikeStationDistance) {
		Collections.sort(bikeStationDistance, new Comparator() {
			public int compare(Object o1, Object o2) {
				Double distance1 = Double.valueOf(((double[]) o1)[1]);
				Double distance2 = Double.valueOf(((double[]) o2)[1]);
				double sComp = distance1.compareTo(distance2);
				
				if (sComp != 0) {
					return (int) sComp;
				}
				else {
					Double number1 = Double.valueOf(((double[]) o1)[2]);
					Double number2 = Double.valueOf(((double[]) o2)[2]);
					return number1.compareTo(number2);
				}
			}
		});
		return bikeStationDistance;
	}
	
}

