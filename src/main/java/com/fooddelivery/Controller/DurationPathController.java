package com.fooddelivery.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.DurationPath.OneMessOneMerchant;
import com.fooddelivery.util.NodeDetailVer2;

@RestController
public class DurationPathController {
	
	/*@Autowired
	private CustomerDao customerDao;
	@Autowired
	private DeliveryRateDao delvDao;*/
	
	@RequestMapping(value={"/servicetest/durationPathAndDeliveryFeeResult"} ,method=RequestMethod.POST)
	@ResponseBody
	public void durationPathAndDeliveryFeeResult(@RequestParam("merchants") String merchants){ 
		/*ResponseEntity<Response<HashMap>>*/
		
		/*
		 * method that calculate TimeAndDistanceAllPath
		 * 1 mess for 1 mer
		 * 2 mess
		 * 3 mess ...
		 */
		String latCustomer = "13.7033152";
		String longCustomer = "100.5023999";
		int[] merchantId = {6,8,9};
		OneMessOneMerchant oneMessOneMerchant = new OneMessOneMerchant(merchantId, latCustomer, longCustomer);
		List<NodeDetailVer2> resultOneMessOneMerchant = oneMessOneMerchant.getResult();
		
		//return merchants;
	}

}
