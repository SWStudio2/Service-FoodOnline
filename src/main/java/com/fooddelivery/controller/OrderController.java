package com.fooddelivery.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.fooddelivery.Model.MerchantsDao;
import com.fooddelivery.Model.OrderDetail;
import com.fooddelivery.Model.Orders;
import com.fooddelivery.Model.OrdersDao;
import com.fooddelivery.Model.OrdersDetailDao;
import com.fooddelivery.Model.OrdersDetailOption;
import com.fooddelivery.Model.OrdersDetailOptionDao;
import com.fooddelivery.Model.SequenceOrders;
import com.fooddelivery.Model.SequenceOrdersDao;
import com.fooddelivery.Model.Order.CustOrder;
import com.fooddelivery.Model.Order.MerchantOrder;
import com.fooddelivery.Model.Order.OrderHeaderAndDetail;
import com.fooddelivery.Model.Order.OrderOptionDetail;
import com.fooddelivery.Model.Order.SubOrder;
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

	final static int RECEIVESTATUS = 14;
	
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
			orders.setOrderStatus(VariableText.ORDER_WAITING_RESPONSE_STATUS);
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
						sequenceOrders.setSequenceCookStatus(VariableText.ORDER_WAITING_RESPONSE_STATUS);
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
			dataMap.put("orderStatus", orders.getOrderStatus());
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
	public ResponseEntity<Response<List<CustOrder>>> getOrderDetail(@RequestBody Map<String, String> json){
		Long cusId = Long.valueOf(json.get("cusId"));
		String isCurrentOrder = json.get("isCurrentOrder");
		
//		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			//final output
			List<CustOrder> custOrderList = new ArrayList<CustOrder>();
			
			List<Orders> orderList = new ArrayList<Orders>();
			if(isCurrentOrder.equalsIgnoreCase("Y")){
				orderList = ordersDao.findLastOrderCusId(cusId);
			}else{
				orderList = ordersDao.findByOrderCusId(cusId);
			}
			
			System.out.println("Size of orders : "+orderList.size());
			//Loop orderList
			for(int i=0;i<orderList.size();i++){
				System.out.println("============= order : "+i+" : "+orderList.get(i).getOrderId()+"============================");
				CustOrder custOrder = new CustOrder();
				
				List<Object[]> odList = new ArrayList<Object[]>();
				
					odList = orderDetailDao.findByOrderId(orderList.get(i).getOrderId());
				
				//convert List<Object[]> to List<OrderHeaderAndDetail>
				List<OrderHeaderAndDetail> orderHeadAndDetList = new ArrayList<OrderHeaderAndDetail>();
				if(odList != null){
					for(Object[] objectArray : odList){
						OrderHeaderAndDetail result = new OrderHeaderAndDetail();
						result.setOrder_id(((Integer) objectArray[0]).longValue());
//						System.out.println("1");
						result.setOrder_cus_id(((Integer) objectArray[1]).longValue());
//						System.out.println("2");
						result.setOrder_address((String) objectArray[2]);
//						System.out.println("3");
						result.setOrder_address_latitude((String) objectArray[3]);
//						System.out.println("4");
						result.setOrder_address_longtitude((String) objectArray[4]);
//						System.out.println("5");
						result.setOrder_created_datetime((Date) objectArray[5]);
//						System.out.println("6");
						result.setOrder_receive_datetime((Date) objectArray[6]);
//						System.out.println("7");
						result.setOrder_delivery_rate((Integer) objectArray[7]);
//						System.out.println("8");
						result.setOrder_total_price((Double) objectArray[8]);
//						System.out.println("9");
						result.setOrder_distance((Double) objectArray[9]);
//						System.out.println("10");
						result.setOrder_status((Integer) objectArray[10]);
//						System.out.println("11");
						result.setOrder_food_price((Double) objectArray[11]);
//						System.out.println("12");
						result.setOrder_delivery_price((Double) objectArray[12]);
//						System.out.println("13");
						result.setOrder_confirm_code((String) objectArray[13]);
//						System.out.println("14");
						result.setOrder_estimate_time((Integer) objectArray[14]);
//						System.out.println("15");
						result.setOrder_estimate_datetime((Date) objectArray[15]);
//						System.out.println("16");
						result.setMenu_name((String) objectArray[16]);
//						System.out.println("17");
						result.setMenu_price((Float) objectArray[17]);
//						System.out.println("18");
						result.setOrder_detail_amount((Integer) objectArray[18]);
//						System.out.println("19");
						result.setOrder_remark((String) objectArray[19]);
//						System.out.println("20");
						result.setOption_total_price((Double) objectArray[20]);
//						System.out.println("21");
						result.setMenu_total_price((Double) objectArray[21]);
//						System.out.println("22");
						orderHeadAndDetList.add(result);
					}
				}
				
				custOrder.setConfirmCode(orderList.get(i).getOrderConfirmCode());
				custOrder.setEstimatedTime(orderList.get(i).getOrderEstimateTime());
				custOrder.setOrderAddress(orderList.get(i).getOrderAddress());
				custOrder.setOrderAddressLatitude(orderList.get(i).getOrderAddressLatitude());
				custOrder.setOrderAddressLongitude(orderList.get(i).getOrderAddressLongtitude());
				custOrder.setOrderCreatedDateTime(orderList.get(i).getOrderCreatedDatetime().toString());
				custOrder.setOrderDeliveryPrice(orderList.get(i).getOrderDeliveryPrice());
				custOrder.setOrderDistance(orderList.get(i).getOrderDistance());
				custOrder.setOrderFoodPrice(orderList.get(i).getOrderFoodPrice());
				custOrder.setOrderNo(String.valueOf(orderList.get(i).getOrderId()));
				custOrder.setOrderStatus(orderList.get(i).getOrderStatus());
				custOrder.setOrderTotalPrice(orderList.get(i).getOrderTotalPrice());
				
				
				List<MerchantOrder> resultOrderMerchantList = new ArrayList<MerchantOrder>();
				for(int j=0;j<orderHeadAndDetList.size();j++){
					System.out.println("orderDetail : "+j+" "+orderHeadAndDetList.get(j).getOrder_id());
					
					List<Object[]> merchantList = merchantsDao.findSpecialByOrderId(orderHeadAndDetList.get(j).getOrder_id());
					
					//convert List<Object[]> to List<OrderHeaderAndDetail>
					MerchantOrder orderMer  = new MerchantOrder();
					for(Object[] objectArray : merchantList){
						
						orderMer.setMerid(((Integer) objectArray[0]));
//						System.out.println("-1");
						orderMer.setMerLatitude(((String) objectArray[1]));
//						System.out.println("-2");
						orderMer.setMerLongitude(((String) objectArray[2]));
//						System.out.println("-3");
						orderMer.setMerDistance(((BigDecimal) objectArray[3]));
//						System.out.println("-4");
						orderMer.setMerDeliveryPrice(((BigDecimal) objectArray[4]));
//						System.out.println("-5");
						orderMer.setMerFoodPrice(((Double) objectArray[5]));
//						System.out.println("-6");
						
						resultOrderMerchantList.add(orderMer);
						
						
					}
					
					
				List<MerchantOrder> resultOrderMerchantListClone = new ArrayList<MerchantOrder>();
				List<Integer> merIdTempList = new ArrayList<Integer>();
				for(int m=0;m<resultOrderMerchantList.size();m++){
					if(m==0){			
						merIdTempList.add(resultOrderMerchantList.get(m).getMerid());
						resultOrderMerchantListClone.add(resultOrderMerchantList.get(m));
					}else{
						if(! merIdTempList.contains(resultOrderMerchantList.get(m).getMerid())){
							merIdTempList.add(resultOrderMerchantList.get(m).getMerid());
							resultOrderMerchantListClone.add(resultOrderMerchantList.get(m));
						}
					}
				}
				
				
				List<SubOrder> resultOrderDetList = new ArrayList<SubOrder>();	
				for(int k=0;k<resultOrderMerchantListClone.size();k++){	
					System.out.println("Merchant : "+k+" "+resultOrderMerchantListClone.get(k).getMerid());
					List<Object[]> odDetList = orderDetailDao.findByOrderIdAndMerId(orderList.get(i).getOrderId(),Long.valueOf(resultOrderMerchantList.get(k).getMerid()));
					
					//convert List<Object[]> 
					int count =0;
					for(Object[] objectArray : odDetList){
						System.out.println("OrderDetail : "+count+" "+(Integer) objectArray[0]);
						count++;
						SubOrder orderDet  = new SubOrder();
						orderDet.setMenuId((Integer) objectArray[0]);
//						System.out.println("--1");
						orderDet.setMenuPrice((Float) objectArray[1]);
//						System.out.println("--2");
						orderDet.setOrderDetailId((Integer) objectArray[2]);
//						System.out.println("--3");
						orderDet.setOrderDetailAmount((Integer) objectArray[3]);
//						System.out.println("--4");
						orderDet.setRemark((String) objectArray[4]);
//						System.out.println("--5");
						
						List<OrderOptionDetail> resultOrderOptList = new ArrayList<OrderOptionDetail>();
						List<Object[]> odOptList = ordersDetailOptionsDao.findByOrderId(Long.valueOf(orderDet.getOrderDetailId()));
						
						//convert List<Object[]> 
						int count2 = 0;
						for(Object[] objectArray2 : odOptList){
							System.out.println("OrderOption : "+count2+" "+(Integer) objectArray2[0]);
							count2++;
							OrderOptionDetail orderOpt  = new OrderOptionDetail();
							orderOpt.setOrderDetailId(((Integer) objectArray2[0]));
//							System.out.println("---1");
							orderOpt.setOptionName(((String) objectArray2[1]));
//							System.out.println("---2");
							orderOpt.setOptionPrice(((Float) objectArray2[2]));
//							System.out.println("---3");
							resultOrderOptList.add(orderOpt);
//							System.out.println("---4");
							
							resultOrderOptList.add(orderOpt);
						}
						
						orderDet.setOptionList(resultOrderOptList);
						resultOrderDetList.add(orderDet);
					}
					resultOrderMerchantListClone.get(k).setSubOrderList(resultOrderDetList);
				}
				
				custOrder.setMerchantOrderList(resultOrderMerchantListClone);	
				

			}
				
			custOrderList.add(custOrder);
			logger.info(String.valueOf(custOrderList.size()));
		}
			
			return ResponseEntity.ok(new Response<List<CustOrder>>(HttpStatus.OK.value(),"Query order sucessfully", custOrderList));
		}catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	
}
