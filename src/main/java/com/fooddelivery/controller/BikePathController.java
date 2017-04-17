package com.fooddelivery.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
//import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.fooddelivery.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fooddelivery.Model.BikePathDao;
import com.fooddelivery.Model.BikeStation;
import com.fooddelivery.Model.BikeStationDao;
import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.CustomerDao;
import com.fooddelivery.Model.DeliveryRate;
import com.fooddelivery.Model.DeliveryRateDao;
import com.fooddelivery.Model.FullTimeMessengerDao;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsDao;

@RequestMapping(value={"/service/customer"})
@RestController
public class BikePathController {
	private static final Logger logger = LoggerFactory.getLogger(BikePathController.class);
	// Wire the UserDao used inside this controller.
	@Autowired
	private FullTimeMessengerDao fullMessDao;
	@Autowired
	private BikeStationDao bikeDao;
	@Autowired
	private MerchantsDao merDao;
	@Autowired
	private BikePathDao bikePathDao;
	final static String BIKEPATH = "bike";
	final static String MerchantPATH = "merchant";
	@RequestMapping(value={"/insertBikePath/{flagAdd}"} ,method=RequestMethod.POST)
	public ResponseEntity<Response<String>> insertBikePath(@PathVariable("flagAdd") String flagAdd) throws InterruptedException{
		
		String result = "failed";
		if(flagAdd.equals("Add"))
		{
			List<BikeStation> bkList = bikeDao.getBikeStationAll();
			List<Merchants> merList = merDao.findAllMerchants();
			HomeController home = new HomeController();
			int runningNo = 1;
			for(int i = 0;i<bkList.size();i++)
			{
				BikeStation tmpBike = bkList.get(i);
				for(int j = 0;j<merList.size();j++)
				{
					Merchants tmpMer = merList.get(j);
					Thread.sleep(1000);
					String[] arrDetail = home.getDistanceDuration(tmpBike.getBikeStationLatitude(), tmpBike.getBikeStationLongitude()
							, tmpMer.getMerLatitude(), tmpMer.getMerLongtitude());
					bikePathDao.insertOrder(runningNo, tmpBike.getBikeStationId(), tmpMer.getMerID(), 
						new BigDecimal(arrDetail[1]).setScale(2, RoundingMode.HALF_EVEN).doubleValue(), 
						new BigDecimal(arrDetail[0]).setScale(2, RoundingMode.HALF_EVEN).doubleValue(), BIKEPATH);				
				}
			}
			
			for(int i = 0;i<merList.size();i++)
			{
				Merchants merStart = merList.get(i);
				for(int j = 0;j<merList.size() && i != j;j++)
				{
					Merchants merDestination = merList.get(j);
					Thread.sleep(1000);
					String[] arrDetail = home.getDistanceDuration(merStart.getMerLatitude(), merStart.getMerLongtitude()
							, merDestination.getMerLatitude(), merDestination.getMerLongtitude());
					bikePathDao.insertOrder(runningNo, merStart.getMerID(), merDestination.getMerID(), 
						new BigDecimal(arrDetail[1]).setScale(2, RoundingMode.HALF_EVEN).doubleValue(), 
						new BigDecimal(arrDetail[0]).setScale(2, RoundingMode.HALF_EVEN).doubleValue(), BIKEPATH);					
				}
			}
			
			result = "ADD Success";
		}
		
		
		return ResponseEntity.ok(new Response<String>(HttpStatus.OK.value(),
				"insert successfully", result));

	}
}
