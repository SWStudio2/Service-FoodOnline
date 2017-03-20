package com.fooddelivery.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.FullTimeMessenger;
import com.fooddelivery.Model.FullTimeMessengerDao;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsDao;
import com.fooddelivery.Model.TimeAndDistanceDetail;
import com.fooddelivery.Model.TimeAndDistanceDetailDao;
import com.fooddelivery.Model.User;
import com.fooddelivery.util.NodeDetail;
import com.fooddelivery.util.NodeDetailVer2;
import com.fooddelivery.util.Utils;
import com.google.api.client.json.Json;

@RestController
public class MessengerController {

	private FullTimeMessengerDao fullMessDao;
	
	@RequestMapping(value="/fullTimeAvailable" , method=RequestMethod.GET)
	@ResponseBody
	public String getFullTimeMessengerAvailable() {
		HomeController home = new HomeController();
		try {
//	    List<FullTimeMessenger> fullTimeMessList = fullMessDao.findEmptyFullTimeMessenger();
		  
		 ArrayList<Merchants> merChantList = new ArrayList<Merchants>();
	
	
		 Merchants mer1 = new Merchants();
		 mer1.setMerID(0);
		 mer1.setMerName("สีลม คอมเพล็กซ์");
		 mer1.setMerLatitude("13.728330");
		 mer1.setMerLongtitude("100.535066");
		 merChantList.add(mer1);

		 Merchants mer2 = new Merchants();
		 mer2.setMerID(1);
		 mer2.setMerName("S&P สยามพาราก้อน");
		 mer2.setMerLatitude("13.74670");
		 mer2.setMerLongtitude("100.534934");
		 merChantList.add(mer2);

		 Merchants mer3 = new Merchants();
		 mer3.setMerID(2);
		 mer3.setMerName("ข้าวมันไก่ ประตูน้ำ");
		 mer3.setMerLatitude("13.749648");
		 mer3.setMerLongtitude("100.542095");
		 merChantList.add(mer3);		 
					
			
		 FullTimeMessenger messenger1 = new FullTimeMessenger();
		 messenger1.setFullId(0);
		 messenger1.setFullName("Messenger1");
		 messenger1.setFullLastestLattitude("13.729379");//MRT SILOM
		 messenger1.setFullLastestLongtitude("100.537906");
		
		 FullTimeMessenger messenger2 = new FullTimeMessenger();
		 messenger2.setFullId(1);
		 messenger2.setFullName("Messenger2");
		 messenger2.setFullLastestLattitude("13.726376");//MRT Lumpini
		 messenger2.setFullLastestLongtitude("100.545026");
	
		 FullTimeMessenger messenger3 = new FullTimeMessenger();
		 messenger3.setFullId(1);
		 messenger3.setFullName("Messenger3");
		 messenger3.setFullLastestLattitude("13.750042");//เดอะแพลทินัมแฟชั่นมอลล์
		 messenger3.setFullLastestLongtitude("100.539636");		 
		 
		 ArrayList<FullTimeMessenger> fullTimeMessList = new ArrayList<FullTimeMessenger>();
		 fullTimeMessList.add(messenger1);
		 fullTimeMessList.add(messenger2);
		 fullTimeMessList.add(messenger3);
		 
		 Customer cus1 = new Customer();
		 cus1.setCusId(0);
		 cus1.setCusName("Customer Name");
		 String latitudeCus = "13.734116";//คณะพาณิชยศาสตร์และการบัญชี
		 String longtitudeCus = "100.530050";
	    if(fullTimeMessList.size() > 0)
	    {
	    	
	    	if(merChantList.size() == 1)
	    	{
	    		Merchants tmpMer = merChantList.get(0);
		    	FullTimeMessenger bestFullTime = null;
		    	double bestTime = 100d;
		    	for(int i = 0;i<fullTimeMessList.size();i++)
		    	{
		    		int tmpTime = 0;
		    		FullTimeMessenger tmpFullTime = fullTimeMessList.get(i);
		    		String[] arrDetail = (String[])home.getDistanceDuration(tmpFullTime.getFullLatestLattitude(), tmpFullTime.getFullLatestLongtitude(), tmpMer.getMerLatitude(), tmpMer.getMerLongtitude());
		    		tmpTime = Integer.parseInt(arrDetail[1]);
		    		System.out.println("full : " + tmpFullTime.getFullName() + " "  +  tmpTime);
		    		//time = getTimeFromGoogleAPI
		    		if(bestTime > tmpTime)
		    		{
		    			bestTime = tmpTime;
		    			bestFullTime = (FullTimeMessenger)fullTimeMessList.get(i);
		    		}
		    	}
		    	
		    	System.out.println(bestFullTime.getFullName());
		    	
		    	int timeMerToCustomer = 0;
		    	String[] arrDetail = (String[])home.getDistanceDuration(tmpMer.getMerLatitude(), tmpMer.getMerLongtitude(),latitudeCus,longtitudeCus );
		    	timeMerToCustomer = Integer.parseInt(arrDetail[1]);
		    	System.out.println("timeMerToCustomer : " + timeMerToCustomer);
		    	int sumTime = (int) (bestTime + timeMerToCustomer);	
		    	System.out.println("Time : " + sumTime);
	    	}
	    	else if(merChantList.size() > 1)
	    	{
	    		ArrayList<Integer> indexPos = new ArrayList<Integer>();
	    		for(int i = 0;i<merChantList.size();i++)
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
	    		ArrayList<NodeDetail> nodeList = new ArrayList<NodeDetail>();
	    		
	    		for(int i = 0;i<fullTimeMessList.size();i++)
	    		{
	    			FullTimeMessenger tmpFullTime = fullTimeMessList.get(i);
		    		for(int j = 0;j<arrResult.size();j++)
		    		{
		    			String tmpValue = arrResult.get(j);
		    			String[] listIndex = tmpValue.split("\\,");
		    			if(listIndex != null)
		    			{
		    				ArrayList<Merchants> arrMerChant = new ArrayList<Merchants>();
		    				for(int k = 0;k<listIndex.length;k++)
		    				{
		    					int index = Integer.parseInt(listIndex[k].trim());
		    					arrMerChant.add((Merchants)merChantList.get(index));
		    				}
		    				
		    				NodeDetail nodeDetail = new NodeDetail();
		    				nodeDetail.setFullMess(tmpFullTime);
		    				nodeDetail.setMerList(arrMerChant);
		    				nodeDetail.setLatitudeDelivery(latitudeCus);
		    				nodeDetail.setLongtitudeDelivery(longtitudeCus);
		    				nodeList.add(nodeDetail);
		    			}
		    		}	    			
	    		}
	    			
	    		NodeDetail bestNode = NodeDetail.getBestNodeDetail(nodeList);

	    		System.out.println(bestNode.getFullMess().getFullName());
	    		System.out.println(bestNode.getMerList().get(0).getMerName());
	    		System.out.println(bestNode.getMerList().get(1).getMerName());
	    		System.out.println(bestNode.getMerList().get(2).getMerName());
	    	}
	  	  	return "found"; 
	    }    
	    
	    else{
	  	  return "User Not found";
	    }
	  }
	  catch (Exception ex) {
	    return ex.toString();
	  }

	}
	
