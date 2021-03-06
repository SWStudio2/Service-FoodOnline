package com.fooddelivery.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.management.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.Model.DeliveryRate;
import com.fooddelivery.Model.DeliveryRateDao;
import com.fooddelivery.Model.MerchantsDao;
import com.fooddelivery.Model.NotificationInbox;
import com.fooddelivery.Model.NotificationInboxDao;
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
import com.fooddelivery.util.Utils;
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

	@Autowired
	private NotificationInboxDao notiDao;

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
			orders.setOrderPaymentType(allorder.getOrderPaymentType());
			//confirm code
			String confirmOrderCode = generateOrderConfirmCode();
			orders.setOrderConfirmCode(confirmOrderCode);
			orders = ordersDao.save(orders);

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
					orderDetail.setOrderDetailStatus(VariableText.Y_FLAG);
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
						sequenceOrders.setSequenceCookStatus(VariableText.MERCHANT_WAITING_CONFIRM_STATUS);
						sequenceOrders.setSequenceMerDistance(Double.valueOf(merchant.getMerDistance()));
						sequenceOrders.setSequenceReceiveStatus(VariableText.MESSENGER_WAITING_CONFIRM_STATUS);
						String confirmSequenceOrderCode = generateOrderConfirmCode();
						sequenceOrders.setSequenceConfirmCode(confirmSequenceOrderCode);
						sequenceOrdersDao.save(sequenceOrders);
						//insert noti
						NotificationInbox notificationInbox = new NotificationInbox();
						notificationInbox.setNoti_ref_id(Utils.toIntExact(merchant.getMerId()));
						notificationInbox.setNoti_type(VariableText.NOTIFICATION_TYPE_MERCHANT);
						notificationInbox.setNoti_order_id(Utils.toIntExact(orders.getOrderId()));
						notificationInbox.setNoti_message_type(VariableText.NOTIFICATION_MSG_TYPE_ACKNOWLEDGE);
						notificationInbox.setNoti_message_detail(
								VariableText.NOTIFICATION_MSG_DETAIL_CREATE_ORDER + " " + orders.getOrderId());
						notificationInbox.setNoti_created_date(currentDateTime);
						notiDao.save(notificationInbox);

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

