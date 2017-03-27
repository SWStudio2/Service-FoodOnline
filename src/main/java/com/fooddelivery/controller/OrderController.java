package com.fooddelivery.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.Model.OrdersDao;
import com.fooddelivery.json.model.placeorder.PlaceOrder;
import com.fooddelivery.json.model.placeorder.allOrder;
import com.fooddelivery.util.Response;

@RestController
public class OrderController {
	
	@Autowired
	private OrdersDao ordersDao;
	
	private String ORDER_STATUS = "Pending";
	
	@RequestMapping(value="/service/orders/inserorders" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response<Map<String, Object>>> insertOrders(@RequestBody PlaceOrder placeOrder
			/*@RequestBody Map<String, Object> mapRequest*/){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String result;
		try {
			/*print param*/
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			dateFormat.setLenient(false);
			Date date = new Date();
			allOrder allorder = placeOrder.getAllOrder();
			
			ordersDao.insertOrder(placeOrder.getCusId(), allorder.getOrderAddress(), 
					allorder.getOrderAddressLatitude(), allorder.getOrderAddressLongitude(), 
					dateFormat.format(date), dateFormat.format(date), 
					10, allorder.getOrderPrice(), 25.5, ORDER_STATUS); //10 --- rate???
			
			
			  result = String.valueOf(placeOrder.getCusId());
			  dataMap.put("result",result);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		return ResponseEntity.ok(new Response<Map<String, Object>>(HttpStatus.OK.value(),"Result", dataMap));

	}

}
