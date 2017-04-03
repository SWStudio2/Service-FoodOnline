package com.fooddelivery.service.durationpath;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsQuery;
import com.fooddelivery.Model.Station;
import com.fooddelivery.Model.StationQuery;
import com.fooddelivery.Model.TimeAndDistanceDetailDao;
import com.fooddelivery.controller.HomeController;
import com.fooddelivery.util.GroupPathDetail;
import com.fooddelivery.util.RoutePathDetail;
import com.fooddelivery.util.Utils;

public class TwoMessThreeMercService {
	

	public String TwoMessThreeMercService(List<Integer> merId,String cus_Latitude,String cus_Longtitude) throws InterruptedException {
		
		//create Dao for query
		TimeAndDistanceDetailDao timeDisDao = new TimeAndDistanceDetailDao();
		MerchantsQuery merDao = new MerchantsQuery();
		
		//get station from database
		StationQuery stationQue = new StationQuery();
		Station[] staList = stationQue.getStationAvailable();
		
		//get Merchant List from merID from Database 
		String merIdAdjust = "";
		for(int i = 0;i<merId.size();i++)
		{
			if (i != 0) {
				merIdAdjust += ",";
			}
			merIdAdjust += merId.get(i);
		}
		Merchants[] merList = merDao.queryMerChantByID(merIdAdjust);
		
		
		
		//create copy of merId
		ArrayList<Integer> indexPos = new ArrayList<Integer>();
		for(int i = 0;i<merId.size();i++)
		{
			indexPos.add(i);
		}
		
		//[0,1,2] ==> [[0,1,2],[1,0,2],[0,2,1],...]
		Object[] resultSet = (Object[])Utils.listPermutations(indexPos).toArray();
		
		//[[a,b,c],[b,c,a],[a,c,b],...] ==> [ "a,b,c", "b,c,a", "a,c,b"]
		ArrayList<GroupPathDetail> groupPathList = new ArrayList<GroupPathDetail>();
		
		//get duration and distance each path
//		String mer1Lat = merList[0].getMerLatitude();
//		String mer1Lng = merList[0].getMerLongtitude();
//		String mer2Lat = merList[1].getMerLatitude();
//		String mer2Lng = merList[1].getMerLongtitude();
//		String mer3Lat = merList[2].getMerLatitude();
//		String mer3Lng = merList[2].getMerLongtitude();
//		for(int i=0;i<staList.length;i++){
//			
//		}
//		
//		callGoogleMap(latOri, lngOri, latDes, lngDes);
		
		//Loop resultSet and station for set value to GroupPathDetail
		for(int i = 0;i<resultSet.length;i++)
		{
			for(int j=0;j<staList.length;j++){
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
				routePath1.setStation(staList[j]);
				routePath1.setLatitudeDelivery(cus_Latitude);
				routePath1.setLongtitudeDelivery(cus_Longtitude);
				
				//set RoutePathDetail of 1 Merchant
				RoutePathDetail routePath2 = new RoutePathDetail();
				routePath2.setMerList(mercList2);
				routePath2.setStation(staList[j]);
				routePath2.setLatitudeDelivery(cus_Latitude);
				routePath2.setLongtitudeDelivery(cus_Longtitude);
				
				//set 2 RoutePathDetail into GroupPathDetail
				GroupPathDetail groupPath = new GroupPathDetail();
				groupPath.addRoutePathDetail(routePath1);
				groupPath.addRoutePathDetail(routePath2);
				
				//add groupPath into GroupPathList
				groupPathList.add(groupPath);
			}
			
			
			
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
						routePathList = calDuraAndDist(routePathList);
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
						
//						System.out.println("groupPathList : "+i+" "+String.valueOf(totalDist)+","+String.valueOf(totalDura));
				}
//				}
				TimeUnit.SECONDS.sleep(30);
			}
			
