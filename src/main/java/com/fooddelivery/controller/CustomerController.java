package com.fooddelivery.controller;

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

import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.CustomerDao;
import com.fooddelivery.Model.DeliveryRate;
import com.fooddelivery.Model.DeliveryRateDao;
import com.fooddelivery.Model.Merchants;

@RequestMapping(value={"/service/customer"})
@RestController
public class CustomerController {
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	// Wire the UserDao used inside this controller.
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private DeliveryRateDao delvDao;

	@RequestMapping(value={"/auth"} ,method=RequestMethod.POST)
	public ResponseEntity<Response<HashMap>> authen(@RequestBody Customer cus){
//		User user = new User();
//		user.setId(1);
//		user.setName("Test1");
//
//		return ResponseEntity.ok(new Response<User>(HttpStatus.OK.value(),
//				"Login successfully", user));

		List<DeliveryRate> delvRate = null;
		List<Customer> customer = null;
		HashMap<String,String> resultList = new HashMap<String, String>();

		try {
			logger.info("Check point 0");
			delvRate = 	delvDao.findAllDeliveryRate();
			logger.info("Check point 0.1");
			customer = customerDao.findByCusEmail(cus.getCusUserName(), cus.getCusPassword());
			logger.info(""+customer);
			logger.info(""+delvRate);

			if(customer.size() != 0){
				Customer cust = customer.get(0);
				resultList.put("cus_id", String.valueOf(cust.getCusId()));
				resultList.put("cus_name", cust.getCusName());
				resultList.put("cus_username", cust.getCusUserName());
				resultList.put("cus_password", cust.getCusPassword());
				resultList.put("cus_contact_number", cust.getCusContactNumber());

				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				Date createdDate = cust.getCusCreatedDate();
				resultList.put("cus_created_date", df.format(createdDate));

				resultList.put("delivery_rate", String.valueOf(delvRate.get(0).getDeliveryRate()));

				logger.info(""+resultList);
			}else{
				logger.info("Check point 1");
				return null;
			}


			return ResponseEntity.ok(new Response<HashMap>(HttpStatus.OK.value(),
					"Login successfully", resultList));

		}
		catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			logger.info("Check point 2");
			return null;
		}


	}
}
