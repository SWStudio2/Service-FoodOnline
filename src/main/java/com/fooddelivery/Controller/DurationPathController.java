package com.fooddelivery.Controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.CustomerDao;
import com.fooddelivery.Model.DeliveryRate;
import com.fooddelivery.Model.DeliveryRateDao;
import com.fooddelivery.util.Response;

public class DurationPathController {
	
	/*@Autowired
	private CustomerDao customerDao;
	@Autowired
	private DeliveryRateDao delvDao;*/
	
	@RequestMapping(value={"/service/durationPathAndDeliveryFeeResult"} ,method=RequestMethod.POST)
	public /*ResponseEntity<Response<HashMap>>*/void durationPathAndDeliveryFeeResult(@RequestParam("merchants") String merchants){
		
		/*
		 * method that calculate TimeAndDistanceAllPath
		 * 1 mess for 1 mer
		 * 2 mess
		 * 3 mess ...
		 */
		

	}

}
