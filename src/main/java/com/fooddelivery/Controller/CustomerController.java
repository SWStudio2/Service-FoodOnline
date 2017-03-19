package com.fooddelivery.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.CustomerDao;
import com.fooddelivery.Model.DeliveryRate;
import com.fooddelivery.Model.DeliveryRateDao;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsDao;

public class CustomerController {
	// Wire the UserDao used inside this controller.
		@Autowired
		private CustomerDao customerDao;
		private DeliveryRateDao delvDao;
		
		@RequestMapping(value="/service/login" , method = RequestMethod.POST)
		@ResponseBody
		public List<Customer> getMerchants(@RequestParam(value = "username") String username , @RequestParam(value = "pass") String pass){
			
			List<DeliveryRate> delvRate = null;
			List<Customer> customer = null;
			HashMap<String,String> resultList = new HashMap<String, String>();
			
			try {
//				delvRate = 	(List<DeliveryRate>) delvDao.findAll();
//				customer = customerDao.findByCusEmail(username, pass);
//				
//				if(customer.size() != 0){
//					Customer cust = customer.get(0);
//					resultList.put("cus_id", String.valueOf(cust.getCusId()));
//					resultList.put("cus_email", cust.getCusEmail());
//					resultList.put("cus_name", cust.getCusName());
//					resultList.put("cus_username", cust.getCusUserName());
//					resultList.put("cus_password", cust.getCusPassword());
//					resultList.put("cus_contact_number", cust.getCusContactNumber());
//					resultList.put("cus_created_date", cust.getCusCreatedDate());
//				}else{
//					
//				}
				
				return customerDao.findByCusEmail(username, pass);
				
		    }
		    catch (Exception ex) {
		    	return null;
		    }
			
		}
}
