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
	public void durationPathAndDeliveryFeeResult(@RequestParam("merchants") int[] merchantsId){ 
		
		/*
		 * method that calculate TimeAndDistanceAllPath
		 * 1 mess for 1 mer
		 * 2 mess
		 * 3 mess ...
		 */
		try {
			String latCustomer = "13.7033152";
			String longCustomer = "100.5023999";
			int amountMerchants = merchantsId.length;
			
			List<Integer> merchantsIdList = convertArrayToListInteger(merchantsId);
			//info Merchants
			List<Merchants> merchantsList = merchantsDao.getMerchantsByMerIds(merchantsIdList);
			//System.out.println(merchantsList.size());
			HashMap<String, Merchants> merchantsHash = convertMerchantsListToHashMap(merchantsList);
			
			//get Station
			List<BikeStation> stationList = bikeStationDao.getBikeStationAvailable();
			//System.out.println(stationList.size());
			
			/*
			 * Algorithm choose
			 * */
			OneMessengerOneMerchantService durationPathService = new OneMessengerOneMerchantService(merchantsId, latCustomer, longCustomer);
			List<NodeDetailVer2> result = durationPathService.oneMessengerForOneMerchants(stationList, merchantsList);

			System.out.println("**************************************");
			System.out.println("***************RESULT*****************");
			System.out.println("**************************************");
			printResult(result);
			
		}
		catch (Exception e) {
			e.printStackTrace();
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
		    System.out.println("Path: " + result.get(i).getStation() + "-"
		    		+ result.get(i).getMerList().get(0).getMerID() + "-"
		    		+ "Customer");
		    System.out.println("Duration: " + result.get(i).getDuration());
		    System.out.println("Distance: " + result.get(i).getDistance());
		    System.out.println("----------------------------------");
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