	public String searchFuncOneMessenger(String[] merId,String cus_Latitude,String cus_Longtitude) {
		
		//Define id Merchant from interface

		TimeAndDistanceDetailDao timeDisDao = new TimeAndDistanceDetailDao();
		TimeAndDistanceDetail[] tmpTimeDisDetail = timeDisDao.getTimeAndDistanceDetail(merId);
		for(int i = 0;i<tmpTimeDisDetail.length;i++)
		{
			System.out.println(tmpTimeDisDetail[i].getSourceId());
			System.out.println(tmpTimeDisDetail[i].getDestinationId());
			System.out.println(tmpTimeDisDetail[i].getDistance());
			System.out.println(tmpTimeDisDetail[i].getDuration());
			System.out.println(tmpTimeDisDetail[i].getPathType());
			System.out.println();
		}
		
		
		Merchants[] merList = queryMerChantByID(merId);
		ArrayList<Integer> indexPos = new ArrayList<Integer>();
		for(int i = 0;i<merId.length;i++)
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
		
		String[] idListStationFullTime = getFullTimeIdAvailable(tmpTimeDisDetail);
		
		ArrayList<NodeDetailVer2> arrNode = new ArrayList<NodeDetailVer2>();
		for(int i = 0;i<idListStationFullTime.length;i++)
		{

			for(int j = 0;j<arrResult.size();j++)
			{
				NodeDetailVer2 tmpNodeDetail = new NodeDetailVer2();
				tmpNodeDetail.setStation(Integer.valueOf(idListStationFullTime[i]));
				ArrayList<Merchants> arrMerchant = new ArrayList<Merchants>();
				String[] postList = arrResult.get(j).split("\\,");
				for(int k = 0;k<postList.length;k++)
				{
					int pos = Integer.parseInt(postList[k].trim());
					arrMerchant.add(merList[pos]);
				}
				
				tmpNodeDetail.setMerList(arrMerchant);
				tmpNodeDetail.setLatitudeDelivery(cus_Latitude);
				tmpNodeDetail.setLatitudeDelivery(cus_Longtitude);
				
				arrNode.add(tmpNodeDetail);
			}
		}
		
		
		return "";
	}
	
