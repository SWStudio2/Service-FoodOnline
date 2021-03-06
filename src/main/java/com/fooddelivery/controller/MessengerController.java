package com.fooddelivery.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fooddelivery.Model.BikePathDao;
import com.fooddelivery.Model.BikeStation;
import com.fooddelivery.Model.BikeStationDao;
import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.CustomerDao;
import com.fooddelivery.Model.FullTimeMessenger;
import com.fooddelivery.Model.FullTimeMessengerDao;
import com.fooddelivery.Model.FullTimeMessengerQuery;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsDao;
import com.fooddelivery.Model.MerchantsQuery;
import com.fooddelivery.Model.NotificationInbox;
import com.fooddelivery.Model.NotificationInboxDao;
import com.fooddelivery.Model.OrderDetail;
import com.fooddelivery.Model.Orders;
import com.fooddelivery.Model.OrdersDao;
import com.fooddelivery.Model.SequenceOrders;
import com.fooddelivery.Model.SequenceOrdersDao;
import com.fooddelivery.Model.Station;
import com.fooddelivery.Model.StationDao;
import com.fooddelivery.Model.StationQuery;
import com.fooddelivery.Model.TimeAndDistanceDetail;
import com.fooddelivery.Model.TimeAndDistanceDetailDao;
import com.fooddelivery.Model.UtilsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
import com.fooddelivery.util.model.Location;

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
	
	@Autowired
	private NotificationInboxDao notiDao;
	
	@Autowired
	private FullTimeMessengerDao fullTimeMessengerDao;
	
	@Autowired
	private StationDao staDao;
	
	@Autowired
	private BikePathDao bikePathDao;

	@Autowired
	private CustomerDao custDao;

	@RequestMapping(value="/service/getestimatedtime", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<Map<String, Object>>> getEstimatedTime(
			@RequestBody Map<String, Object> mapRequest) {

		String cus_Latitude = (String) mapRequest.get("cusLatitude");
		String cus_Longtitude = (String) mapRequest.get("cusLongitude");
		
		Location locationCustomer = new Location();
		locationCustomer.setLatitude(cus_Latitude);
		locationCustomer.setLongtitude(cus_Longtitude);
		
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
		GroupPathDetail bestTimeTwoMessThreeMerService = new GroupPathDetail();
		RoutePathDetail routePathOneMessThreeService = new RoutePathDetail();

		if(list.size() == 1)
		{
			try {
				TimeAndDistanceDetail[] timeAndDistanceDetail = getBikePathByMerchants(merIDList);
				List<Merchants> merchants = getMerchantsByMerchantsId(merIDList);
				List<Station> stations = getStationAvailable();
				OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService();
				bestTimeOneMessOneMerchantService = oneMess.oneMessengerOneMerchantService(
						locationCustomer, timeAndDistanceDetail, merchants, stations, listStation, null, null, 
						null);
				//bestTimeOneMessOneMerchantService = oneMess.oneMessengerForOneMerchants(listStation, null, null);		
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
		else if(list.size() == 2) 
		{
			try {
				TimeAndDistanceDetail[] timeAndDistanceDetail = getBikePathByMerchants(merIDList);
				List<Merchants> merchants = getMerchantsByMerchantsId(merIDList);
				List<Station> stations = getStationAvailable();
				OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService();
				bestTimeOneMessOneMerchantService = oneMess.oneMessengerOneMerchantService(
						locationCustomer, timeAndDistanceDetail, merchants, stations, 
						listStation, null, null, fullTimeMessengerDao);
				//bestTimeOneMessOneMerchantService = oneMess.oneMessengerForOneMerchants(listStation, null, null);
				//P'YUI
				routePathOneMessThreeService = this.searchFuncOneMessenger(list, cus_Latitude, cus_Longtitude,null);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
		else if(list.size() == 3)
		{
			try {
				//Mike
				routePathOneMessThreeService = this.searchFuncOneMessenger(list, cus_Latitude, cus_Longtitude,null);

				//YUI
				//prepare default cooking time (pull from DB)
				HashMap<Integer, Double> hashMerCookingTime = new HashMap<Integer, Double>();
				for(int i = 0;i<merIDList.length;i++)
				{
					int cookingTime = merchantsDao.findMerCookTime(merIDList[i]);
					hashMerCookingTime.put(merIDList[i], cookingTime*1.0);
				}
				TwoMessThreeMercService twoMessService = new TwoMessThreeMercService();
				bestTimeTwoMessThreeMerService = twoMessService.twoMessThreeMercService(list, cus_Latitude, cus_Longtitude,	fullMessDao
						,staDao,merchantsDao,bikePathDao,hashMerCookingTime);
				
				//MINT
				TimeAndDistanceDetail[] timeAndDistanceDetail = getBikePathByMerchants(merIDList);
				List<Merchants> merchants = getMerchantsByMerchantsId(merIDList);
				List<Station> stations = getStationAvailable();

				OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService();
				bestTimeOneMessOneMerchantService = oneMess.oneMessengerOneMerchantService(
						locationCustomer, timeAndDistanceDetail, merchants, stations,
						listStation, null, null, fullTimeMessengerDao);
				//routePathOneMessThreeService = this.searchFuncOneMessenger(list, cus_Latitude, cus_Longtitude);

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
		
		if (bestTimeTwoMessThreeMerService.getTotalDuration() != null && !bestTimeTwoMessThreeMerService.equals("")){
			twoMessValue = Double.parseDouble(bestTimeTwoMessThreeMerService.getTotalDuration());
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
			//chooseTime = oneMessValue;//Use this step temporary
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

	public RoutePathDetail searchFuncOneMessenger(List<Integer> merId,String cus_Latitude,String cus_Longtitude,HashMap<Integer, Double> hashCooking) throws InterruptedException {

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
		bestNode = bestNode.getBestNodeDetail(arrNode,tmpTimeAndDistance,hashCooking);
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
		
		Location locationCustomer = new Location();
		
		if(isRecall.equals("Y"))
		{

			seqOrderAndMerchant = query.getMerchantAndOrderSeqByOrderId(orderId);
			lantAndLong = query.getLatitudeAndLongtitudeByOrderId(orderId);

			List<Integer> list = new ArrayList<Integer>();
			System.out.println("SIZE : " + seqOrderAndMerchant.size());
			HashMap<Integer, Double> hashMerCookingTime = new HashMap<Integer, Double>();
			for(int i = 0;i<seqOrderAndMerchant.size();i++)
			{
				HashMap<String, Object> tmpHashSeqOrder = seqOrderAndMerchant.get(i);
				list.add((Integer) tmpHashSeqOrder.get("SEQOR_MER_ID"));
				Double cookingTime = (Double)tmpHashSeqOrder.get("SEQOR_COOK_TIME");
				hashMerCookingTime.put((Integer) tmpHashSeqOrder.get("SEQOR_MER_ID"), cookingTime);
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
			
			locationCustomer.setLatitude(cus_Latitude);
			locationCustomer.setLongtitude(cus_Longtitude);

			GroupPathDetail bestTimeOneMessOneMerchantService = new GroupPathDetail();
			GroupPathDetail bestTimeTwoMessTwoService = new GroupPathDetail();
			RoutePathDetail routePathOneMessThreeService = new RoutePathDetail();

			List<Object[]> fullTimeMessengerInStation = fullTimeMessengerDao.getNumberOfMessengerInStation();

			if(list.size() == 1)
			{
				try {
					TimeAndDistanceDetail[] timeAndDistanceDetail = getBikePathByMerchants(merIDList);
					List<Merchants> merchants = getMerchantsByMerchantsId(merIDList);
					List<Station> stations = getStationAvailable();
					OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService();
					bestTimeOneMessOneMerchantService = oneMess.oneMessengerOneMerchantService(
							locationCustomer, timeAndDistanceDetail, merchants, stations,
							listStation, hashMerCookingTime, fullTimeMessengerInStation, fullTimeMessengerDao);
					/*bestTimeOneMessOneMerchantService = oneMess.oneMessengerForOneMerchants(
							listStation, hashMerCookingTime, fullTimeMessengerInStation);*/
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(list.size() == 2)
			{
				
				//1 mess to many merchant
				try {
					routePathOneMessThreeService = this.searchFuncOneMessenger(list, cus_Latitude, cus_Longtitude,hashMerCookingTime);
					//1 mess to one merchant
					//Mint
					TimeAndDistanceDetail[] timeAndDistanceDetail = getBikePathByMerchants(merIDList);
					List<Merchants> merchants = getMerchantsByMerchantsId(merIDList);
					List<Station> stations = getStationAvailable();
					OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService();
					bestTimeOneMessOneMerchantService = oneMess.oneMessengerOneMerchantService(
							locationCustomer, timeAndDistanceDetail, merchants, stations,
							listStation, hashMerCookingTime, fullTimeMessengerInStation, fullTimeMessengerDao);
					//1 mess to many merchant
					//routePathOneMessThreeService = this.searchFuncOneMessenger(list, cus_Latitude, cus_Longtitude);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(list.size() == 3)
			{
				try {
					//Mike
					routePathOneMessThreeService = this.searchFuncOneMessenger(list, cus_Latitude, cus_Longtitude,hashMerCookingTime);

					//YUI
					TwoMessThreeMercService twoMessService = new TwoMessThreeMercService();
					bestTimeTwoMessTwoService = twoMessService.twoMessThreeMercService(list, cus_Latitude, cus_Longtitude, fullMessDao
						      ,staDao,merchantsDao,bikePathDao,hashMerCookingTime);

					//MINT
					TimeAndDistanceDetail[] timeAndDistanceDetail = getBikePathByMerchants(merIDList);
					List<Merchants> merchants = getMerchantsByMerchantsId(merIDList);
					List<Station> stations = getStationAvailable();
					OneMessengerOneMerchantService oneMess = new OneMessengerOneMerchantService();
					bestTimeOneMessOneMerchantService = oneMess.oneMessengerOneMerchantService(
							locationCustomer, timeAndDistanceDetail, merchants, stations,
							listStation, hashMerCookingTime, fullTimeMessengerInStation, fullTimeMessengerDao);
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
			
			if (bestTimeOneMessOneMerchantService.getTotalDuration() != null && !bestTimeOneMessOneMerchantService.equals("")) {
				threeMessValue = Double.parseDouble(bestTimeOneMessOneMerchantService.getTotalDuration());
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
				chooseWay = "3Messenger";//*****
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
					query.updateEstimateTimeToOrder(orderId, estimateTime);
					logger.info("esimate " + estimateTime);
					msg = "updateSequenceRoutePath successfully";
					
                    NotificationInbox noti = new NotificationInbox();
                    noti.setNoti_message_detail("คุณได้รับมอบหมายงานออร์เดอร์รหัส " + orderId + " กรุณากดยืนยันเพื่อเริ่มงาน");
                    noti.setNoti_ref_id(idMessenger);//
                    noti.setNoti_message_type(VariableText.NOTIFICATION_MSG_TYPE_REQUEST);
                    noti.setNoti_read_flag(0);
                    noti.setNoti_type("Messenger");
                    noti.setNoti_order_id(orderId);
                    noti.setNoti_created_date(new Date());
                    notiDao.save(noti);
                    	                    
				}
				//Send noti to customer
                NotificationInbox notiToCustomer = new NotificationInbox();
                notiToCustomer.setNoti_message_detail("ออร์เดอร์รหัส " + orderId + " ร้านค้ากำลังทำอาหาร");
                notiToCustomer.setNoti_ref_id(custDao.getCustomerIdByOrderId(orderId));//
                notiToCustomer.setNoti_message_type(VariableText.NOTIFICATION_MSG_TYPE_REQUEST);
                notiToCustomer.setNoti_read_flag(0);
                notiToCustomer.setNoti_type("Customer");
                notiToCustomer.setNoti_order_id(orderId);
                notiToCustomer.setNoti_created_date(new Date());
                notiDao.save(notiToCustomer);		
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
		                    NotificationInbox noti = new NotificationInbox();
		                    noti.setNoti_message_detail("คุณได้รับมอบหมายงานออร์เดอร์รหัส " + orderId + " กรุณากดยืนยันเพื่อเริ่มงาน");
		                    noti.setNoti_ref_id(idMessenger);//
		                    noti.setNoti_message_type(VariableText.NOTIFICATION_MSG_TYPE_REQUEST);
		                    noti.setNoti_read_flag(0);
		                    noti.setNoti_type("Messenger");
		                    noti.setNoti_order_id(orderId);
		                    noti.setNoti_created_date(new Date());
		                    notiDao.save(noti);			                    
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
		                    NotificationInbox noti = new NotificationInbox();
		                    noti.setNoti_message_detail("คุณได้รับมอบหมายงานออร์เดอร์รหัส " + orderId + " กรุณากดยืนยันเพื่อเริ่มงาน");
		                    noti.setNoti_ref_id(idMessenger);//
		                    noti.setNoti_message_type(VariableText.NOTIFICATION_MSG_TYPE_REQUEST);
		                    noti.setNoti_read_flag(0);
		                    noti.setNoti_type("Messenger");
		                    noti.setNoti_order_id(orderId);
		                    noti.setNoti_created_date(new Date());
		                    notiDao.save(noti);		                   		                    
						}
					}
				}
				
				//Send noti to customer
                NotificationInbox notiToCustomer = new NotificationInbox();
                notiToCustomer.setNoti_message_detail("ออร์เดอร์รหัส " + orderId + " ร้านค้ากำลังทำอาหาร");
                notiToCustomer.setNoti_ref_id(custDao.getCustomerIdByOrderId(orderId));//
                notiToCustomer.setNoti_message_type(VariableText.NOTIFICATION_MSG_TYPE_REQUEST);
                notiToCustomer.setNoti_read_flag(0);
                notiToCustomer.setNoti_type("Customer");
                notiToCustomer.setNoti_order_id(orderId);
                notiToCustomer.setNoti_created_date(new Date());
                notiDao.save(notiToCustomer);
                
				int estimateTime = 99;
				BigDecimal value = new BigDecimal(bestTimeTwoMessTwoService.getTotalDuration());
				estimateTime = value.intValue();
				Date currentDateTime = DateTime.getCurrentDateTime();
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDateTime);
				cal.add(Calendar.MINUTE, estimateTime);
				currentDateTime = cal.getTime();
				query.updateEstimateTimeToOrder(orderId, estimateTime);
				logger.info("esimate " + estimateTime);
				msg = "updateSequenceRoutePath successfully";
			}
			else if(chooseWay.equals("3Messenger"))
			{
				for (int i=0; i<bestTimeOneMessOneMerchantService.getAllRoutePath().size(); i++) {
					RoutePathDetail tmpPath = bestTimeOneMessOneMerchantService.getAllRoutePath().get(i);
					if(tmpPath.getMerList().size() == 1) {
						int idMessenger = tmpPath.getFtID();
						int runningNo = 1;
						Merchants tmpMerchant = tmpPath.getMerList().get(0);
						for(int j = 0;j<seqOrderAndMerchant.size();j++) {
							HashMap<String, Object> tmpSeqOrderMerchant = seqOrderAndMerchant.get(j);
							int seqOrderMerID = (Integer)tmpSeqOrderMerchant.get("SEQOR_MER_ID");
							int seqOrderID = (Integer)tmpSeqOrderMerchant.get("SEQOR_ID");
							if(tmpMerchant.getMerID() == seqOrderMerID) {
								logger.info("1mess. idMessenger" + idMessenger);
								query.updateSequenceOrder(seqOrderID, idMessenger, runningNo);
								fullQuery.updateFulltimeMessengerStatus(orderId, idMessenger);
								runningNo++;
								break;
							}
						}
						BigDecimal value = new BigDecimal(bestTimeOneMessOneMerchantService.getTotalDuration());
						int estimateTime = value.intValue();
						Date currentDateTime = DateTime.getCurrentDateTime();
						Calendar cal = Calendar.getInstance();
						cal.setTime(currentDateTime);
						cal.add(Calendar.MINUTE, estimateTime);
						currentDateTime = cal.getTime();
						
						Orders order = ordersDao.getOrderByOrderId(orderId);
						order.setOrderStatus(VariableText.ORDER_COOKING_STATUS);
						order.setOrderEstimateTime(estimateTime);
						order.setOrderEstimateDatetime(currentDateTime);
						ordersDao.save(order);
						
						logger.info("esimate " + estimateTime);
						msg = "updateSequenceRoutePath successfully";
						
	                    NotificationInbox noti = new NotificationInbox();
	                    noti.setNoti_message_detail("คุณได้รับมอบหมายงานออร์เดอร์รหัส " + orderId + "กรุณากดยืนยันเพื่อเริ่มงาน");
	                    noti.setNoti_ref_id(idMessenger);//
	                    noti.setNoti_message_type(VariableText.NOTIFICATION_MSG_TYPE_REQUEST);
	                    noti.setNoti_read_flag(0);
	                    noti.setNoti_type("Messenger");
	                    noti.setNoti_order_id(orderId);
	                    noti.setNoti_created_date(new Date());
	                    notiDao.save(noti);						
					}
				}
				//Send noti to customer
                NotificationInbox notiToCustomer = new NotificationInbox();
                notiToCustomer.setNoti_message_detail("ออร์เดอร์รหัส " + orderId + " ร้านค้ากำลังทำอาหาร");
                notiToCustomer.setNoti_ref_id(custDao.getCustomerIdByOrderId(orderId));//
                notiToCustomer.setNoti_message_type(VariableText.NOTIFICATION_MSG_TYPE_REQUEST);
                notiToCustomer.setNoti_read_flag(0);
                notiToCustomer.setNoti_type("Customer");
                notiToCustomer.setNoti_order_id(orderId);
                notiToCustomer.setNoti_created_date(new Date());
                notiDao.save(notiToCustomer);					
			}

	  }
	  else
	  {
		  msg = "No update";
	  }
	  return ResponseEntity.ok(new Response<String>(HttpStatus.OK.value(),msg, "Success"));

	 }
	@RequestMapping(value={"service/fulltime/auth"} ,method=RequestMethod.POST)
	public ResponseEntity<Response<FullTimeMessenger>> authen(@RequestBody FullTimeMessenger full){
		List<FullTimeMessenger> fullMess = null;
		HashMap<String,String> resultList = new HashMap<String, String>();

		try {
			logger.info("Check point 0");

			fullMess = fullMessDao.findByFullEmail(full.getFullEmail(),full.getFullPassword());

			logger.info(""+fullMess);

			if(fullMess.size() != 0){
				FullTimeMessenger fullMess1 = fullMess.get(0);
				fullMess1.setFullEmail("");
				fullMess1.setFullPassword("");

				return ResponseEntity.ok(new Response<FullTimeMessenger>(HttpStatus.OK.value(),
						"Login successfully", fullMess1));

			}else{
				logger.info("Check point 1");
				return null;
			}

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
	
	@RequestMapping(value = {"service/fulltime/{fullId}"}, method = RequestMethod.GET)
	public ResponseEntity<Response<Map<String, Object>>>
	getOrderByFullTimeMessenger(@PathVariable("fullId") String fullId) {
		try {
			long fullTimeMessengerId = Long.parseLong(fullId);
			FullTimeMessenger fullTime = fullMessDao.getFullTimeByFullId(fullTimeMessengerId);
			BikeStation bikeStation = fullTime.getBikeStation();
			logger.info("Get Full Time Success : ID "+fullTimeMessengerId);
			List<SequenceOrders> seqOrdersList = seqOrderDao.getSequenceOrderByMessIdAndOrderId(fullTime.getOrder().getOrderId(), fullTimeMessengerId);
			logger.info("Get SeqOrderList : Size "+seqOrdersList.size());
			Orders assignedOrder = fullTime.getOrder();
			logger.info("Assignment Order : ID "+assignedOrder.getOrderId());
			// Mapping Menu with Sequence Order
			List<OrderDetail> orderDetails = assignedOrder.getOrderDetails();
			//Reset Seq and Order Detail
			assignedOrder.setOrderDetails(null);
			List<SequenceOrders> mappedSeqOrdersList = new ArrayList<SequenceOrders>();
			for (SequenceOrders seq:seqOrdersList) {
				for (OrderDetail detail:orderDetails) {
					if (detail.getMerId() == seq.getMerchants().getMerID() ) {
						seq.getOrderDetails().add(detail);
					}
				}
				mappedSeqOrdersList.add(seq);
			}
			assignedOrder.setSequenceOrders(mappedSeqOrdersList);

			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("order",assignedOrder);
			dataMap.put("bikeStation",bikeStation);
			if (seqOrdersList.size() > 0) {
				assignedOrder.setSequenceOrders(seqOrdersList);
				return ResponseEntity.ok(new Response<Map<String, Object>>
						(HttpStatus.OK.value(), "Get data successfully", dataMap));

			}
			return ResponseEntity.ok(new Response<Map<String, Object>>
					(HttpStatus.OK.value(), "Cannot found his assignment", null));
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			return null;
		}
	}

	/*
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
	}*/

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
			logger.info("Station ID: " + bikeStationDistanceSortedList.get(i)[0] +
					" | distance: " + bikeStationDistanceSortedList.get(i)[1] +
					" | number: " + bikeStationDistanceSortedList.get(i)[2]);
		}

		if ((double) bikeStationDistanceSortedList.get(0)[1] < 5.00) {
			for (int i=0; i<bikeStationDistanceSortedList.size(); i++) {
				if (bikeStationDistanceSortedList.get(i)[1] > 5.00) {
					bikeStationDistanceSortedList.remove(i);
				}
			}
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

	@RequestMapping(value="/service/fulltime/accept" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response<String>> acceptOrderMessenger(@RequestBody Map<String, Object> mapRequest)
	{
		String messeage = "";
		try {
			int noti_id = (Integer) mapRequest.get("noti_id");
			int full_id = (Integer) mapRequest.get("full_id");
			String isAccept = (String)mapRequest.get("isAccept");
			if(isAccept.equals("Y"))
			{
				long full_id_L = full_id;
				fullMessDao.updateFullTimeStatus(full_id, VariableText.MESSENGER_RECEIVING_STATUS);
				notiDao.updateNotiReadFlagByNotiId(noti_id);
				messeage = "Messenger accept successfully";
			}
			else
			{
				messeage = "is accept not Y";
			}
		}
		catch (Exception e) {
			logger.info(e.getMessage());
			messeage = "Messenger accept Failed";
			return null;
		}
		return ResponseEntity.ok(new Response<String>(HttpStatus.OK.value(),messeage, messeage));

	}
	
	@Transactional
	@RequestMapping(value={"service/fulltime/backtostation/{fullId}"}, method=RequestMethod.GET)
	public ResponseEntity<Response<String>> backToStation(@PathVariable("fullId") long fullId) {
		String message = "";
		try {
			FullTimeMessenger fullTimeMessenger = fullTimeMessengerDao.findById(fullId);
			if (fullTimeMessenger != null) {
				fullTimeMessenger.setFullStatusId(VariableText.MESSENGER_STATION_STATUS);
				fullTimeMessengerDao.save(fullTimeMessenger);
				message = "Back to station successfully";
			}
			else {
				message = "No update";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			return null;
		}
		
		return ResponseEntity.ok(new Response<String>(HttpStatus.OK.value(), message, null));
	}

}
