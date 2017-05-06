package com.fooddelivery.service.durationpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.Model.BikeStation; 
import com.fooddelivery.Model.FullTimeMessengerDao;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.Station;
import com.fooddelivery.Model.TimeAndDistanceDetail;
import com.fooddelivery.controller.HomeController;
import com.fooddelivery.util.GroupPathDetail;
import com.fooddelivery.util.NodeDetailVer2;
import com.fooddelivery.util.RoutePathDetail;
import com.fooddelivery.util.VariableText;
import com.fooddelivery.util.model.Location;

@RestController
public class OneMessengerOneMerchantService {
	private static final Logger logger = LoggerFactory.getLogger(OneMessengerOneMerchantService.class);
	
	@Autowired
	private FullTimeMessengerDao fullTimeDao;
	
	//private String latitudeCustomer;
	//private String longtitudeCustomer;
	private Location locationCustomer;
	private TimeAndDistanceDetail[] timeAndDistanceDetail;
	private List<Merchants> merchants;
	private List<Station> stations;
	private List<BikeStation> bikeStations;
	private HashMap<Integer, Double> hashMerCookingTime;
	private List<Object[]> fullTimeMessengerInStation;
	
	@RequestMapping(value="/service/OneMessengerOneMerchantService", method=RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public GroupPathDetail oneMessengerOneMerchantService(
			Location locationCustomer,
			TimeAndDistanceDetail[] timeAndDistanceDetail,
			List<Merchants> merchants,
			List<Station> stations,
			List<BikeStation> bikeStations,
			HashMap<Integer, Double> hashMerCookingTime,
			List<Object[]> fullTimeMessengerInStation,
			FullTimeMessengerDao fullTimeDao
	) throws InterruptedException {	
		this.locationCustomer = locationCustomer;
		this.timeAndDistanceDetail = timeAndDistanceDetail;
		this.merchants = merchants;
		this.stations = stations;
		this.bikeStations = bikeStations;
		this.hashMerCookingTime = hashMerCookingTime;
		this.fullTimeMessengerInStation = fullTimeMessengerInStation;
		this.fullTimeDao = fullTimeDao;
		
		return oneMessengerForOneMerchants(bikeStations, hashMerCookingTime, fullTimeMessengerInStation);
	}
	
	public OneMessengerOneMerchantService
	(
			/*Location locationCustomer,
			TimeAndDistanceDetail[] timeAndDistanceDetail,
			List<Merchants> merchants,
			List<Station> stations*/
	) {
		/*this.locationCustomer = locationCustomer;
		this.timeAndDistanceDetail = timeAndDistanceDetail;
		this.merchants = merchants;
		this.stations = stations;*/
	}
	
	/*public List<NodeDetailVer2> oneMessengerForOneMerchants(
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
/*					stationToMerchantsDetail.add(detail);
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
	}*/
	
	public GroupPathDetail oneMessengerForOneMerchants(
			List<BikeStation> stations,
			HashMap<Integer, Double> hashMerCookingTime,
			List<Object[]> fullTimeMessengerInStation) {
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
						locationCustomer.getLatitude(), //latitudeCustomer, 
						locationCustomer.getLongtitude()); //longtitudeCustomer);
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
				if (hashMerCookingTime != null) {
					durationCookTime = hashMerCookingTime.get(merchant.getMerID());
				}
				
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
				routPathDetail.setLatitudeDelivery(locationCustomer.getLatitude());//latitudeCustomer);
				routPathDetail.setLongtitudeDelivery(locationCustomer.getLongtitude());//longtitudeCustomer);
				routPathDetail.setDuration(String.valueOf(duration));
				routPathDetail.setDistance(String.valueOf(distance));
				//*****************ADD FtID********************************
				//after sort
				ArrayList<Merchants> mercList = new ArrayList<Merchants>();
				mercList.add(merchant);
				routPathDetail.setMerList(mercList);
				arrRoutePathDetail.add(routPathDetail);
			}
		}
		
		List<RoutePathDetail> resultRoutePathDetail = sortResultRoutePathDetail(arrRoutePathDetail);
		/*
		 * add mess id
		 */
		//FullTime Available
		//fullTimeMessengerInStation = fullTimeMessengerDao.getNumberOfMessengerInStation();
		HashMap<String, Integer> newFullTimeAvailableHash = new HashMap<String, Integer>();
		if (fullTimeMessengerInStation != null) {
			HashMap<String, String[]> numberFullTimeAvailableInStationHash = convertNumberMessengerInStationListToHashMap(
					fullTimeMessengerInStation);
			HashMap<String, String[]> numberFullTimeAvailableInStationHashLeft = numberFullTimeAvailableInStationHash;
			for (int i=0; i<resultRoutePathDetail.size(); i++) {
				RoutePathDetail routePathDetail = resultRoutePathDetail.get(i);
				String[] numberAvailable = numberFullTimeAvailableInStationHashLeft.get(
						String.valueOf(routePathDetail.getStation().getStationId()));
				/*if (Integer.valueOf(numberFullTimeAvailableInStationHashLeft.get(
						String.valueOf(routePathDetail.getMerList().get(0).getMerID()))[1])
						!= 0) {*/
				if (Integer.valueOf(numberAvailable[1]) != 0) {
					int k = 0;
					if (newFullTimeAvailableHash.get(String.valueOf(routePathDetail.getStation().getStationId())) != null) {
						k = newFullTimeAvailableHash.get(String.valueOf(
								routePathDetail.getStation().getStationId()));
					}
					int ftID = fullTimeDao.getFulltimeMessengerFreeByStationID(Long.valueOf(
							routePathDetail.getStation().getStationId())).get(k);
					newFullTimeAvailableHash.put(String.valueOf(routePathDetail.getStation().getStationId()), k+1);
					routePathDetail.setFtID(ftID);
					resultRoutePathDetail.remove(i);
					resultRoutePathDetail.add(i,routePathDetail);
					String[] merchantAndNumberAvailableMessenger = numberFullTimeAvailableInStationHashLeft.get(
							String.valueOf(routePathDetail.getStation().getStationId()));
					int numberAvailableMessenger = Integer.valueOf(merchantAndNumberAvailableMessenger[1]);
					numberAvailableMessenger = numberAvailableMessenger - 1;
					merchantAndNumberAvailableMessenger[1] = String.valueOf(numberAvailableMessenger);
					numberFullTimeAvailableInStationHashLeft.put(
							String.valueOf(routePathDetail.getStation().getStationId()), 
							merchantAndNumberAvailableMessenger);
				}
				else {
					resultRoutePathDetail.remove(i);
				}
			}
		}
		for (int i=0; i<resultRoutePathDetail.size(); i++) {
			System.out.println("Merchant: " + resultRoutePathDetail.get(i).getMerList().get(0).getMerName()
					+ " | Messenger Id: " + resultRoutePathDetail.get(i).getFtID());
		}
		
		result = getBestResultRoutePathDetail(result, resultRoutePathDetail, merchantsHash);
		System.out.println("--------");
		for (int i=0; i<result.getAllRoutePath().size(); i++) {
			RoutePathDetail route = result.getAllRoutePath().get(i);
			System.out.println("Merchant: " + route.getMerList().get(0).getMerName()
					+ " | Messenger Id: " + route.getFtID());
		}
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
	
	private HashMap<String, String[]> convertNumberMessengerInStationListToHashMap(List<Object[]> numberMessengerInStation) {
		HashMap<String, String[]> result = new HashMap<String, String[]>();
		for (int i=0; i<numberMessengerInStation.size(); i++) {
			String[] intArray = new String[]{numberMessengerInStation.get(i)[0] + "",
					numberMessengerInStation.get(i)[1] + ""};
			result.put(String.valueOf(numberMessengerInStation.get(i)[0]), intArray);
		}
		return result;
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
