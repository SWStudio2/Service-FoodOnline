package com.fooddelivery.service.durationpath;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fooddelivery.Model.*;
import com.fooddelivery.controller.HomeController;
import com.fooddelivery.util.GroupPathDetail;
import com.fooddelivery.util.RoutePathDetail;
import com.fooddelivery.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TwoMessThreeMercService {
	private static final Logger logger = LoggerFactory.getLogger(TwoMessThreeMercService.class);
	
	@Autowired
	private BikePathDao bikePathDao;
	
	@Autowired
	private FullTimeMessengerDao fullTimeMessDao;
	
	@Autowired
	private StationDao stationDao;
	
	private HashMap<Integer, Double[]> mapMercDistDura = new HashMap<Integer, Double[]>();
	private List<Station> stationList = new ArrayList<Station>();
	private HashMap<Integer, Integer> amtFTMap = new HashMap<Integer, Integer>();
	
	@RequestMapping(value="/service/TwoMessThreeMercService", method=RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public GroupPathDetail twoMessThreeMercService(@RequestBody Map<String, Object> mapRequest) throws InterruptedException {	
		
		//Define variable from mapRequest
		ArrayList<Integer> merIdArray = (ArrayList<Integer>) mapRequest.get("merIdArray");
		String cus_Latitude = (String) mapRequest.get("lat");
		String cus_Longtitude = (String) mapRequest.get("lng");
		System.out.println("MerIDArray " +merIdArray);
		System.out.println("cus_Latitude " +cus_Latitude);
		System.out.println("cus_Longtitude " +cus_Longtitude);
		return twoMessThreeMercService(merIdArray, cus_Latitude, cus_Longtitude);
	}
	 
	//not a service
	public GroupPathDetail twoMessThreeMercService(List<Integer> merIdArray, String cus_Latitude, String cus_Longtitude) throws InterruptedException
	{
		//create Dao for query
		MerchantsQuery merDao = new MerchantsQuery();
		
		//get station from database
		StationQuery stationQue = new StationQuery();
		Station[] staList = stationQue.getStationAvailable();
		
		//get Merchant List from merID from Database 
		String merIdAdjust = "";
		for(int i = 0;i<merIdArray.size();i++)
		{
			if (i != 0) {
				merIdAdjust += ",";
			}
			merIdAdjust += merIdArray.get(i);
			
		}
		System.out.println("merIdAdjust" + merIdAdjust);
		//===================== For Test ===========================
//		List<Integer> merId = new ArrayList<Integer>();
//		merId.add(1);
//		merId.add(2);
//		merId.add(3);
//		merIdAdjust = "1,2,3";
		
		Merchants[] merList = merDao.queryMerChantByID(merIdAdjust);
		HomeController home = new HomeController();
		
		//clear hashmap for new customer
		mapMercDistDura.clear();
		for(int i=0;i<merList.length;i++){
			String[] distDuraStr = home.getDistanceDuration(merList[i].getMerLatitude(), merList[i].getMerLongtitude(), cus_Latitude, cus_Longtitude);
			Double[] distDuraD = new Double[2];
			Double distD = Double.valueOf(distDuraStr[0]);
			Double duraD = Double.valueOf(distDuraStr[1]);
			distDuraD[0] = distD;
			distDuraD[1] = duraD;
			mapMercDistDura.put(merList[i].getMerID(), distDuraD);
			System.out.println("Merchant to customer[Google]"+distD +" "+duraD + " " + mapMercDistDura.size());
		}
		
		
		//create copy of merId
		ArrayList<Integer> indexPos = new ArrayList<Integer>();
		for(int i = 0;i<merIdArray.size();i++)
		{
			indexPos.add(i);
		}
		
		//[0,1,2] ==> [[0,1,2],[1,0,2],[0,2,1],...]
		Object[] resultSet = (Object[])Utils.listPermutations(indexPos).toArray();
		
		//[[a,b,c],[b,c,a],[a,c,b],...] ==> [ "a,b,c", "b,c,a", "a,c,b"]
		ArrayList<GroupPathDetail> groupPathList = new ArrayList<GroupPathDetail>();
		
		//get amount fulltime each station
		stationList = getStationFreeMess();
		
		//Loop resultSet and station for set value to GroupPathDetail
		for(int i = 0;i<resultSet.length;i++)
		{
			for(int j=0;j<stationList.size();j++){
				String tmpValue = resultSet[i].toString();
				tmpValue = tmpValue.replace("[", "");
				tmpValue = tmpValue.replace("]", "");
				
				//{"0","1","2"}
				String[] result =  tmpValue.split(",");
				int indexMer0 = Integer.valueOf(result[0].trim());
				int indexMer1 = Integer.valueOf(result[1].trim());
				int indexMer2 = Integer.valueOf(result[2].trim());
				
				Merchants merc0 = merList[indexMer0];
				Merchants merc1 = merList[indexMer1];
				Merchants merc2 = merList[indexMer2];
				
				//set Merchants List size 2
				ArrayList<Merchants> mercList1 = new ArrayList<Merchants>();
				mercList1.add(merc0);
				mercList1.add(merc1);
				
				//set Merchants List size 1
				ArrayList<Merchants> mercList2 = new ArrayList<Merchants>();
				mercList2.add(merc2);
				
				//set RoutePathDetail of 2 Merchant
				RoutePathDetail routePath1 = new RoutePathDetail();
				routePath1.setMerList(mercList1);
				routePath1.setStation(stationList.get(j));
				routePath1.setLatitudeDelivery(cus_Latitude);
				routePath1.setLongtitudeDelivery(cus_Longtitude);
				
				//set RoutePathDetail of 1 Merchant
				RoutePathDetail routePath2 = new RoutePathDetail();
				routePath2.setMerList(mercList2);
				routePath2.setStation(stationList.get(j));
				routePath2.setLatitudeDelivery(cus_Latitude);
				routePath2.setLongtitudeDelivery(cus_Longtitude);
				
				//set 2 RoutePathDetail into GroupPathDetail
				GroupPathDetail groupPath = new GroupPathDetail();
				groupPath.addRoutePathDetail(routePath1);
				groupPath.addRoutePathDetail(routePath2);
				
				//add groupPath into GroupPathList
				groupPathList.add(groupPath);
			}
//			for(int j=0;j<staList.length;j++){
//				String tmpValue = resultSet[i].toString();
//				tmpValue = tmpValue.replace("[", "");
//				tmpValue = tmpValue.replace("]", "");
//				
//				//{"0","1","2"}
//				String[] result =  tmpValue.split(",");
//				int indexMer0 = Integer.valueOf(result[0].trim());
//				int indexMer1 = Integer.valueOf(result[1].trim());
//				int indexMer2 = Integer.valueOf(result[2].trim());
//				
//				Merchants merc0 = merList[indexMer0];
//				Merchants merc1 = merList[indexMer1];
//				Merchants merc2 = merList[indexMer2];
//				
//				//set Merchants List size 2
//				ArrayList<Merchants> mercList1 = new ArrayList<Merchants>();
//				mercList1.add(merc0);
//				mercList1.add(merc1);
//				
//				//set Merchants List size 1
//				ArrayList<Merchants> mercList2 = new ArrayList<Merchants>();
//				mercList2.add(merc2);
//				
//				//set RoutePathDetail of 2 Merchant
//				RoutePathDetail routePath1 = new RoutePathDetail();
//				routePath1.setMerList(mercList1);
//				routePath1.setStation(staList[j]);
//				routePath1.setLatitudeDelivery(cus_Latitude);
//				routePath1.setLongtitudeDelivery(cus_Longtitude);
//				
//				//set RoutePathDetail of 1 Merchant
//				RoutePathDetail routePath2 = new RoutePathDetail();
//				routePath2.setMerList(mercList2);
//				routePath2.setStation(staList[j]);
//				routePath2.setLatitudeDelivery(cus_Latitude);
//				routePath2.setLongtitudeDelivery(cus_Longtitude);
//				
//				//set 2 RoutePathDetail into GroupPathDetail
//				GroupPathDetail groupPath = new GroupPathDetail();
//				groupPath.addRoutePathDetail(routePath1);
//				groupPath.addRoutePathDetail(routePath2);
//				
//				//add groupPath into GroupPathList
//				groupPathList.add(groupPath);
//			}
 		}
		
		// set total distance and total duration of each GroupPath
		GroupPathDetail BestGroupPath = null;
		
		if(groupPathList != null && groupPathList.size() != 0){
			//Loop routePathList of each GroupPath
			for(int i=0;i<groupPathList.size();i++){
//				if(i==6){
				List<RoutePathDetail> routePathList = null;
				if(groupPathList.get(i) != null && groupPathList.get(i).getAllRoutePath() != null){
						
						routePathList = groupPathList.get(i).getAllRoutePath();
						//cal duration and distance
						System.out.println("GroupPath : "+ i);
						routePathList = calDuraAndDist(routePathList);
						

						logger.info("GroupPath : "+i+" Distance : "+routePathList.get(0).getDistance()+" Duration : "+routePathList.get(0).getDuration());
						logger.info("GroupPath : "+i+" Distance : "+routePathList.get(1).getDistance()+" Duration : "+routePathList.get(1).getDuration());
						logger.info("=========================================================================");

						System.out.println("GroupPath : "+i+" Distance : "+routePathList.get(0).getDistance()+" Duration : "+routePathList.get(0).getDuration());
						System.out.println("GroupPath : "+i+" Distance : "+routePathList.get(1).getDistance()+" Duration : "+routePathList.get(1).getDuration());
						System.out.println("=========================================================================");

						
						//cal total distance and duration of this GroupPath
						double totalDist = Double.valueOf(routePathList.get(0).getDistance()) + Double.valueOf(routePathList.get(1).getDistance());
						double duraMessOne = Double.valueOf(routePathList.get(0).getDuration());
						double duraMessTwo	= Double.valueOf(routePathList.get(1).getDuration());
						double totalDura = 0;
						
						if(duraMessOne > duraMessTwo){
							totalDura = duraMessOne;
						}else{
							totalDura = duraMessTwo;
						}
						
						//set total distance and duration in GroupPath
						groupPathList.get(i).setTotalDistance(String.valueOf(totalDist));
						
						groupPathList.get(i).setTotalDuration(String.valueOf(totalDura));
						
//						logger.info("groupPathList : "+i+" "+String.valueOf(totalDist)+","+String.valueOf(totalDura));
				}
//				}
				//TimeUnit.SECONDS.sleep(2);
				
			}
			
			//get Best Case
			BestGroupPath = getBestGroupPath(groupPathList);
			
			//sort duration of each groupPath
//			Collections.sort(groupPathList);
			
//			checkFreeFT(groupPathList.get(0));
		}
		
		//return best node's time estimate
		return BestGroupPath;
	}
	
	private void idSameStation(GroupPathDetail gpDetail) {
		int idR1 = gpDetail.getAllRoutePath().get(0).getStation().getStationId();
		int idR2 = gpDetail.getAllRoutePath().get(1).getStation().getStationId();
		
		if(idR1 == idR2){
			if(amtFTMap.get(idR1) >= 2){
				//do nothing
			}else{
				//find next best GroupPath that use other station
			}
		}

	}

	/**
	 * cal duration and distance of each routePathDetail
	 * @param routePathList
	 * @return
	 */
	public List<RoutePathDetail> calDuraAndDist(List<RoutePathDetail> routePathList) throws InterruptedException {
		HomeController home = new HomeController();
		List<RoutePathDetail> routePathListClone = routePathList;
		
		String duraTwoMerRoutePath = "";
		String distTwoMerRoutePath = "";
				
		for(int i=0;i<routePathList.size();i++){
			System.out.println("RoutPath [" + i + "]");
			int staId = routePathListClone.get(i).getStation().getStationId();
			//For call Google Map API
			String staLat = routePathListClone.get(i).getStation().getStationLantitude();
			String staLng = routePathListClone.get(i).getStation().getStationLongtitude();
			
			if(i == 0){
				
				int merOneId = routePathListClone.get(i).getMerList().get(0).getMerID();
				int merTwoId = routePathListClone.get(i).getMerList().get(1).getMerID();
				//For call Google Map API
				String merOneLat = routePathListClone.get(i).getMerList().get(0).getMerLatitude();
				String merOneLng = routePathListClone.get(i).getMerList().get(0).getMerLongtitude();
				String merTwoLat = routePathListClone.get(i).getMerList().get(1).getMerLatitude();
				String merTwoLng = routePathListClone.get(i).getMerList().get(1).getMerLongtitude();
				
				//For set distance and duration of three path
				Double[] duraDistFirstPath = new Double[2];
				Double[] duraDistSecPath = new Double[2];
				Double[] duraDistLastPath = new Double[2];
				
				//Cal First Path 
				System.out.println(staId +" "+merOneId);
				BikePath firstPath = bikePathDao.findBikePathFromId(staId, merOneId , "bike");
				System.out.println("firstPath "+firstPath);
				
				if(firstPath != null){
					duraDistFirstPath[0] = firstPath.getBike_path_distance();
					duraDistFirstPath[1] = firstPath.getBike_path_duration();
				}else{
					String[] duraDistFirstPathStr = home.getDistanceDuration(staLat, staLng, merOneLat, merOneLng);
					BikePath newBikePath = new BikePath();
					newBikePath.setBike_path_source_id(staId);
					newBikePath.setBike_path_destination_id(merOneId);
					newBikePath.setBike_path_type("bike");
					newBikePath.setBike_path_distance(Double.valueOf(duraDistFirstPathStr[0]));
					newBikePath.setBike_path_duration(Double.valueOf(duraDistFirstPathStr[1]));
					bikePathDao.save(newBikePath);
					System.out.println("firduraDistFirstPath[Google] "+duraDistFirstPath);
				}
				
				System.out.println("duraDistFirstPath[0] -> "+duraDistFirstPath[0]);
				
				//Cal Second Path 
				BikePath secPath = bikePathDao.findBikePathFromId(merOneId, merTwoId , "merchant");
				if(secPath != null){
					duraDistSecPath[0] = secPath.getBike_path_distance();
					duraDistSecPath[1] = secPath.getBike_path_duration();
				}else{
					String[] duraDistSecPathStr = home.getDistanceDuration(merOneLat, merOneLng, merTwoLat, merTwoLng);
					BikePath newBikePath = new BikePath();
					newBikePath.setBike_path_source_id(merOneId);
					newBikePath.setBike_path_destination_id(merTwoId);
					newBikePath.setBike_path_type("merchant");
					newBikePath.setBike_path_distance(Double.valueOf(duraDistSecPathStr[0]));
					newBikePath.setBike_path_duration(Double.valueOf(duraDistSecPathStr[1]));
					bikePathDao.save(newBikePath);
				}
				
				//cal Last Path 
				duraDistLastPath = mapMercDistDura.get(merTwoId);
				
				double distFirst = Double.valueOf(duraDistFirstPath[0]);
				double distSec = Double.valueOf(duraDistSecPath[0]);
				double distLast = Double.valueOf(duraDistLastPath[0]);
				double duraFirst = Double.valueOf(duraDistFirstPath[1]);
				double duraSec = Double.valueOf(duraDistSecPath[1]);
				double duraLast = Double.valueOf(duraDistLastPath[1]);
	
				distTwoMerRoutePath = String.valueOf(distFirst+distSec+distLast);
				duraTwoMerRoutePath = String.valueOf(duraFirst+duraSec+duraLast); //TODO ยังไม่รวมเวลาทำอาหาร
				
				routePathListClone.get(i).setDistance(distTwoMerRoutePath);
				routePathListClone.get(i).setDuration(duraTwoMerRoutePath);
				
				System.out.println("duraDistFirstPath : " + duraDistFirstPath[0]);
				System.out.println("duraDuraFirstPath : " + duraDistFirstPath[1]);
				System.out.println("duraDistSecPath : " + duraDistSecPath[0]);
				System.out.println("duraDuraSecPath : " + duraDistSecPath[1]);
				System.out.println("duraDistLastPath : " + duraDistLastPath[0]);
				System.out.println("duraDuraLastPath : " + duraDistLastPath[1]);
				
			}else if(i == 1){
				int merThreeId = routePathListClone.get(i).getMerList().get(0).getMerID();
				String merThreeLat = routePathListClone.get(i).getMerList().get(0).getMerLatitude();
				String merThreeLng = routePathListClone.get(i).getMerList().get(0).getMerLongtitude();
				
				String[] duraDistFirstPath = new String[2];
				
				//Cal First Path 
				BikePath firstPath = bikePathDao.findBikePathFromId(staId, merThreeId , "bike");
				System.out.println("bikePathDao : " +bikePathDao);
				System.out.println("firstPath : " +firstPath);
				if(firstPath != null){
					duraDistFirstPath[0] = String.valueOf(firstPath.getBike_path_distance());
					duraDistFirstPath[1] = String.valueOf(firstPath.getBike_path_duration());
				}else{
					duraDistFirstPath = home.getDistanceDuration(staLat, staLng, merThreeLat, merThreeLng);
					BikePath newBikePath = new BikePath();
					newBikePath.setBike_path_source_id(staId);
					newBikePath.setBike_path_destination_id(merThreeId);
					newBikePath.setBike_path_type("bike");
					newBikePath.setBike_path_distance(Double.valueOf(duraDistFirstPath[0]));
					newBikePath.setBike_path_duration(Double.valueOf(duraDistFirstPath[1]));
					bikePathDao.save(newBikePath);
				}
				
				//Cal Second or Last Path 
				Double[] duraDistSecPath = mapMercDistDura.get(merThreeId);
				
				double distOneMer = Double.valueOf(duraDistFirstPath[0])+duraDistSecPath[0];
				double duraOneMer = Double.valueOf(duraDistFirstPath[1])+duraDistSecPath[1];
				
				routePathListClone.get(i).setDistance(String.valueOf(distOneMer));
				routePathListClone.get(i).setDuration(String.valueOf(duraOneMer));
				
				System.out.println("duraDistFirstPath : " + duraDistFirstPath[0]);
				System.out.println("duraDuraFirstPath : " + duraDistFirstPath[1]);
				System.out.println("duraDistSecPath : " + duraDistSecPath[0]);
				System.out.println("duraDuraSecPath : " + duraDistSecPath[1]);
			}

		}
		
		return routePathListClone;
		
	}
	
	public GroupPathDetail getBestGroupPath(List<GroupPathDetail> groupPathList){
		double minDist = 999;
		double minDura = 999;
		GroupPathDetail bestDistGroupPath = null;
		GroupPathDetail bestDuraGroupPath = null;
		
		List<GroupPathDetail> groupPathDetList = new ArrayList<GroupPathDetail>();
		
		for(int i=0;i<groupPathList.size();i++){
			double dist = Double.valueOf(groupPathList.get(i).getTotalDistance());
			double dura = Double.valueOf(groupPathList.get(i).getTotalDuration());
			
			if(dist < minDist){
				minDist = dist;
				bestDistGroupPath = groupPathList.get(i);
			}
			if(dura < minDura){
				minDura = dura;
				bestDuraGroupPath = groupPathList.get(i);
			}
		}
		
		return bestDuraGroupPath;
	}
	
	public List<Station> getStationFreeMess(){
		List<Station> stationList = new ArrayList<Station>();
		
		//query amount of free FullTime Messenger each station 
		List<Object[]> freeFTAmtList = fullTimeMessDao.getNumberOfMessengerInStation();
		BigInteger minFreeFT = new BigInteger("2");
		
		if(freeFTAmtList != null){
			for(Object[] freeFT : freeFTAmtList){
				BigInteger bigIntAmt = (BigInteger) freeFT[1];
				Integer i1 =  bigIntAmt.intValue();
				if(i1 >= 2){
//					System.out.println("count_fulltime Integer : "+i1);
					Station sta = stationDao.findByID(i1);
					stationList.add(sta);
					amtFTMap.put(sta.getStationId(), i1);
				}
			}
		}
		System.out.println("Size of station that have free FT : "+stationList.size());
		return stationList;
	}
	
	
//	public static void main(String[] args) throws InterruptedException{
//		
//		List<Integer> merIdList = new ArrayList<Integer>();
//		merIdList.add(7);
//		merIdList.add(6);
//		merIdList.add(5);
//		
//		TwoMessThreeMercService test = new TwoMessThreeMercService();
//		System.out.println(test.TwoMessThreeMercService(merIdList,"13.718996", "100.532571"));
//		
//	}
}