//	@RequestMapping(value="/service/orders/getOrderDetail" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public ResponseEntity<Response<List<CustOrder>>> getOrderDetail(@RequestBody Map<String, String> json){
//		Long cusId = Long.valueOf(json.get("cusId"));
//		String isCurrentOrder = json.get("isCurrentOrder");
//
////		Map<String, Object> dataMap = new HashMap<String, Object>();
//		try {
//			//final output
//			List<CustOrder> custOrderList = new ArrayList<CustOrder>();
//
//			List<Orders> orderList = new ArrayList<Orders>();
//			if(isCurrentOrder.equalsIgnoreCase("Y")){
//				orderList = ordersDao.findLastOrderCusId(cusId);
//			}else{
//				orderList = ordersDao.findByOrderCusId(cusId);
//			}
//
//			System.out.println("Size of orders : "+orderList.size());
//			//Loop orderList
//			for(int i=0;i<orderList.size();i++){
//				System.out.println("============= order : "+i+" : "+orderList.get(i).getOrderId()+"============================");
//				CustOrder custOrder = new CustOrder();
//
//				List<Object[]> odList = new ArrayList<Object[]>();
//
//				odList = orderDetailDao.findByOrderId(orderList.get(i).getOrderId());
//
//				//convert List<Object[]> to List<OrderHeaderAndDetail>
//				List<OrderHeaderAndDetail> orderHeadAndDetList = new ArrayList<OrderHeaderAndDetail>();
//
//				if(odList != null){
//					for(Object[] objectArray : odList){
//						OrderHeaderAndDetail result = new OrderHeaderAndDetail();
//						result.setOrder_id(((Integer) objectArray[0]).longValue());
////						System.out.println("1");
//						result.setOrder_cus_id(((Integer) objectArray[1]).longValue());
////						System.out.println("2");
//						result.setOrder_address((String) objectArray[2]);
////						System.out.println("3");
//						result.setOrder_address_latitude((String) objectArray[3]);
////						System.out.println("4");
//						result.setOrder_address_longtitude((String) objectArray[4]);
////						System.out.println("5");
//						result.setOrder_created_datetime((Date) objectArray[5]);
////						System.out.println("6");
//						result.setOrder_receive_datetime((Date) objectArray[6]);
////						System.out.println("7");
//						result.setOrder_delivery_rate((Integer) objectArray[7]);
////						System.out.println("8");
//						result.setOrder_total_price((Double) objectArray[8]);
////						System.out.println("9");
//						result.setOrder_distance((Double) objectArray[9]);
////						System.out.println("10");
//						result.setOrder_status((Integer) objectArray[10]);
////						System.out.println("11");
//						result.setOrder_food_price((Double) objectArray[11]);
////						System.out.println("12");
//						result.setOrder_delivery_price((Double) objectArray[12]);
////						System.out.println("13");
//						result.setOrder_confirm_code((String) objectArray[13]);
////						System.out.println("14");
//						result.setOrder_estimate_time((Integer) objectArray[14]);
////						System.out.println("15");
//						result.setOrder_estimate_datetime((Date) objectArray[15]);
////						System.out.println("16");
//						result.setMenu_id((Integer) objectArray[16]);
////						System.out.println("17");
//						result.setMenu_name((String) objectArray[17]);
////						System.out.println("18");
//						result.setMenu_price((Float) objectArray[18]);
////						System.out.println("19");
//						result.setOrder_detail_amount((Integer) objectArray[19]);
////						System.out.println("20");
//						result.setOrder_remark((String) objectArray[20]);
////						System.out.println("21");
//						result.setOption_total_price((Double) objectArray[21]);
////						System.out.println("22");
//						result.setMenu_total_price((Double) objectArray[22]);
////						System.out.println("23");
//						orderHeadAndDetList.add(result);
//					}
//				}
//
//				custOrder.setConfirmCode(orderList.get(i).getOrderConfirmCode());
//				custOrder.setEstimatedTime(orderList.get(i).getOrderEstimateTime());
//				custOrder.setOrderAddress(orderList.get(i).getOrderAddress());
//				custOrder.setOrderAddressLatitude(orderList.get(i).getOrderAddressLatitude());
//				custOrder.setOrderAddressLongitude(orderList.get(i).getOrderAddressLongtitude());
//				custOrder.setOrderCreatedDateTime(orderList.get(i).getOrderCreatedDatetime().toString());
//				custOrder.setOrderDeliveryPrice(orderList.get(i).getOrderDeliveryPrice());
//				custOrder.setOrderDistance(orderList.get(i).getOrderDistance());
//				custOrder.setOrderFoodPrice(orderList.get(i).getOrderFoodPrice());
//				custOrder.setOrderNo(String.valueOf(orderList.get(i).getOrderId()));
//				custOrder.setOrderStatus(orderList.get(i).getOrderStatus());
//				custOrder.setOrderTotalPrice(orderList.get(i).getOrderTotalPrice());
//				custOrder.setEstimatedDateTime(orderList.get(i).getOrderEstimateDatetime().toString());
//
//			if(orderList.get(i).getOrderId() == 522){
//				List<MerchantOrder> resultOrderMerchantList = new ArrayList<MerchantOrder>();
//				for(int j=0;j<orderHeadAndDetList.size();j++){
//					System.out.println("orderDetail : "+j+" "+orderHeadAndDetList.get(j).getOrder_id());
//
//					List<Object[]> merchantList = merchantsDao.findSpecialByOrderId(orderHeadAndDetList.get(j).getOrder_id());
//
//					//convert List<Object[]> to List<OrderHeaderAndDetail>
//
//					for(Object[] objectArray : merchantList){
//						System.out.println("à¸à¸·à¹à¸­à¸£à¹à¸²à¸ : "+(String) objectArray[1]);
//						System.out.println("Merchant ID: "+(Integer) objectArray[0]);
//
//						MerchantOrder orderMer  = new MerchantOrder();
//						orderMer.setMerid(((Integer) objectArray[0]));
////						System.out.println("-1");
//						orderMer.setMerName(((String) objectArray[1]));
////						System.out.println("-1.1");
//						orderMer.setMerLatitude(((String) objectArray[2]));
////						System.out.println("-2");
//						orderMer.setMerLongitude(((String) objectArray[3]));
////						System.out.println("-3");
//						orderMer.setMerDistance(((BigDecimal) objectArray[4]));
////						System.out.println("-4");
//						orderMer.setMerDeliveryPrice(((BigDecimal) objectArray[5]));
////						System.out.println("-5");
//						orderMer.setMerFoodPrice(((Double) objectArray[6]));
////						System.out.println("-6");
//
//						resultOrderMerchantList.add(orderMer);
//					}
//
//					System.out.println("Merchants size : "+resultOrderMerchantList.size());
//
//					HashMap<Integer, MerchantOrder> resultOrderMercHashMap = new HashMap<Integer, MerchantOrder>();
//
//					for(int n=0;n<resultOrderMerchantList.size();n++){
//						resultOrderMercHashMap.put(resultOrderMerchantList.get(n).getMerid(), resultOrderMerchantList.get(n));
//					}
//
//					List<MerchantOrder> resultOrderMerchantListClone = new ArrayList<MerchantOrder>();
//					for(int m=0;m<resultOrderMerchantList.size();m++){
//						if(m == 0){
//							resultOrderMercHashMap.put(resultOrderMerchantList.get(m).getMerid(), resultOrderMerchantList.get(m));
//							resultOrderMerchantListClone.add(resultOrderMerchantList.get(m));
//						}else{
//							if(resultOrderMercHashMap.get(resultOrderMerchantList.get(m)) == null) {
//								System.out.println("not contain*********");
//								resultOrderMercHashMap.put(resultOrderMerchantList.get(m).getMerid(), resultOrderMerchantList.get(m));
//								resultOrderMerchantListClone.add(resultOrderMerchantList.get(m));
//							}
//						}
//					}
//
//					System.out.println("MerchantsClone size : "+resultOrderMerchantListClone.size());
//
//					List<SubOrder> resultOrderDetList = new ArrayList<SubOrder>();
//					for(int k=0;k<resultOrderMerchantList.size();k++){
//
//						List<Object[]> odDetList = orderDetailDao.findByOrderIdAndMerId(orderList.get(i).getOrderId(),Long.valueOf(resultOrderMerchantList.get(k).getMerid()));
//
//						//convert List<Object[]>
//						int count =0;
//						for(Object[] objectArray : odDetList){
//							System.out.println("OrderDetail : "+count+" "+(Integer) objectArray[0]);
//							count++;
//							SubOrder orderDet  = new SubOrder();
//							orderDet.setMenuId((Integer) objectArray[0]);
//	//						System.out.println("--1");
//							orderDet.setMenuName((String) objectArray[1]);
//	//						System.out.println("--2");
//							orderDet.setMenuPrice((Float) objectArray[2]);
//	//						System.out.println("--3");
//							orderDet.setOrderDetailId((Integer) objectArray[3]);
//	//						System.out.println("--4");
//							orderDet.setOrderDetailAmount((Integer) objectArray[4]);
//	//						System.out.println("--5");
//							orderDet.setRemark((String) objectArray[5]);
//	//						System.out.println("--6");
//							orderDet.setMenuTotalPrice((Double) objectArray[6]);
//	//						System.out.println("--7");
//
//							List<OrderOptionDetail> resultOrderOptList = new ArrayList<OrderOptionDetail>();
//							List<Object[]> odOptList = ordersDetailOptionsDao.findByOrderId(Long.valueOf(orderDet.getOrderDetailId()));
//
//							//convert List<Object[]>
//							int count2 = 0;
//							for(Object[] objectArray2 : odOptList){
//								System.out.println("OrderOption : "+count2+" "+(Integer) objectArray2[0]);
//								count2++;
//								OrderOptionDetail orderOpt = new OrderOptionDetail();
//								orderOpt.setOrderDetailId(((Integer) objectArray2[0]));
//	//							System.out.println("---1");
//								orderOpt.setOptionName(((String) objectArray2[1]));
//	//							System.out.println("---2");
//								orderOpt.setOptionPrice(((Float) objectArray2[2]));
//	//							System.out.println("---3");
//								resultOrderOptList.add(orderOpt);
//							}
//
//							orderDet.setOptionList(resultOrderOptList);
//							resultOrderDetList.add(orderDet);
//						}
//						resultOrderMerchantList.get(k).setSubOrderList(resultOrderDetList);
//					}
//
//					custOrder.setMerchantOrderList(resultOrderMerchantList);
//
//				}
//			}// end for test
//			custOrderList.add(custOrder);
//			logger.info(String.valueOf(custOrderList.size()));
//		}
//
//			return ResponseEntity.ok(new Response<List<CustOrder>>(HttpStatus.OK.value(),"Query order sucessfully", custOrderList));
//		}catch (Exception e){
//			System.out.println(e.getMessage());
//			return null;
//		}
//	}


	@RequestMapping(value="/service/orders/noti" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response<List<NotificationInbox>>> noti(@RequestBody Map<String, Object> mapRequest)
	{
		List<NotificationInbox> notiList = new ArrayList<NotificationInbox>();
		String messeage = "";
		try {
			int noti_ref_id = (Integer) mapRequest.get("noti_ref_id");
			String noti_type = (String) mapRequest.get("noti_type");

			notiList = notiDao.findNotiNonRead(noti_ref_id, noti_type);
			messeage = "Get notification successfully";
		}
		catch (Exception e) {
			logger.info(e.getMessage());
			messeage = "Get notification Failed";
			return null;
		}
		return ResponseEntity.ok(new Response<List<NotificationInbox>>(HttpStatus.OK.value(),messeage, notiList));

	}

	@RequestMapping(value="/service/orders/noti/accept/{notiId}" , method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Response<String>> acceptNoti(@PathVariable("notiId") int noti_id)
	{

		String messeage = "";
		logger.info("noti_id {} ",noti_id);
		try {

			notiDao.updateNotiReadFlagByNotiId(noti_id);
			messeage = "Acknowledge noti successfully";
		}
		catch (Exception e) {
			logger.info(e.getMessage());
			messeage = "Acknowledge noti Failed";
			return null;
		}
		return ResponseEntity.ok(new Response<String>(HttpStatus.OK.value(),messeage, messeage));

	}


	@RequestMapping(value="/service/orders/getOrderDetail" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response<List<CustOrder>>> getOrderDetail(@RequestBody Map<String, String> json){
		Long cusId = Long.valueOf(json.get("cusId"));
		String isCurrentOrder = json.get("isCurrentOrder");
		
		try {
			//final output
			List<CustOrder> custOrderList = new ArrayList<CustOrder>();

			List<Orders> orderList = new ArrayList<Orders>();
			if(isCurrentOrder.equalsIgnoreCase("Y")){
				orderList = ordersDao.findLastOrderCusId(cusId);
			}else{
				orderList = ordersDao.findByOrderCusId(cusId, VariableText.ORDER_RECEIVED_STATUS);
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
						result.setOrder_cus_id(((Integer) objectArray[1]).longValue());
						result.setOrder_address((String) objectArray[2]);
						result.setOrder_address_latitude((String) objectArray[3]);
						result.setOrder_address_longtitude((String) objectArray[4]);
						result.setOrder_created_datetime((Date) objectArray[5]);
						result.setOrder_receive_datetime((Date) objectArray[6]);
						result.setOrder_delivery_rate((Integer) objectArray[7]);
						result.setOrder_total_price((Double) objectArray[8]);
						result.setOrder_distance((Double) objectArray[9]);
						result.setOrder_status((Integer) objectArray[10]);
						result.setOrder_food_price((Double) objectArray[11]);
						result.setOrder_delivery_price((Double) objectArray[12]);
						result.setOrder_confirm_code((String) objectArray[13]);
						result.setOrder_estimate_time((Integer) objectArray[14]);
						result.setOrder_estimate_datetime((Date) objectArray[15]);
						result.setPayment_type((String) objectArray[16]);
						result.setMenu_id((Integer) objectArray[17]);
						result.setMenu_name((String) objectArray[18]);
						result.setMenu_price((Float) objectArray[19]);
						result.setOrder_detail_amount((Integer) objectArray[20]);
						result.setOrder_remark((String) objectArray[21]);
						result.setOption_total_price((Double) objectArray[22]);
						result.setMenu_total_price((Double) objectArray[23]);
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
				custOrder.setEstimatedDateTime(orderList.get(i).getOrderEstimateDatetime().toString());

				//Loop orderHeadAndDetList For get Merchant List
				for(int j=0;j<orderHeadAndDetList.size();j++){
//					System.out.println("orderDetail : "+j+" "+orderHeadAndDetList.get(j).getOrder_id());
					List<Object[]> merchantList = merchantsDao.findSpecialByOrderId(orderHeadAndDetList.get(j).getOrder_id());

					//convert List<Object[]> to List<OrderHeaderAndDetail>
					List<MerchantOrder> orderMerList = new ArrayList<MerchantOrder>();
					for(Object[] objectArray : merchantList){
//						System.out.println("Merchant ID: "+(Integer) objectArray[0]);
//						System.out.println("à¸à¸·à¹à¸­à¸£à¹à¸²à¸ : "+(String) objectArray[1]);

						MerchantOrder orderMer  = new MerchantOrder();
						orderMer.setMerid(((Integer) objectArray[0]));
						orderMer.setMerName(((String) objectArray[1]));
						orderMer.setMerLatitude(((String) objectArray[2]));
						orderMer.setMerLongitude(((String) objectArray[3]));
						orderMer.setMerDistance(((BigDecimal) objectArray[4]));
						orderMer.setMerDeliveryPrice(((BigDecimal) objectArray[5]));
						orderMer.setMerFoodPrice(((Double) objectArray[6]));
						orderMerList.add(orderMer);
					}

					for(int k=0;k<orderMerList.size();k++){
						List<Object[]> odDetList = orderDetailDao.findByOrderIdAndMerId(orderList.get(i).getOrderId(),Long.valueOf(orderMerList.get(k).getMerid()));
						//convert List<Object[]>
						int count =0;

						List<SubOrder> subOrderList  = new ArrayList<SubOrder>();
						for(Object[] objectArray : odDetList){
//							System.out.println("OrderDetail : "+count+" "+(Integer) objectArray[0]);
							count++;
							SubOrder subOrder  = new SubOrder();
							subOrder.setMenuId((Integer) objectArray[0]);
							subOrder.setMenuName((String) objectArray[1]);
							subOrder.setMenuPrice((Float) objectArray[2]);
							subOrder.setOrderDetailId((Integer) objectArray[3]);
							subOrder.setOrderDetailAmount((Integer) objectArray[4]);
							subOrder.setRemark((String) objectArray[5]);
							subOrder.setMenuTotalPrice((Double) objectArray[6]);

							List<OrderOptionDetail> orderOptList = new ArrayList<OrderOptionDetail>();
							List<Object[]> odOptList = ordersDetailOptionsDao.findByOrderId(Long.valueOf(subOrder.getOrderDetailId()));
							//convert List<Object[]>
							for(Object[] objectArray2 : odOptList){
								OrderOptionDetail orderOpt = new OrderOptionDetail();
								orderOpt.setOrderDetailId(((Integer) objectArray2[0]));
								orderOpt.setOptionName(((String) objectArray2[1]));
								orderOpt.setOptionPrice(((Float) objectArray2[2]));
								orderOptList.add(orderOpt);
							}
							subOrder.setOptionList(orderOptList);



							subOrderList.add(subOrder);
						}
						orderMerList.get(k).setSubOrderList(subOrderList);
					}

					custOrder.setMerchantOrderList(orderMerList);
				}

				custOrderList.add(custOrder);
			}
			logger.info("Get Order Success.");
			return ResponseEntity.ok(new Response<List<CustOrder>>(HttpStatus.OK.value(),"Query order sucessfully", custOrderList));
		}catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "/service/orders/getorder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Response<List<Orders>>> getOrderTracking(@RequestBody Map<String, String> json) {
		Long cusId = Long.valueOf(json.get("cusId"));
		String isCurrentOrder = json.get("isCurrentOrder");
		logger.info("isCurrent "+isCurrentOrder);
		try {
			if (isCurrentOrder.equals("Y")) {
				List<Orders> orderList = ordersDao.getCurrentOrderByCusId(cusId);
				logger.info("orderList.size "+orderList.size());
				if (orderList.size() > 0 ) {

					List<SequenceOrders> seqOrdersList = orderList.get(0).getSequenceOrders();
					List<SequenceOrders> mappedSeqOrdersList = new ArrayList<SequenceOrders>();
					logger.info("seqOrdersList.size "+seqOrdersList.size());
					List<OrderDetail> orderDetails = orderList.get(0).getOrderDetails();
					for (SequenceOrders seq : seqOrdersList) {
						for (OrderDetail detail : orderDetails) {
							if (detail.getMerId() == seq.getMerchants().getMerID()) {
								seq.getOrderDetails().add(detail);
							}
						}
						mappedSeqOrdersList.add(seq);
					}
					orderList.get(0).setSequenceOrders(mappedSeqOrdersList);
				}
				
				return ResponseEntity.ok(new Response<List<Orders>>(HttpStatus.OK.value(), "Query order sucessfully", orderList));
			}else{
				List<Orders> orderList = ordersDao.findByOrderCusId(cusId, VariableText.ORDER_RECEIVED_STATUS);
				return ResponseEntity.ok(new Response<List<Orders>>(HttpStatus.OK.value(), "Query order sucessfully", orderList));
			}
		}catch(Exception e){
			logger.info(e.getMessage());
			return null;
		}
	}

	@RequestMapping(value="/service/orders/verifycurrent/{cusId}" , method = RequestMethod.GET)
	public ResponseEntity<Response<Map<String, Object>>> haveCurrentOrder(@PathVariable("cusId") int cusId)
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String messeage = "";
		try {
			logger.info("cusId {}", cusId);
			String status = VariableText.ORDER_WAITING_RESPONSE_STATUS+","+VariableText.ORDER_COOKING_STATUS+","+VariableText.ORDER_DELIVERING_STATUS;
			logger.info("status {}", status);
			int count = ordersDao.isCurrentOrderIsExist(cusId);
			if (count > 0) {
				dataMap.put("result",VariableText.Y_FLAG);
			}else{
				dataMap.put("result",VariableText.N_FLAG);
			}

		}
		catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}
		return ResponseEntity.ok(new Response<Map<String, Object>>(HttpStatus.OK.value(), "Check current order successfully", dataMap));

	}

}
