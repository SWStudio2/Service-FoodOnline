package com.fooddelivery.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

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

import com.fooddelivery.Model.DeliveryRate;
import com.fooddelivery.Model.DeliveryRateDao;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsDao;
import com.fooddelivery.Model.OrderDetail;
import com.fooddelivery.Model.OrdersDetailOption;
import com.fooddelivery.Model.Orders;
import com.fooddelivery.Model.OrdersDao;
import com.fooddelivery.Model.OrdersDetailDao;
import com.fooddelivery.Model.OrdersDetailOption;
import com.fooddelivery.Model.OrdersDetailOptionDao;
import com.fooddelivery.Model.SequenceOrders;
import com.fooddelivery.Model.SequenceOrdersDao;
import com.fooddelivery.Model.Order.CustOrder;
import com.fooddelivery.Model.Order.OrderHeaderAndDetail;
import com.fooddelivery.Model.Order.OrderMerchant;
import com.fooddelivery.Model.Order.OrderOptionDetail;
import com.fooddelivery.json.model.placeorder.PlaceOrder;
import com.fooddelivery.json.model.placeorder.allOrder;
import com.fooddelivery.json.model.placeorder.merchant;
import com.fooddelivery.json.model.placeorder.option;
import com.fooddelivery.json.model.placeorder.order;
import com.fooddelivery.util.DateTime;
import com.fooddelivery.util.Response;
import com.fooddelivery.util.VariableText;

