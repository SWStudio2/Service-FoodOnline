package com.fooddelivery.service.durationpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.fooddelivery.Model.BikeStation;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.Station;
import com.fooddelivery.Model.TimeAndDistanceDetail;
import com.fooddelivery.controller.HomeController;
import com.fooddelivery.util.GroupPathDetail;
import com.fooddelivery.util.NodeDetailVer2;
import com.fooddelivery.util.RoutePathDetail;
import com.fooddelivery.util.VariableText;

public class OneMessengerOneMerchantService {
	
	int[] merchantsId;
	String latitudeCustomer;
	String longtitudeCustomer;
	TimeAndDistanceDetail[] timeAndDistanceDetail;
	List<Merchants> merchants;
	List<Station> stations;
	
	public OneMessengerOneMerchantService
	(
			int[] merchantsId,
			String latitudeCustomer,
			String longtitudeCustomer,
			TimeAndDistanceDetail[] timeAndDistanceDetail,
			List<Merchants> merchants,
			List<Station> stations
	) {
		this.merchantsId = merchantsId;
		this.latitudeCustomer = latitudeCustomer;
		this.longtitudeCustomer = longtitudeCustomer;
		this.timeAndDistanceDetail = timeAndDistanceDetail;
		this.merchants = merchants;
		this.stations = stations;
	}
	
