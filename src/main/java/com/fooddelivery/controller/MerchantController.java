package com.fooddelivery.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.Model.DeliveryRateDao;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsDao;
import com.fooddelivery.Model.OrdersDao;
import com.fooddelivery.Model.OrdersDetailDao;
import com.fooddelivery.Model.OrdersDetailOptionDao;
import com.fooddelivery.Model.SequenceOrders;
import com.fooddelivery.Model.SequenceOrdersDao;
import com.fooddelivery.util.DateTime;
import com.fooddelivery.util.Response;
import com.fooddelivery.util.VariableText;


@RestController
public class MerchantController {
	private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);
	// Wire the UserDao used inside this controller.
	@Autowired
	private MerchantsDao merchantsDao;
	
	@Autowired
	private SequenceOrdersDao sequenceOrderDao;
	
	@Autowired
	private OrdersDao ordersDao;

	final static int RECEIVESTATUS = 14;	
	
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
	    	logger.info(ex.getMessage());
	    	return null;
	    }
		
	}
	
	@RequestMapping(value="/service/merchant/confirmcode" , method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Response<Map<String, Object>>> confirmCode(){
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		long orderId = 502;
		long merId = 1; //2
		String confirmCode = "4143"; //4143-1 4576-2
		
		try {
			List<Integer> results = sequenceOrderDao.checkConfirmCode(orderId, merId, confirmCode);
			if (results.size() == 0) {
				return null;
			}
			else {
				//update
				SequenceOrders sequenceOrder = sequenceOrderDao.getSequenceOrderById(Long.valueOf(results.get(0)));
				sequenceOrder.setSequenceReceiveDatetime(DateTime.getCurrentDateTime());
				sequenceOrder.setSequenceReceiveStatus(VariableText.MERCHANT_RECEIVED_STATUS);
				sequenceOrderDao.save(sequenceOrder);
			}
			dataMap.put("result",results.get(0));
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		
		return ResponseEntity.ok(new Response<Map<String, Object>>(HttpStatus.OK.value(),"Confirm code from Merchant successfully", dataMap));
	}
	
	@RequestMapping(value="/service/orders/confirmcode/merchant" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response<Map<String, Object>>> verifyConfirmCodeMerchant(@RequestBody Map<String, Object> mapRequest)
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String messeage = "";
		try {
		  int order_id = (Integer) mapRequest.get("order_id");
		  String seqor_confirm_code = (String) mapRequest.get("seqor_confirm_code");
		  int mer_id = (Integer) mapRequest.get("mer_id");
		  String result = ordersDao.verifyConfirmCodeMerchant(order_id, mer_id, seqor_confirm_code);
		  String resultVerify = "";
		  dataMap.put("isPass", "Y");
		  dataMap.put("order_id", order_id);
		  if(result.equals("Y"))
		  {
			  resultVerify = "correct confirm code";
			  Date currentDateTime = DateTime.getCurrentDateTime();
			  ordersDao.updateReceiveStatus(currentDateTime.toString(), RECEIVESTATUS, order_id, mer_id);
		  }
		  else
		  {
			  resultVerify = "incorrect confirm code";
		  }
		  dataMap.put("seqor_receive_status", resultVerify);
		  messeage = "Call seqor confirm code successfully";
		}
		catch (Exception e) {
			logger.info(e.getMessage());
			dataMap.put("isPass", "N");
			messeage = "Call seqor confirm code unsuccessfully";
			return null;
		}
		return ResponseEntity.ok(new Response<Map<String, Object>>(HttpStatus.OK.value(),messeage, dataMap));

	}		
	
}
