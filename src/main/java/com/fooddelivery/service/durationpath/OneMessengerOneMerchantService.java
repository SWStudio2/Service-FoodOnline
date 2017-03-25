package com.fooddelivery.service.durationpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.fooddelivery.Model.BikeStation;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.controller.HomeController;
import com.fooddelivery.util.NodeDetailVer2;

public class OneMessengerOneMerchantService {
	
	int[] merchantsId;
	String latitudeCustomer;
	String longtitudeCustomer;
	
	public OneMessengerOneMerchantService
	(
			int[] merchantsId,
			String latitudeCustomer,
			String longtitudeCustomer
	) {
		this.merchantsId = merchantsId;
		this.latitudeCustomer = latitudeCustomer;
		this.longtitudeCustomer = longtitudeCustomer;
	}
	
	public List<NodeDetailVer2> oneMessengerForOneMerchants(
			List<BikeStation> stationList,
			List<Merchants> merchantsList) {
		List<NodeDetailVer2> result = new ArrayList<NodeDetailVer2>();
		
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
						latitudeCustomer, 
						longtitudeCustomer);
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
			nodeDetail.setLatitudeDelivery(latitudeCustomer);
			nodeDetail.setLongtitudeDelivery(longtitudeCustomer);
			nodeDetail.setDuration(String.valueOf(duration));
			nodeDetail.setDistance(String.valueOf(distance));
			resultAll.add(nodeDetail);
		}
		
		resultAll = sortResult(resultAll);
		result = getBestResult(resultAll, merchantsHash);

		return result;
	}
	
	private HashMap<String, Merchants> convertMerchantsListToHashMap(List<Merchants> merchantsList) {
		HashMap<String, Merchants> result = new HashMap<String, Merchants>();
		for (int i=0; i<merchantsList.size(); i++) {
			result.put(String.valueOf(merchantsList.get(i).getMerID()), merchantsList.get(i));
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

}