			//get Best Case
			BestGroupPath = getBestGroupPath(groupPathList);
			
		}
		
		//return best node's time estimate
		return BestGroupPath.getTotalDuration();
	}
	
	/**
	 * cal duration and distance of each routePathDetail
	 * @param routePathList
	 * @return
	 */
	public List<RoutePathDetail> calDuraAndDist(List<RoutePathDetail> routePathList){
		HomeController home = new HomeController();
		List<RoutePathDetail> routePathListClone = routePathList;
		
		String cusLat = routePathListClone.get(0).getLatitudeDelivery();
		String cusLng = routePathListClone.get(0).getLongtitudeDelivery();
		
		String duraTwoMerRoutePath = "";
		String distTwoMerRoutePath = "";
		String duraOneMerRoutePath = "";
		String distOneMerRoutePath = "";
				
		for(int i=0;i<routePathList.size();i++){
			
			String staLat = routePathListClone.get(i).getStation().getStationLantitude();
			String staLng = routePathListClone.get(i).getStation().getStationLongtitude();
			
			if(i == 0){
				String merOneLat = routePathListClone.get(i).getMerList().get(0).getMerLatitude();
				String merOneLng = routePathListClone.get(i).getMerList().get(0).getMerLongtitude();
				String merTwoLat = routePathListClone.get(i).getMerList().get(1).getMerLatitude();
				String merTwoLng = routePathListClone.get(i).getMerList().get(1).getMerLongtitude();
				
				String[] duraDistFirstPath = home.getDistanceDuration(staLat, staLng, merOneLat, merOneLng);
				String[] duraDistSecPath = home.getDistanceDuration(merOneLat, merOneLng, merTwoLat, merTwoLng);
				String[] duraDistLastPath = home.getDistanceDuration(merTwoLat, merTwoLng, cusLat, cusLng);
				
				double distFirst = Double.valueOf(duraDistFirstPath[0]);
				double distSec = Double.valueOf(duraDistSecPath[0]);
				double distLast = Double.valueOf(duraDistLastPath[0]);
				double duraFirst = Double.valueOf(duraDistFirstPath[1]);
				double duraSec = Double.valueOf(duraDistSecPath[1]);
				double duraLast = Double.valueOf(duraDistLastPath[1]);
				
				
				if(duraDistFirstPath != null && duraDistSecPath != null && duraDistLastPath != null){
					distTwoMerRoutePath = String.valueOf(distFirst+distSec+distLast);
					duraTwoMerRoutePath = String.valueOf(duraFirst+duraSec+duraLast);
				}
				
				routePathListClone.get(i).setDistance(distTwoMerRoutePath);
				routePathListClone.get(i).setDuration(duraTwoMerRoutePath);
			}else if(i == 1){
				String merThreeLat = routePathListClone.get(i).getMerList().get(0).getMerLatitude();
				String merThreeLng = routePathListClone.get(i).getMerList().get(0).getMerLongtitude();
				
				String[] duraDistFirstPath = home.getDistanceDuration(staLat, staLng, merThreeLat, merThreeLng);
				String[] duraDistSecPath = home.getDistanceDuration(merThreeLat, merThreeLng, cusLat, cusLng);
				
				double distOneMer = Double.valueOf(duraDistFirstPath[0])+Double.valueOf(duraDistSecPath[0]);
				double duraOneMer = Double.valueOf(duraDistFirstPath[1])+Double.valueOf(duraDistSecPath[1]);
				
				routePathListClone.get(i).setDistance(String.valueOf(distOneMer));
				routePathListClone.get(i).setDuration(String.valueOf(duraOneMer));
			}

		}
		
		return routePathListClone;
		
	}
	
	public GroupPathDetail getBestGroupPath(List<GroupPathDetail> groupPathList){
		double minDist = 999;
		double minDura = 999;
		GroupPathDetail bestDistGroupPath = null;
		GroupPathDetail bestDuraGroupPath = null;
		
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
	
	public String[] callGoogleMap(String latOri,String lngOri,String latDes,String lngDes){
		HomeController home = new HomeController();
		String[] duraDist = home.getDistanceDuration(latOri, lngOri, latDes, lngDes);
		
		return duraDist;
	}
	
	
	/*public static void main(String[] args) throws InterruptedException{
		
		List<Integer> merIdList = new ArrayList<Integer>();
		merIdList.add(1);
		merIdList.add(2);
		merIdList.add(3);
		
		TwoMessThreeMercService test = new TwoMessThreeMercService();
		System.out.println(test.TwoMessThreeMercService(merIdList,"13.718996", "100.532571"));
		
	}*/
}
