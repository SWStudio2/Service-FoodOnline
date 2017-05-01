package com.fooddelivery.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
//import java.util.Optional;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.fooddelivery.util.DateTime;
import com.fooddelivery.util.Response;
import com.fooddelivery.util.VariableText;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.CustomerDao;
import com.fooddelivery.Model.DeliveryRate;
import com.fooddelivery.Model.DeliveryRateDao;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.NotificationInbox;
import com.fooddelivery.Model.NotificationInboxDao;

@RequestMapping(value={"/service/customer"})
@RestController
public class CustomerController {
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	// Wire the UserDao used inside this controller.
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private DeliveryRateDao delvDao;

	@Autowired
	private CustomerDao custDao;

	@Autowired
	private NotificationInboxDao notiDao;
	
	

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
	
	@RequestMapping(value="/service/orders/confirmcode/customer" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response<Map<String, Object>>> verifyConfirmCustomer(@RequestBody Map<String, Object> mapRequest)
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();
		long order_id = (Long) mapRequest.get("order_id");
		String confirm_code = (String) mapRequest.get("seqor_confirm_code");
		long full_id = (Long) mapRequest.get("full_id");
		long[] merList = (long[])mapRequest.get("mer_id");
		
		String messeage = "";
		String result = customerDao.verifyConfirmCodeCustomer(order_id, confirm_code);
		if(result.equals("Y"))
		{
			Date currentDateTime = DateTime.getCurrentDateTime();
			customerDao.updateReceiveStatusCustomer(currentDateTime.toString(), VariableText.ORDER_RECEIVED_STATUS, order_id);
			
		  int cus_id = custDao.getCustomerIdByOrderId(order_id);
		  
		  //Wait query update fulltime , order
			
		}
		return ResponseEntity.ok(new Response<Map<String, Object>>(HttpStatus.OK.value(),messeage, dataMap));

	}		
}