	public List<NodeDetailVer2> oneMessengerForOneMerchants(
			List<BikeStation> stationList,
			List<Merchants> merchantsList,
			HashMap<Integer, Double> hashMerCookingTime) {
		List<NodeDetailVer2> result = new ArrayList<NodeDetailVer2>();
		
		HashMap<String, Merchants> merchantsHash = convertMerchantsListToHashMap(merchantsList);
		HomeController homeController = new HomeController();
		List<String[]> stationToMerchantsDetail = new ArrayList<String[]>();
		//get bike_path station -> merchants
		for (int i=0; i<stationList.size(); i++) {
			BikeStation bikeStation = stationList.get(i);
			try {
				Thread.sleep(600);
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
				Thread.sleep(600);
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
				if (hashMerCookingTime != null) {
					durationCookTime = hashMerCookingTime.get(merchant.getMerID());
				}
				//Double durationCookTime = Double.valueOf(merchant.getCookingTime());
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
		
		resultAll = sortResultNodeDetailVer2(resultAll);
		result = getBestResult(resultAll, merchantsHash);

		return result;
	}
	
	public GroupPathDetail oneMessengerForOneMerchants(List<BikeStation> stations) {
		GroupPathDetail result = new GroupPathDetail();
		HomeController homeController = new HomeController();
		HashMap<String, Merchants> merchantsHash = convertMerchantsListToHashMap(this.merchants);
		HashMap<String, BikeStation> bikeStationsHash = convertBikeStationsListToHashMap(stations);
		HashMap<String, Station> stationsHash = convertStationsListToHashMap(this.stations);
		List<String[]> stationToMerchantsDetail = new ArrayList<String[]>();
		
		//get TimeAndDistance ที่เป็น Type Bike
		List<TimeAndDistanceDetail> timeAndDistanceBikeDetailList = new ArrayList<TimeAndDistanceDetail>();
		for (int i=0; i<this.timeAndDistanceDetail.length; i++) {
			if (timeAndDistanceDetail[i].getPathType().trim().toString().equals(VariableText.BIKE_PATH_TYPE)
					&& bikeStationsHash.get(String.valueOf(timeAndDistanceDetail[i].getSourceId())) != null) {
				timeAndDistanceBikeDetailList.add(timeAndDistanceDetail[i]);
			}
		}
		
		//get bike_path station -> merchants
		/*for (int i=0; i<timeAndDistanceBikeDetailList.size(); i++) {
			BikeStation bikeStation = bikeStationsHash.get(String.valueOf(
					timeAndDistanceBikeDetailList.get(i).getSourceId()));
			try {
				Thread.sleep(600);
				for (int j=0; j<merchants.size(); j++) {
					Merchants merchants = this.merchants.get(j);
					String[] detailArray = (String[])homeController.getDistanceDuration(
							bikeStation.getBikeStationLatitude(), 
							bikeStation.getBikeStationLongitude(),
							merchants.getMerLatitude(), 
							merchants.getMerLongtitude());
					String[] detail = {String.valueOf(bikeStation.getBikeStationId()),
							String.valueOf(merchants.getMerID()),
							detailArray[0], 
							detailArray[1]};
					stationToMerchantsDetail.add(detail);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
		
		HashMap<String, String[]> merchantsToCustomerDetail = new HashMap<String, String[]>();
		//get bike_path merchants -> customer
		for (int i=0; i<merchants.size(); i++) {
			Merchants merchants = this.merchants.get(i);
			try {
				Thread.sleep(600);
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
		
		List<RoutePathDetail> arrRoutePathDetail = new ArrayList<RoutePathDetail>();
		for (int i=0; i<timeAndDistanceBikeDetailList.size(); i++) {
			String stationId = String.valueOf(timeAndDistanceBikeDetailList.get(i).getSourceId());
			String stationToMerchantId = String.valueOf(timeAndDistanceBikeDetailList.get(i).getDestinationId());
			Double durationStationToMerchant = Double.valueOf(timeAndDistanceBikeDetailList.get(i).getDuration());
			Double distanceStationToMerchant = Double.valueOf(timeAndDistanceBikeDetailList.get(i).getDistance());
			
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
			
			RoutePathDetail routPathDetail = new RoutePathDetail();
			if (stationsHash.get(stationId) != null) {
				routPathDetail.setStation(stationsHash.get(stationId));
				routPathDetail.setLatitudeDelivery(latitudeCustomer);
				routPathDetail.setLongtitudeDelivery(longtitudeCustomer);
				routPathDetail.setDuration(String.valueOf(duration));
				routPathDetail.setDistance(String.valueOf(distance));
				ArrayList<Merchants> mercList = new ArrayList<Merchants>();
				mercList.add(merchant);
				routPathDetail.setMerList(mercList);
				arrRoutePathDetail.add(routPathDetail);
			}
		}
		
		List<RoutePathDetail> resultRoutePathDetail = sortResultRoutePathDetail(arrRoutePathDetail);
		result = getBestResultRoutePathDetail(result, resultRoutePathDetail, merchantsHash);
		ArrayList<RoutePathDetail> bestResultRoute = result.getAllRoutePath();
		String totalDistance = bestResultRoute.get(bestResultRoute.size()-1).getDistance();
		String totalDuration = bestResultRoute.get(bestResultRoute.size()-1).getDuration();
		result.setTotalDistance(totalDistance);
		result.setTotalDuration(totalDuration);
		
		return result;
	}
	
	private HashMap<String, Merchants> convertMerchantsListToHashMap(List<Merchants> merchantsList) {
		HashMap<String, Merchants> result = new HashMap<String, Merchants>();
		for (int i=0; i<merchantsList.size(); i++) {
			result.put(String.valueOf(merchantsList.get(i).getMerID()), merchantsList.get(i));
		}
		return result;
	}
	
	private HashMap<String, BikeStation> convertBikeStationsListToHashMap(List<BikeStation> stationList) {
		HashMap<String, BikeStation> result = new HashMap<String, BikeStation>();
		for (int i=0; i<stationList.size(); i++) {
			result.put(String.valueOf(stationList.get(i).getBikeStationId()), stationList.get(i));
		}
		return result;
	}
	
	private HashMap<String, Station> convertStationsListToHashMap(List<Station> stationList) {
		HashMap<String, Station> result = new HashMap<String, Station>();
		for (int i=0; i<stationList.size(); i++) {
			result.put(String.valueOf(stationList.get(i).getStationId()), stationList.get(i));
		}
		return result;
	}
	
	private List<NodeDetailVer2> sortResultNodeDetailVer2(List<NodeDetailVer2> nodeDetailVer2) {
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
	
	private List<RoutePathDetail> sortResultRoutePathDetail(List<RoutePathDetail> routePathDetail) {
		Collections.sort(routePathDetail, new Comparator() {
			public int compare(Object o1, Object o2) {
				Double duration1 = Double.valueOf(((RoutePathDetail) o1).getDuration());
				Double duration2 = Double.valueOf(((RoutePathDetail) o2).getDuration());
				double sComp = duration1.compareTo(duration2);
				
				if (sComp != 0) {
					return (int) sComp;
				}
				else {
					Double distance1 = Double.valueOf(((RoutePathDetail) o1).getDistance());
					Double distance2 = Double.valueOf(((RoutePathDetail) o2).getDistance());
					return distance1.compareTo(distance2);
				}
			}
		});
		return routePathDetail;
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
	
	private GroupPathDetail getBestResultRoutePathDetail(
			GroupPathDetail groupPathDetail,
			List<RoutePathDetail> routePathDetail, 
			HashMap<String, Merchants> merchantsHash) {
		//ArrayList<RoutePathDetail> result = new ArrayList<RoutePathDetail>();
		
		for (int i=0; i<routePathDetail.size(); i++) {
			String merId = String.valueOf(routePathDetail.get(i).getMerList().get(0).getMerID());
			Merchants merchant = merchantsHash.get(merId);
			if (merchant != null) {
				groupPathDetail.addRoutePathDetail(routePathDetail.get(i));
				//result.add(routePathDetail.get(i));
				merchantsHash.remove(merId);
			}
			else {
			}
			if (merchantsHash.isEmpty()) {
				break;
			}
		}
		
		return groupPathDetail;
	}

}
