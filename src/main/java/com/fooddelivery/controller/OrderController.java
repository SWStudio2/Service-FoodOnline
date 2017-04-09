package com.fooddelivery.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.Model.DeliveryRate;
import com.fooddelivery.Model.DeliveryRateDao;
import com.fooddelivery.Model.OrderDetail;
import com.fooddelivery.Model.Orders;
import com.fooddelivery.Model.OrdersDao;
import com.fooddelivery.Model.OrdersDetailDao;
import com.fooddelivery.Model.SequenceOrders;
import com.fooddelivery.Model.SequenceOrdersDao;
import com.fooddelivery.json.model.placeorder.PlaceOrder;
import com.fooddelivery.json.model.placeorder.allOrder;
import com.fooddelivery.json.model.placeorder.merchant;
import com.fooddelivery.json.model.placeorder.order;
import com.fooddelivery.util.Response;
import com.fooddelivery.util.VariableText;

@RestController
public class OrderController {
	
	@Autowired
	private OrdersDao ordersDao;
	
	@Autowired
	private DeliveryRateDao deliveryRateDao;
	
	@Autowired
	private OrdersDetailDao orderDetailDao;
	
	@Autowired
	private SequenceOrdersDao sequenceOrdersDao;
	
	@RequestMapping(value="/service/orders/inserorders" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response<Map<String, Object>>> insertOrders(@RequestBody PlaceOrder placeOrder
			/*@RequestBody Map<String, Object> mapRequest*/){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			/*print param*/
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			dateFormat.setLenient(false);
			Date date = new Date();
			allOrder allorder = placeOrder.getAllOrder();
			
			List<DeliveryRate> deliveryRateList = deliveryRateDao.findAllDeliveryRate();
			int deliveryRate;
			if (deliveryRateList != null) {
				deliveryRate = Math.round(deliveryRateList.get(0).getDeliveryRate());
			}
			else {
				deliveryRate = 0;
			}
			
			Orders orders = new Orders();
			orders.setOrderCusId(placeOrder.getCusId());
			orders.setOrderAddress(allorder.getOrderAddress());
			orders.setOrderAddressLatitude(allorder.getOrderAddressLatitude());
			orders.setOrderAddressLongtitude(allorder.getOrderAddressLongitude());
			orders.setOrderDatetime(date);
			orders.setOrderDatetimeDelivery(date);
			orders.setOrderDeliveryRate(deliveryRate);
			orders.setOrderTotalPrice(allorder.getOrderTotalPrice());
			orders.setOrderDistance(allorder.getOrderDistance());
			orders.setOrderFoodPrice(allorder.getOrderFoodPrice());
			orders.setOrderDeliveryPrice(allorder.getOrderDeliveryPrice());
			orders.setOrderStatus(VariableText.ORDER_PENDING_STATUS);
			//confirm code
			String confirmOrderCode = generateOrderConfirmCode();
			orders.setOrderConfirmCode(confirmOrderCode);
			ordersDao.save(orders);
			
			//System.out.println("id: " + orders.getOrderId());
			
			//orderDetail & sequenceOrderDetail
			List<merchant> merchants = allorder.getMerchant();
			for (int i=0; i<merchants.size(); i++) {
				merchant merchant = merchants.get(i);
				List<order> orderList = merchant.getOrder();
				for (int j=0; j<orderList.size(); j++) {
					order order = orderList.get(j);
					OrderDetail orderDetail = new OrderDetail();
					orderDetail.setOrderId(orders.getOrderId());
					orderDetail.setOrderDetailAmount(order.getOrderDetailAmount());
					orderDetail.setOrderRemark(order.getRemark());
					orderDetail.setMenuId(order.getMenuId());
					orderDetail.setMerId(merchant.getMerId());
					orderDetailDao.save(orderDetail);
					
					SequenceOrders sequenceOrders = new SequenceOrders();
					sequenceOrders.setSequenceOrderId(orders.getOrderId());
					sequenceOrders.setSequenceOrderMerchantId(merchant.getMerId());
					sequenceOrders.setSequenceCookStatus(VariableText.ORDER_WAIT_STATUS);
					sequenceOrdersDao.save(sequenceOrders);
					
				}
			}
			
			dataMap.put("orderNo",orders.getOrderId());
			dataMap.put("orderConfirmCode", confirmOrderCode);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		return ResponseEntity.ok(new Response<Map<String, Object>>(HttpStatus.OK.value(),"Inserted order sucessfully", dataMap));

	}
	
	private String generateOrderConfirmCode() {
		String result = "";
		Random rand = new Random();
		int val1 = rand.nextInt(9);
		int val2 = rand.nextInt(9);
		int val3 = rand.nextInt(9);
		int val4 = rand.nextInt(9);
		
		result = String.valueOf(val1) + String.valueOf(val2) + String.valueOf(val3) + String.valueOf(val4);
		
		return result;
	}
	
//	@RequestMapping(value="/service/orders/getOrderDetail" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public ResponseEntity<Response<Map<String, Object>>> getOrderDetail(@RequestBody String cusId , String isCurrentOrder){
//		Map<String, Object> dataMap = new HashMap<String, Object>();
//		try {
//			
//			
//			
//			return null;
//		}catch (Exception e){
//			System.out.println(e.getMessage());
//			return null;
//		}
//	}
}
