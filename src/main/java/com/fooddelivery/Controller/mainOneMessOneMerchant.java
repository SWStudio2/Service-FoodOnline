package com.fooddelivery.Controller;

import com.fooddelivery.Controller.HomeController;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.TimeAndDistanceDetail;
import com.fooddelivery.util.NodeDetailVer2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class mainOneMessOneMerchant {
	
	/*
	 * this function for one mess support one merchant
	 */
	
	private static String BIKE_PATH_TYPE = "bike";
	private static String BIKE_PATH = "/TxtFile/"
			+ "TimeAndDistanceDetail_Merchant689.txt";
	private static String MERCHANT_PATH = "/TxtFile/"
			+ "Merchants689.txt";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String latCustomer = "13.7033152";
		String longCustomer = "100.5023999";
		
		String[] idMerchants = {"6", "8", "9"};
		
		/*
		 * '6','8','6.00','1.70','merchant'
		 * '6','9','9.00','2.20','merchant'
		 * '8','6','12.00','1.60','merchant'
		 * '8','9','8.00','1.90','merchant'
		 * '9','6','11.00','3.40','merchant'
		 * '9','8','6.00','1.60','merchant'
		 * '1','6','10.00','2.60','bike'
		 * '1','8','7.00','2.00','bike'
		 * '1','9','9.00','2.50','bike'
		 * '2','6','15.00','4.40','bike'
		 * '2','8','9.00','2.80','bike'
		 * '2','9','11.00','3.30','bike'
		 * '3','6','8.00','2.00','bike'
		 * '3','8','9.00','3.40','bike'
		 * '3','9','12.00','3.70','bike'
		 * '4','6','14.00','3.30','bike'
		 * '4','8','14.00','4.60','bike'
		 * '4','9','16.00','5.30','bike'
		 */
		//call method bike_path
		List<TimeAndDistanceDetail> timeAndDistanceDetailList = 
				getBikePathByMerchants(idMerchants);
		
		//call merchant info
		/*MerchantController merchantController = new MerchantController();
		merchantController.getInfoMerchants("6,8,9");*/
		List<Merchants> merchantList = getMerchantsByMerchantsId(idMerchants);
		int amountMerchants = idMerchants.length;
		/*
		 * make to HashMap
		 */
		HashMap<String, Merchants> merchantsHash = new HashMap<String, Merchants>();
		for(int i=0; i<merchantList.size(); i++) {
			merchantsHash.put(String.valueOf(merchantList.get(i).getMerID()), 
					merchantList.get(i));
		}
		
		//amountMerchants = idMerchants.length;
		/*Merchants[] merList = new Merchants[amountMerchants];
		for(int i = 0;i<merList.length;i++)
		{
			merList[i] = new Merchants();
			
		}
		merList[0].setMerID(6);
		merList[0].setMerName("รองเมือง เกาเหลา");
		merList[0].setMerLatitude("13.746012");
		merList[0].setMerLongtitude("100.518321");
		
		merList[1].setMerID(8);
		merList[1].setMerName("ฮ่องกง นู้ดเดิ้ล หัวลำโพง");
		merList[1].setMerLatitude("13.738376");
		merList[1].setMerLongtitude("100.517617");			
				
		merList[2].setMerID(9);
		merList[2].setMerName("ภัตตาคารตั้งจั๊วหลี หัวปลาหม้อไฟ");
		merList[2].setMerLatitude("13.736537");
		merList[2].setMerLongtitude("100.514813");*/
		
		/*
		 * get Merchant to Customer
		 */
		HomeController home = new HomeController();
		HashMap<String, String[]> merchantToCustomerDetail = 
				new HashMap<String, String[]>();
		for(int i = 0; i<merchantList.size(); i++)
		{
			Thread.sleep(1000);
			Merchants tmpMer = merchantList.get(i);
			String[] arrDetail = (String[])home.getDistanceDuration(latCustomer, 
					longCustomer, tmpMer.getMerLatitude(), 
					tmpMer.getMerLongtitude());
			/*System.out.println(tmpMer.getMerName());
			System.out.println("Distance : " + arrDetail[0]);
			System.out.println("Duration : " + arrDetail[1]);*/
			
			String[] detail = {String.valueOf(merchantList.get(i).getMerID()), 
					arrDetail[0], arrDetail[1]};
			merchantToCustomerDetail.put(
					String.valueOf(merchantList.get(i).getMerID()), detail);
			
		}
		
		/*
		 * 
		 */
		//เอา model ที่เป็น type bike มาวนเพื่อใช้ messenger
		List<TimeAndDistanceDetail> timeAndDistanceBikeDetailList = 
				new ArrayList<TimeAndDistanceDetail>();
		for (int i=0; i<timeAndDistanceDetailList.size(); i++) {
			//System.out.println(">" + timeAndDistanceDetailList.get(i).getPathType());
			if (timeAndDistanceDetailList.get(i).getPathType().trim()
					.toString().equals(BIKE_PATH_TYPE)) {
				//System.out.println(timeAndDistanceDetailList.get(i).getSourceId());
				timeAndDistanceBikeDetailList.add(
						timeAndDistanceDetailList.get(i));
			}
		}
		
		/*HashMap<String, Collection<Double>> result = new HashMap<String, 
				Collection<Double>>();*/
		List<NodeDetailVer2> resultAll = new ArrayList<NodeDetailVer2>();
		for (int i=0; i<timeAndDistanceBikeDetailList.size(); i++) { //mess->merchants
			//mess->mer = path1 (timeAndDistanceBikeDetailList)
			Double durationPath1 = Double.valueOf(
					timeAndDistanceBikeDetailList.get(i).getDuration());
			Double distancePath1 = Double.valueOf(
					timeAndDistanceBikeDetailList.get(i).getDistance());
			//merId->cus = path2 (merchantToCustomerDetail)
			Double durationPath2 = Double.valueOf(
					merchantToCustomerDetail.get(
							String.valueOf(timeAndDistanceBikeDetailList.get(i)
									.getDestinationId()
									))[2]);
			Double distancePath2 = Double.valueOf(
					merchantToCustomerDetail.get(
							String.valueOf(timeAndDistanceBikeDetailList.get(i)
									.getDestinationId()
									))[1]);
			//path1+path2
			Double duration = durationPath1 + durationPath2;
			Double distance = distancePath1 + distancePath2;
			
			/*String key = timeAndDistanceBikeDetailList.get(i).getSourceId() + "-"
					+ timeAndDistanceBikeDetailList.get(i).getDestinationId() + "-"
					+ "Customer";*/
			/*result.put(key, new ArrayList<Double>());
			result.get(key).add(duration);
			result.get(key).add(distance);*/
			NodeDetailVer2 nodeDetail = new NodeDetailVer2();
			nodeDetail.setStation(timeAndDistanceBikeDetailList.get(i).getSourceId());
			ArrayList<Merchants> merchantsList = new ArrayList<Merchants>();
			merchantsList.add(merchantsHash.get(timeAndDistanceBikeDetailList
					.get(i).getDestinationId()));
			nodeDetail.setMerList(merchantsList);
			nodeDetail.setLatitudeDelivery(latCustomer);
			nodeDetail.setLongtitudeDelivery(longCustomer);
			nodeDetail.setDuration(String.valueOf(duration));
			nodeDetail.setDistance(String.valueOf(distance));
			resultAll.add(nodeDetail);
		}
		//sort
		resultAll = sortResult(resultAll);
		printResult(resultAll);
		List<NodeDetailVer2> result = getBestResult(resultAll, idMerchants);
		System.out.println("**************************************");
		System.out.println("***************RESULT*****************");
		System.out.println("**************************************");
		printResult(result);
	}
	
	private static List<TimeAndDistanceDetail> getBikePathByMerchants(String[] merchantsId) {
		File file = new File(new java.io.File("").getAbsolutePath() + BIKE_PATH);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			List<TimeAndDistanceDetail> result = 
					new ArrayList<TimeAndDistanceDetail>();
			while ( (line = br.readLine() ) != null ) {
				String[] temp = line.split(",");
				for (int i=0; i<temp.length; i++) {
					temp[i] = temp[i].replace("'", "");
				}
				TimeAndDistanceDetail timeAndDistanceDetail = 
						new TimeAndDistanceDetail();
				timeAndDistanceDetail.setSourceId(temp[0]);
				timeAndDistanceDetail.setDestinationId(temp[1]);
				timeAndDistanceDetail.setDuration(temp[2]);
				timeAndDistanceDetail.setDistance(temp[3]);
				timeAndDistanceDetail.setPathType(temp[4]);
				result.add(timeAndDistanceDetail);
			}
			br.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static List<Merchants> getMerchantsByMerchantsId(String[] merchantsId) {
		File file = new File(new java.io.File("").getAbsolutePath() + MERCHANT_PATH);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			List<Merchants> result = 
					new ArrayList<Merchants>();
			while ( (line = br.readLine() ) != null ) {
				//System.out.println(line);
				String[] temp = line.split("','");
				for (int i=0; i<temp.length; i++) {
					temp[i] = temp[i].replace("'", "");
				}
				Merchants merchant = 
						new Merchants();
				merchant.setMerID(Long.valueOf(temp[0]));
				merchant.setMerName(temp[1]);
				merchant.setMerAddress(temp[2]);
				merchant.setMerLatitude(temp[3]);
				merchant.setMerLongtitude(temp[4]);
				merchant.setMerContactNumber(temp[5]);
				merchant.setMerPercentShare(Double.valueOf(temp[6]));
				merchant.setMerOpenStatus(temp[7]);				
				result.add(merchant);
			}
			br.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void printResult(List<NodeDetailVer2> result) {
		for (int i=0; i<result.size(); i++) {
		    System.out.println("Path: " + result.get(i).getStation() + "-"
		    		+ result.get(i).getMerList().get(0).getMerID() + "-"
		    		+ "Customer");
		    System.out.println("Duaration: " + result.get(i).getDuration());
		    System.out.println("Distance: " + result.get(i).getDistance());
		    System.out.println("----------------------------------");
		}
	}
	
	private static List<NodeDetailVer2> getBestResult(List<NodeDetailVer2> 
		nodeDetailVer2, String[] merchantsId) {
		List<NodeDetailVer2> result = new ArrayList<NodeDetailVer2>();
		/*
		 * HashMap Merchants Id
		 */
		HashMap<String, String> merchantsHash = new HashMap<String, String>();
		for (int i=0; i<merchantsId.length; i++) {
			merchantsHash.put(merchantsId[i], merchantsId[i]);
		}
		
		for (int i=0; i<nodeDetailVer2.size(); i++) {
			String merId = String.valueOf(nodeDetailVer2.get(i).getMerList().get(0)
					.getMerID());
			//System.out.println(">>>>" + merId);
			String merchantId = merchantsHash.get(merId);
			if (merchantId != null) {
				result.add(nodeDetailVer2.get(i));
				merchantsHash.remove(merId);
			}
			if (merchantsHash.isEmpty()) {
				break;
			}
		}
		return result;
	}
	
	//sort
	private static List<NodeDetailVer2> sortResult(List<NodeDetailVer2> nodeDetailVer2) {
	    Collections.sort(nodeDetailVer2, new Comparator() {
	        public int compare(Object o1, Object o2) {
	            String duration1 = ((NodeDetailVer2) o1).getDuration();
	            String duration2 = ((NodeDetailVer2) o2).getDuration();
	            int sComp = duration1.compareTo(duration2);

	            if (sComp != 0) {
	               return sComp;
	            } else {
	               String distance1 = ((NodeDetailVer2) o1).getDistance();
	               String distance2 = ((NodeDetailVer2) o2).getDistance();
	               return distance1.compareTo(distance2);
	            }
	    }});
	    return nodeDetailVer2;
	}
}