@RestController
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private OrdersDao ordersDao;
	
	@Autowired
	private MerchantsDao merchantsDao;
	
	@Autowired
	private DeliveryRateDao deliveryRateDao;
	
	@Autowired
	private OrdersDetailDao orderDetailDao;
	
	@Autowired
	private SequenceOrdersDao sequenceOrdersDao;
	
	@Autowired
	private OrdersDetailOptionDao ordersDetailOptionsDao;

	
	@RequestMapping(value="/service/orders/inserorders" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response<Map<String, Object>>> insertOrders(@RequestBody PlaceOrder placeOrder
			/*@RequestBody Map<String, Object> mapRequest*/){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			/*print param*/
			/*DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
			dateFormat.setLenient(false);
			Date date = new Date();*/
			Date currentDateTime = DateTime.getCurrentDateTime();
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
			orders.setOrderCreatedDatetime(currentDateTime);
			orders.setOrderDeliveryRate(deliveryRate);
			orders.setOrderTotalPrice(allorder.getOrderTotalPrice());
			orders.setOrderDistance(allorder.getOrderDistance());
			orders.setOrderFoodPrice(allorder.getOrderFoodPrice());
			orders.setOrderDeliveryPrice(allorder.getOrderDeliveryPrice());
			orders.setOrderEstimateTime(allorder.getOrderEstimateTime());
			orders.setOrderEstimateDatetime(DateTime.getDateTime(allorder.getOrderEstimateDateTime()));
			orders.setOrderStatus(VariableText.ORDER_PENDING_STATUS);
			//confirm code
			String confirmOrderCode = generateOrderConfirmCode();
			orders.setOrderConfirmCode(confirmOrderCode);
			ordersDao.save(orders);
			
			//logger.info("id: " + orders.getOrderId());
			
			//orderDetail & sequenceOrderDetail
			List<merchant> merchants = allorder.getMerchant();
			for (int i=0; i<merchants.size(); i++) {
				merchant merchant = merchants.get(i);
				List<order> orderList = merchant.getOrder();
				
				HashMap<String, String> merchantIds = new HashMap<String, String>();
				
				for (int j=0; j<orderList.size(); j++) {
					order order = orderList.get(j);
					OrderDetail orderDetail = new OrderDetail();
					orderDetail.setOrderId(orders.getOrderId());
					orderDetail.setOrderDetailAmount(order.getOrderDetailAmount());
					orderDetail.setOrderRemark(order.getRemark());
					orderDetail.setMenuId(order.getMenuId());
					orderDetail.setMerId(merchant.getMerId());
					orderDetailDao.save(orderDetail);
					
					if (order.getOption() != null) {
						for (int k=0; k<order.getOption().size(); k++) {
							option option = order.getOption().get(k);
							OrdersDetailOption orderDetailOptions = new OrdersDetailOption();
							orderDetailOptions.setOptionId(option.getOptionId());
							orderDetailOptions.setOrderDetailId(orderDetail.getOrderDetailId());
							ordersDetailOptionsDao.save(orderDetailOptions);
						}
					}
					
					if (merchantIds.get(String.valueOf(merchant.getMerId())) == null) {
						SequenceOrders sequenceOrders = new SequenceOrders();
						sequenceOrders.setSequenceOrderId(orders.getOrderId());
						sequenceOrders.setSequenceOrderMerchantId(merchant.getMerId());
						sequenceOrders.setSequenceCookStatus(VariableText.ORDER_WAIT_STATUS);
						sequenceOrders.setSequenceMerDistance(Double.valueOf(merchant.getMerDistance()));
						String confirmSequenceOrderCode = generateOrderConfirmCode();
						sequenceOrders.setSequenceConfirmCode(confirmSequenceOrderCode);
						sequenceOrdersDao.save(sequenceOrders);
						merchantIds.put(String.valueOf(merchant.getMerId()), String.valueOf(merchant.getMerId()));
					}
				}
			}
			
			dataMap.put("orderNo",orders.getOrderId());
			dataMap.put("orderConfirmCode", confirmOrderCode);
		}
		catch (Exception e) {
			logger.info(e.getMessage());
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
	
	@RequestMapping(value="/service/orders/getOrderDetail" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response<List<OrderOptionDetail>>> getOrderDetail(@RequestBody Map<String, String> json){
		Long cusId = Long.valueOf(json.get("cusId"));
		String isCurrentOrder = json.get("isCurrentOrder");
		
//		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			//final output
			List<CustOrder> custOrderList = new ArrayList<CustOrder>();
			
			List<Orders> orderList = ordersDao.findByOrderCusId(cusId);
			//Loop orderList 
			List<Object[]> odList = orderDetailDao.findByOrderId(2L);
			//convert List<Object[]> to List<OrderHeaderAndDetail>
			List<OrderHeaderAndDetail> resultList = new ArrayList<OrderHeaderAndDetail>();
			if(odList != null){
				System.out.print(odList.size());
				for(Object[] objectArray : odList){
					OrderHeaderAndDetail result = new OrderHeaderAndDetail();
					result.setOrder_id(((Integer) objectArray[0]).longValue());
					result.setOrder_cus_id(((Integer) objectArray[1]).longValue());
					result.setOrder_address((String) objectArray[2]);
					result.setOrder_address_latitude((String) objectArray[3]);
					result.setOrder_address_longtitude((String) objectArray[4]);
					result.setOrder_created_datetime((Date) objectArray[5]);
					result.setOrder_receive_datetime((Date) objectArray[6]);
					result.setOrder_delivery_rate((Integer) objectArray[7]);
					result.setOrder_total_price((Double) objectArray[8]);
					result.setOrder_distance((Double) objectArray[9]);
					result.setOrder_status((String) objectArray[10]);
					result.setOrder_food_price((Double) objectArray[11]);
					result.setOrder_delivery_price((Double) objectArray[12]);
					result.setOrder_confirm_code((String) objectArray[13]);
					result.setOrder_estimate_time((Integer) objectArray[14]);
					result.setOrder_estimate_datetime((Date) objectArray[15]);
					result.setMenu_name((String) objectArray[16]);
					result.setMenu_price((Float) objectArray[17]);
					result.setOrder_detail_amount((Integer) objectArray[18]);
					result.setOrder_remark((String) objectArray[19]);
					result.setOption_total_price((Double) objectArray[20]);
					result.setMenu_total_price((Double) objectArray[21]);
					resultList.add(result);
				}
			}
			
			
			//Loop odList
			List<Object[]> merchantList = merchantsDao.findSpecialByOrderId(2L);
			//convert List<Object[]> to List<OrderHeaderAndDetail>
			List<OrderMerchant> resultOrderMerchantList = new ArrayList<OrderMerchant>();
			for(Object[] objectArray : merchantList){
				OrderMerchant orderMer  = new OrderMerchant();
				orderMer.setMerName(((String) objectArray[0]));
				orderMer.setMerLat(((String) objectArray[1]));
				orderMer.setMerLng(((String) objectArray[2]));
				orderMer.setSeqMerDistance(((Double) objectArray[3]));
				orderMer.setDeliveryPrice(((Double) objectArray[4]));
				orderMer.setFoodPrice(((Double) objectArray[5]));
				
				resultOrderMerchantList.add(orderMer);
			}
			
			//Loop merchantList
			List<Object[]> odOptList = ordersDetailOptionsDao.findByOrderId(412L);
			//convert List<Object[]> to List<OrderHeaderAndDetail>
			List<OrderOptionDetail> resultOrderOptList = new ArrayList<OrderOptionDetail>();
			for(Object[] objectArray : odOptList){
				OrderOptionDetail orderOpt  = new OrderOptionDetail();
				orderOpt.setOptionName(((String) objectArray[0]));
//				orderOpt.setOptionPrice(((Double) objectArray[1]));
//				orderOpt.setOrderDetailId(((String) objectArray[2]));
				resultOrderOptList.add(orderOpt);
			}
			
			//{2, 1, Test_Address, 10.1326874, 10.1326874, 2017-03-23 21:00:00.0, null, 10, 300.0, 10.5, null, null, null, null, null, null, นมขาว, 50.0, 2, -, 0.0, 100.0}'; nested exception is org.springframework.core.convert.ConverterNotFoundException: No converter found capable of converting from type [java.lang.Integer] to type [com.fooddelivery.Model.Order.OrderHeaderAndDetail]
			/*	 1,ORDER_ID,ad_4e5de0567b1e3fc,orders,INT,binary,10,1,0
				 2,ORDER_CUS_ID,ad_4e5de0567b1e3fc,orders,INT,binary,10,1,0
				 3,ORDER_ADDRESS,ad_4e5de0567b1e3fc,orders,VARCHAR,utf8,50,12,0
				 4,ORDER_ADDRESS_LATITUDE,ad_4e5de0567b1e3fc,orders,VARCHAR,utf8,255,10,0
				 5,ORDER_ADDRESS_LONGTITUDE,ad_4e5de0567b1e3fc,orders,VARCHAR,utf8,255,10,0
				 6,ORDER_CREATED_DATETIME,ad_4e5de0567b1e3fc,orders,DATETIME,binary,19,19,0
				 7,ORDER_RECEIVE_DATETIME,ad_4e5de0567b1e3fc,orders,DATETIME,binary,19,0,0
				 8,ORDER_DELIVERY_RATE,ad_4e5de0567b1e3fc,orders,INT,binary,4,2,0
				 9,ORDER_TOTAL_PRICE,ad_4e5de0567b1e3fc,orders,DOUBLE,binary,10,4,2
				10,ORDER_DISTANCE,ad_4e5de0567b1e3fc,orders,DOUBLE,binary,10,3,2
				11,ORDER_STATUS,ad_4e5de0567b1e3fc,orders,VARCHAR,utf8,50,0,0
				12,ORDER_FOOD_PRICE,ad_4e5de0567b1e3fc,orders,DOUBLE,binary,10,-2,2
				13,ORDER_DELIVERY_PRICE,ad_4e5de0567b1e3fc,orders,DOUBLE,binary,10,-2,2
				14,ORDER_CONFIRM_CODE,ad_4e5de0567b1e3fc,orders,VARCHAR,utf8,255,0,0
				15,ORDER_ESTIMATE_TIME,ad_4e5de0567b1e3fc,orders,INT,binary,11,0,0
				16,ORDER_ESTIMATE_DATETIME,ad_4e5de0567b1e3fc,orders,DATETIME,binary,19,0,0
				17,menu_name,ad_4e5de0567b1e3fc,menu,VARCHAR,utf8,50,45,0
				18,menu_price,ad_4e5de0567b1e3fc,menu,FLOAT,binary,5,3,2
				19,order_detail_amount,ad_4e5de0567b1e3fc,order_detail,INT,binary,10,1,0
				20,order_remark,ad_4e5de0567b1e3fc,order_detail,VARCHAR,utf8,100,1,0
				21,option_total_price,,,DOUBLE,binary,19,2,2
				22,menu_total_price,,,DOUBLE,binary,19,4,2

			 */
			/*
			"o.*,"
		      +"m.menu_name,"
		      +"m.menu_price,"
		      +"od.order_detail_amount,"
		      +"od.order_remark,"
		       +"IFNULL(sop.sum_option,0) as option_total_price,"
		      +"((m.menu_price * od.order_detail_amount) +  IFNULL(sop.sum_option,0) ) as menu_total_price"
			*/
//			List<Merchants> merchantList = merchantsDao.findByOrderId(2L);
//			System.out.println(merchantList);
			
//			Orders o = orderList.get(0);
//			List<OrderDetail> odList = o.getOrderDetails();
//			System.out.println(odList);
//			
//			List<Merchants> merchantList = merchantsDao.findByOrderId(2L);
//			System.out.println(merchantList);
//			
//			List<OrderDetail> odListById = orderDetailDao.findByOrderIdAndMerId(2L, 4L );
//			System.out.println(odListById.get(0));
//			
//			List<OrdersDetailOption> odOptList = orderDetailOptionDao.findByOrderId(412L);
//			System.out.println(odOptList.get(0));
			
//			if(orderList != null){
//				for(int i=0;i<orderList.size();i++){
//					CustOrder custOrder = new CustOrder();
//					custOrder.setOrderAddressLatitude(orderList.get(i).getOrderAddressLatitude());
//					custOrder.setOrderAddressLongitude(orderList.get(i).getOrderAddressLongtitude());
//					custOrder.setOrderTotalPrice(orderList.get(i).getOrderTotalPrice());
//					custOrder.setOrderFoodPrice(orderList.get(i).getOrderFoodPrice());
//					custOrder.setOrderDeliveryPrice(orderList.get(i).getOrderDeliveryPrice());
//					custOrder.setOrderDistance(orderList.get(i).getOrderDistance());
//					custOrder.setEstimatedTime(orderList.get(i).getOrderEstimateTime());
//					custOrder.setConfirmCode(orderList.get(i).getOrderConfirmCode());
//					custOrder.setOrderNo(String.valueOf(orderList.get(i).getOrderId()));
//					custOrder.setOrderStatus(orderList.get(i).getOrderStatus());
//					custOrder.setOrderCreatedDateTime(orderList.get(i).getOrderCreatedDatetime().toString());
//					
//					List<Merchants> merchantList = merchantsDao.findByOrderId(orderList.get(i).getOrderId());
//					if(){
//						
//					}
//				}
//			}
			
			
			return ResponseEntity.ok(new Response<List<OrderOptionDetail>>(HttpStatus.OK.value(),"Query order sucessfully", resultOrderOptList));
		}catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}

}