	public static String[] getFullTimeIdAvailable(TimeAndDistanceDetail[] tmp)
	{
		ArrayList<String> arrMessId = new ArrayList<String>();
		for(int i = 0;i<tmp.length;i++)
		{
			if(tmp[i].getPathType().equals("bike") && !arrMessId.contains(tmp[i].getSourceId()))
			{
				arrMessId.add(tmp[i].getSourceId());
			}
		}
		
		String[] idList = new String[arrMessId.size()];
		for(int i = 0;i<arrMessId.size();i++)
		{
			idList[i] = arrMessId.get(i);
		}
		return idList;
	}
	
	  public Merchants[] queryMerChantByID(String[] merId)
	  {
		  Connection connect = null;
			Statement s = null;
			String URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc";
			URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc?useUnicode=true&amp;characterEncoding=UTF-8";
			String USER = "ba8167e655c97d";
			String PASSWORD = "fcc5664d";
			
			String merchantIdForQuery = "";
			Merchants[] merList = null;
			for(int i = 0;i<merId.length;i++)
			{
				if(i == 0)
				{
					merchantIdForQuery += merId[i];
				}
				else
				{
					merchantIdForQuery += "," + merId[i];
				}
			}
			


			// Close

			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(URL_DB, USER, PASSWORD);
				
				if(connect != null){
					System.out.println("Database Connected.");
				} else {
					System.out.println("Database Connect Failed.");
				}
				s = connect.createStatement();
							
				String sql = "";
				
				StringBuffer sqlBuffer = new StringBuffer();

				sqlBuffer.append("SELECT MER_ID,MER_LATITUDE,MER_LONGTITUDE,MER_COOKTIME \n ");
				sqlBuffer.append("FROM merchants  \n ");
				sqlBuffer.append("Where mer_id in (merchantList) \n ");

				
				sql = sqlBuffer.toString();	
				
				ResultSet rec = s.executeQuery(sql);
				
				if(rec == null)
				{
					System.out.print(" record not found ");
				}
//				System.out.println("" + sql);
				ArrayList<Merchants> arrMerchant = new ArrayList<Merchants>();
				while((rec!=null) && (rec.next()))
	            {
					Merchants tmpMerchant = new Merchants();
					tmpMerchant.setMerID(Integer.parseInt(rec.getString("MER_ID")));
					tmpMerchant.setMerLatitude(rec.getString("MER_LATITUDE"));
					tmpMerchant.setMerLongtitude(rec.getString("MER_LONGTITUDE"));
					tmpMerchant.setCookingTime(rec.getInt("MER_COOKTIME"));
					arrMerchant.add(tmpMerchant);
	            }
				
				merList = new Merchants[arrMerchant.size()];
				for(int i = 0;i<arrMerchant.size();i++)
				{
					merList[i] = (Merchants)arrMerchant.get(i);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Close
			try {
				if(connect != null){
					connect.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				  
		  return merList;
	  }
	
}


