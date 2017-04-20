package com.fooddelivery.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.Model.BikeStation;
import com.fooddelivery.Model.BikeStationDao;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsDao;
import com.fooddelivery.service.durationpath.OneMessengerOneMerchantService;
import com.fooddelivery.util.NodeDetailVer2;

@RestController
public class DurationPathController {
	
	Logger logger = LoggerFactory.getLogger(DurationPathController.class);

	@Autowired
	private MerchantsDao merchantsDao;
	@Autowired
	private BikeStationDao bikeStationDao;

	@RequestMapping(value={"/service/durationPathAndDeliveryFeeResult"} ,method=RequestMethod.POST)
	@ResponseBody
	public List<NodeDetailVer2> durationPathAndDeliveryFeeResult(
			@RequestParam("merchants") int[] merchantsId,
			@RequestParam("cusLatitude") String cusLatitude,
			@RequestParam("cusLongtitude") String cusLongtitude){ 
		
		/*
		 * method that calculate TimeAndDistanceAllPath
		 * 1 mess for 1 mer
		 * 2 mess
		 * 3 mess ...
		 */
		try {
			String latCustomer = cusLatitude;/*"13.7033152";*/
			String longCustomer = cusLongtitude;/*"100.5023999";*/
			//int amountMerchants = merchantsId.length;
			
			List<Integer> merchantsIdList = convertArrayToListInteger(merchantsId);
			//info Merchants
			List<Merchants> merchantsList;
			//if (merchantsId.size() == 1) {
			//	merchantsList = merchantsDao.getMerchantByMerId(String.valueOf(merchantsId.get(0))/*merchantsIdList*/);
			//}
			//else {
				merchantsList = merchantsDao.getMerchantsByMerIds(merchantsIdList/*merchantsIdList*/);
			//}
			//logger.info(merchantsList.size());
			HashMap<String, Merchants> merchantsHash = convertMerchantsListToHashMap(merchantsList);
			
			//get Station
			List<BikeStation> stationList = bikeStationDao.getBikeStationAvailable();
			//logger.info(stationList.size());
			
			/*
			 * Algorithm choose
			 * */
			OneMessengerOneMerchantService durationPathService = new OneMessengerOneMerchantService(merchantsId, latCustomer, longCustomer, null, null, null);
			List<NodeDetailVer2> result = durationPathService.oneMessengerForOneMerchants(stationList, merchantsList);

			logger.info("**************************************");
			logger.info("***************RESULT*****************");
			logger.info("**************************************");
			printResult(result);
			return result;
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private HashMap<String, Merchants> convertMerchantsListToHashMap(List<Merchants> merchantsList) {
		HashMap<String, Merchants> result = new HashMap<String, Merchants>();
		for (int i=0; i<merchantsList.size(); i++) {
			result.put(String.valueOf(merchantsList.get(i).getMerID()), merchantsList.get(i));
		}
		return result;
	}
	
	private void printResult(List<NodeDetailVer2> result) {
		for (int i=0; i<result.size(); i++) {
		    logger.info("Path: " + result.get(i).getStation() + "-"
		    		+ result.get(i).getMerList().get(0).getMerID() + "-"
		    		+ "Customer");
		    logger.info("Duration: " + result.get(i).getDuration());
		    logger.info("Distance: " + result.get(i).getDistance());
		    logger.info("----------------------------------");
		}
	}
	
	private List<Integer> convertArrayToListInteger(int[] input) {
		List<Integer> result = new ArrayList<Integer>();
		for (int i=0; i<input.length; i++) {
			result.add(input[i]);
		}
		return result;
	}
}
