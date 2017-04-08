package com.fooddelivery.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsDao;
import com.fooddelivery.util.DateTime;


@RestController
public class MerchantController {
	
	// Wire the UserDao used inside this controller.
	@Autowired
	private MerchantsDao merchantsDao;
	
	@RequestMapping(value="/service/merchant/getall" , method = RequestMethod.POST)
	@ResponseBody
	public List<Merchants> getMerchants(@RequestParam(value = "mername" , required = false) Optional<String> mername){
		
		
		try {
			
			List<Merchants> merchants = null;
			
			//set current date and time and convert year to �.�.
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			Date date = new Date();
			String dateStr = dateFormat.format(date);
			
			String time = String.format("%02d:%02d:%02d", date.getHours(), date.getMinutes(), date.getSeconds());
			
			if(mername.isPresent()){
				merchants = merchantsDao.findByMerName(mername.get(),dateStr,time);
			}else{
				merchants = merchantsDao.findByStatus(dateStr,time);
			}
			
			return merchants;

			
	    }
	    catch (Exception ex) {
	    	System.out.println(ex.getMessage());
	    	return null;
	    }
		
	}
	
}
