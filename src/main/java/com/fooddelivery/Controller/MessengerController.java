package com.fooddelivery.Controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fooddelivery.Model.FullTimeMessenger;
import com.fooddelivery.Model.FullTimeMessengerDao;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.User;
import com.google.api.client.json.Json;


public class MessengerController {

	private FullTimeMessengerDao fullMessDao;
	
	@RequestMapping(value="/fullTimeAvailable" , method=RequestMethod.GET)
	@ResponseBody
	public String getFullTimeMessengerAvailable() {
	  try {
//	    List<FullTimeMessenger> fullTimeMessList = fullMessDao.findEmptyFullTimeMessenger();
		  
		 ArrayList<Merchants> merChantList = new ArrayList<Merchants>();
		 
		 Merchants mer1 = new Merchants();
		 mer1.setMerID(0);
		 mer1.setMerName("โอชายะ");
		 mer1.setMerLatitude("13.7330385");
		 mer1.setMerLongtitude("100.5274889");
		 
		  
		 FullTimeMessenger messenger1 = new FullTimeMessenger();
		 messenger1.setFullId(0);
		 messenger1.setFullName("Mike test1");
		 messenger1.setFullLastestLattitude(13.7299118d);//MRT SILOM
		 messenger1.setFullLastestLongtitude(100.5333147d);
		 
		 FullTimeMessenger messenger2 = new FullTimeMessenger();
		 messenger2.setFullId(1);
		 messenger2.setFullName("Mess2");
		 messenger2.setFullLastestLattitude(13.7265737d);//MRT Lumpini
		 messenger2.setFullLastestLongtitude(100.5432149d);
		 
		 ArrayList<FullTimeMessenger> fullTimeMessList = new ArrayList<FullTimeMessenger>();
		 fullTimeMessList.add(messenger1);
		 fullTimeMessList.add(messenger2);
		 
		 
	    if(fullTimeMessList.size() > 0)
	    {
	    	FullTimeMessenger bestFullTime;
	    	double bestTime = 100d;
	    	for(int i = 0;i<fullTimeMessList.size();i++)
	    	{
	    		double tmpTime = 0d;
	    		//time = getTimeFromGoogleAPI
	    		if(bestTime > tmpTime)
	    		{
	    			bestTime = tmpTime;
	    			bestFullTime = (FullTimeMessenger)fullTimeMessList.get(i);
	    		}
	    	}
	    	
	    	double timeMerToCustomer = 0d;
	    	double sumTime = bestTime + timeMerToCustomer;
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

}


