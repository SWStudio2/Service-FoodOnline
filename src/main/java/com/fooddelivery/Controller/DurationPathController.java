package com.fooddelivery.Controller;

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
import com.fooddelivery.util.NodeDetailVer2;

@RestController
public class DurationPathController {
	
	Logger logger = LoggerFactory.getLogger(DurationPathController.class);

	@Autowired
	private MerchantsDao merchantsDao;
	@Autowired
	private BikeStationDao bikeStationDao;
	
	@RequestMapping(value={"/servicetest/durationPathAndDeliveryFeeResult"} ,method=RequestMethod.POST)
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
			
			List<Integer> merchantsIdList = new ArrayList<Integer>();
			for (int i=0; i<merchantsId.length; i++) {
				merchantsIdList.add(merchantsId[i]);
			}
			
			//info Merchants
			List<Merchants> merchantsList = merchantsDao.getMerchantsByMerIds(merchantsIdList);
			//System.out.println(merchantsList.size());
			HashMap<String, Merchants> merchantsHash = convertMerchantsListToHashMap(merchantsList);
			
			
			//get Station
			List<BikeStation> stationList = bikeStationDao.getBikeStationAvailable();
			//System.out.println(stationList.size());
			
			List<NodeDetailVer2> resultAll = getOneMessForOneMerchant(
					latCustomer, longCustomer, stationList, merchantsList);

			resultAll = sortResult(resultAll);
			printResult(resultAll);
			List<NodeDetailVer2> result = getBestResult(resultAll, merchantsHash);
			System.out.println("**************************************");
			System.out.println("***************RESULT*****************");
			System.out.println("**************************************");
			printResult(result);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private List<NodeDetailVer2> getBestResult(List<NodeDetailVer2> nodeDetailVer2, HashMap<String, Merchants> merchantsHash) {
		List<NodeDetailVer2> result = new ArrayList<NodeDetailVer2>();
		
		for (int i=0; i<nodeDetailVer2.size(); i++) {
			String merId = String.valueOf(nodeDetailVer2.get(i).getMerList().get(0).getMerID());
			Merchants merchant = merchantsHash.get(merId);
			if (merchant != null) {
				result.add(nodeDetailVer2.get(i));
				merchantsHash.remove(merId);
			}
			else {
			}
			if (merchantsHash.isEmpty()) {
				break;
			}
		}
		
		return result;
	}
	
	private List<NodeDetailVer2> sortResult(List<NodeDetailVer2> nodeDetailVer2) {
		Collections.sort(nodeDetailVer2, new Comparator() {
			public int compare(Object o1, Object o2) {
				Double duration1 = Double.valueOf(((NodeDetailVer2) o1).getDuration());
				Double duration2 = Double.valueOf(((NodeDetailVer2) o2).getDuration());
				double sComp = duration1.compareTo(duration2);
				
				if (sComp != 0) {
					return (int) sComp;
				}
				else {
					Double distance1 = Double.valueOf(((NodeDetailVer2) o1).getDistance());
					Double distance2 = Double.valueOf(((NodeDetailVer2) o2).getDistance());
					return distance1.compareTo(distance2);
				}
			}
		});
		return nodeDetailVer2;
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
		    System.out.println("Duaration: " + result.get(i).getDuration());
		    System.out.println("Distance: " + result.get(i).getDistance());
		    System.out.println("----------------------------------");
		}
	}
	
	private List<NodeDetailVer2> getOneMessForOneMerchant
	(
			String latCustomer,
			String longCustomer,
			List<BikeStation> stationList,
			List<Merchants> merchantsList
	) 
	{
		HashMap<String, Merchants> merchantsHash = convertMerchantsListToHashMap(merchantsList);
		HomeController homeController = new HomeController();
		List<String[]> stationToMerchantsDetail = new ArrayList<String[]>();
		//get bike_path station -> merchants
		for (int i=0; i<stationList.size(); i++) {
			BikeStation bikeStation = stationList.get(i);
			try {
				Thread.sleep(1000);
				for (int j=0; j<merchantsList.size(); j++) {
					Merchants merchants = merchantsList.get(j);
					String[] detailArray = (String[])homeController.getDistanceDuration(
							bikeStation.getBikeStationLatitude(), 
							bikeStation.getBikeStationLongitude(),
							merchants.getMerLatitude(), 
							merchants.getMerLongtitude());
					String[] detail = {String.valueOf(bikeStation.getBikeStationId()),
							String.valueOf(merchants.getMerID()),
							detailArray[0], 
							detailArray[1]};
					/*stationToMerchantsDetail.put(bikeStation.getBikeStationId() + "-" + merchants.getMerID(),
							detail);*/
					stationToMerchantsDetail.add(detail);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		HashMap<String, String[]> merchantsToCustomerDetail = new HashMap<String, String[]>();
		//get bike_path merchants -> customer
		for (int i=0; i<merchantsList.size(); i++) {
			Merchants merchants = merchantsList.get(i);
			try {
				Thread.sleep(1000);
				String[] detailArray = (String[]) homeController.getDistanceDuration(
						merchants.getMerLatitude(), 
						merchants.getMerLongtitude(), 
						latCustomer, 
						longCustomer);
				String[] detail = {String.valueOf(merchants.getMerID()),
						detailArray[0], detailArray[1]};
				merchantsToCustomerDetail.put(String.valueOf(merchants.getMerID()), detail);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		List<NodeDetailVer2> resultAll = new ArrayList<NodeDetailVer2>();
		for (int i=0; i<stationToMerchantsDetail.size(); i++) {
			String stationId = stationToMerchantsDetail.get(i)[0];
			String stationToMerchantId = stationToMerchantsDetail.get(i)[1];
			Double durationStationToMerchant = Double.valueOf(stationToMerchantsDetail.get(i)[3]);
			Double distanceStationToMerchant = Double.valueOf(stationToMerchantsDetail.get(i)[2]);
			
			//station->mer = path1 ---- compare with cookTime
			Merchants merchant = merchantsHash.get(stationToMerchantId);
			Double durationPath1 = Double.valueOf(0);
			if ( merchant != null) {
				Double durationCookTime = Double.valueOf(merchant.getCookingTime());
				if (durationStationToMerchant > durationCookTime)
					durationPath1 = durationStationToMerchant;
				else durationPath1 = durationCookTime;
			}
			Double distancePath1 = distanceStationToMerchant;
			
			//mer->customer = path2
			String[] merchantToCustomerDetail = merchantsToCustomerDetail.get(stationToMerchantId);
			Double durationPath2 = Double.valueOf(merchantToCustomerDetail[2]);
			Double distancePath2 = Double.valueOf(merchantToCustomerDetail[1]);
			
			//Path1 + Path2
			Double duration = durationPath1 + durationPath2;
			Double distance = distancePath1 + distancePath2;
			
			NodeDetailVer2 nodeDetail = new NodeDetailVer2();
			nodeDetail.setStation(Integer.valueOf(stationId));
			ArrayList<Merchants> mercList = new ArrayList<Merchants>();
			mercList.add(merchant);
			nodeDetail.setMerList(mercList);
			nodeDetail.setLatitudeDelivery(latCustomer);
			nodeDetail.setLongtitudeDelivery(longCustomer);
			nodeDetail.setDuration(String.valueOf(duration));
			nodeDetail.setDistance(String.valueOf(distance));
			resultAll.add(nodeDetail);
		}
		return resultAll;
	}

}
