package com.fooddelivery.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import com.fooddelivery.Model.BikeStation;
import com.fooddelivery.Model.BikeStationDao;
import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.CustomerDao;
import com.fooddelivery.Model.DeliveryRate;
import com.fooddelivery.Model.DeliveryRateDao;
import com.fooddelivery.Model.FullTimeMessenger;
import com.fooddelivery.Model.FullTimeMessengerDao;
import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.NotificationInbox;
import com.fooddelivery.Model.NotificationInboxDao;
import com.fooddelivery.Model.Orders;
import com.fooddelivery.Model.OrdersDao;
import com.fooddelivery.Model.SequenceOrdersDao;

//@RequestMapping(value={"/service/customer"})
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
	private OrdersDao ordersDao;


	@Autowired
	private NotificationInboxDao notiDao;

	@Autowired
	private SequenceOrdersDao seqOrderDao;

	@Autowired
	private FullTimeMessengerDao fulltimeDao;

	@Autowired
	private BikeStationDao bikeStationDao;


	@RequestMapping(value={"/service/customer/auth"} ,method=RequestMethod.POST)
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
	public ResponseEntity<Response<BikeStation>> verifyConfirmCustomer(@RequestBody Map<String, Object> mapRequest)
	{
		BikeStation bikeBack = new BikeStation();
		int order_id = (Integer) mapRequest.get("order_id");
		String confirm_code = (String) mapRequest.get("seqor_confirm_code");
		int full_id = (Integer) mapRequest.get("full_id");
		List<Integer> merList = (List<Integer>)mapRequest.get("mer_id");

		String messeage = "";
		String result = customerDao.verifyConfirmCodeCustomer(order_id, confirm_code);
		if(result.equals("Y"))
		{
			//seqOrderDao.updateReceiveStatusSeqOrder(VariableText.MESSENGER_DELIVERIED_STATUS, order_id, full_id);//SQL B3
			fulltimeDao.updateFullTimeStatus(full_id, VariableText.MESSENGER_DELIVERIED_STATUS);//B4

			Orders currentOrder = ordersDao.getOrderByOrderId(order_id);
			//MessengerController mesController = new MessengerController();
			bikeBack = calculateNewStation(currentOrder.getOrderAddressLatitude(), currentOrder.getOrderAddressLongtitude());
			//Mint next step
			/*assign new station — fulltime_messenger
			 * bike station now stationId
			 * and clear full_order_id ให้เป้น ""-blank or null
			 */
			FullTimeMessenger fullTimeMess = fulltimeDao.getFullTimeByFullId(full_id);
			fullTimeMess.setFull_bike_station_now(String.valueOf(bikeBack.getBikeStationId()));
			fullTimeMess.setFullOrderId("");
			fulltimeDao.save(fullTimeMess);

			//check customer get all seq_order
			if (seqOrderDao.checkDeliveriedAllMenu(Integer.valueOf(order_id)) == "Y") {
				currentOrder.setOrderReceiveDatetime(DateTime.getCurrentDateTime());
				currentOrder.setOrderStatus(VariableText.ORDER_RECEIVED_STATUS);
				ordersDao.save(currentOrder);
			}

			//Date currentDateTime = DateTime.getCurrentDateTime();
			//customerDao.updateReceiveStatusCustomer(currentDateTime.toString(), VariableText.ORDER_RECEIVED_STATUS, order_id);
			int cus_id = custDao.getCustomerIdByOrderId(order_id);
			messeage = "Pass";
			//Wait query update fulltime , order


			return ResponseEntity.ok(new Response<BikeStation>(HttpStatus.OK.value(), messeage, bikeBack));
		}
		else
		{
			messeage = "verify Confirm Code Customer incorrenct";
			return ResponseEntity.ok(new Response<BikeStation>(HttpStatus.OK.value(), messeage, null));
		}

	}

	//************คำนวณหาจุดจอดใหม่***********************
	/*
	 * ให้เอาที่อยู่ลูกค้าสุดท้าย มาคิดหาระยะทางกับจุดจอดทั้งหมด
	 * แล้วเปรียบเทียบกับ 5 กิโลเมตร
	 * ภายในจุดจอดที่อยู่ใน 5 กิโล ให้ดูที่ จน. แมสว่ามีน้อยไหม
	 * ถ้าเท่ากัน ก็เลือกระยะที่อยู่น้อยที่สุด
	 * แต่ถ้าระยะทางห่างเกิน 5 กิโล ก็ไม่ต้องเข้าข้างบน
	 * ให้หาจุดจอดที่ใกล้ที่สุด
	 * update messenger ด้วย
	 * return station
	 * - station id
	 * - latitude
	 * - long
	 * - station name
	 */
	/*@RequestMapping(value={"service/fulltime/calculateNewStation/{lastestLatitude}"}, method=RequestMethod.GET)*/
	//public ResponseEntity<Response<BikeStation>> calculateNewStation(@PathVariable("lastestLatitude")
	public BikeStation calculateNewStation(
			String lastestLatitude, String latestLongtitude) {
		//lastestLatitude = "13.7324056";//"13.9038336";
		//latestLongtitude = "100.5304452";//"100.621662";
		BikeStation result = new BikeStation();
		//คำนวณจุดจอดใกล้-ไกล
		HomeController homeController = new HomeController();
		List<BikeStation> bikeStationList = bikeStationDao.getBikeStationAll();
		HashMap<String, BikeStation> bikeStationHashMap = new HashMap<String, BikeStation>();
		bikeStationHashMap = convertBikeStationListToHashMap(bikeStationList);
		//HashMap<String, String[]> bikeStationDistanceHash = new HashMap<String, String[]>();
		List<Object[]> fullTimeMessengerInStation = fulltimeDao.getNumberOfMessengerInStation();
		/*for (int j=0; j<fullTimeMessengerInStation.size(); j++) {
			System.out.println("station: " + fullTimeMessengerInStation.get(j)[0] +
					" available: " + fullTimeMessengerInStation.get(j)[1]);
		}*/
		List<double[]> bikeStationDistanceList = new ArrayList<double[]>();
		HashMap<String, String[]> numberFullTimeAvailableInStationHash = convertNumberMessengerInStationListToHashMap(
				fullTimeMessengerInStation);
		for (Map.Entry entry : numberFullTimeAvailableInStationHash.entrySet()) {
			String[] values = (String[])entry.getValue();
			for (int i=0; i<values.length; i++)
				System.out.println(">" + entry.getKey() + " value: " + values[i]);
		}
		for(int i=0; i<bikeStationList.size(); i++) {
			BikeStation bikeStation = bikeStationList.get(i);
			try {
				Thread.sleep(600);
				String[] detailArray = (String[]) homeController.getDistanceDuration(
						lastestLatitude,
						latestLongtitude,
						bikeStation.getBikeStationLatitude(),
						bikeStation.getBikeStationLongitude());
				logger.info("bikeStationId: " + bikeStation.getBikeStationId() );
				if (numberFullTimeAvailableInStationHash.get(String.valueOf(bikeStation.getBikeStationId())) != null) {
					String[] fullTimeAvailableInStation = (String[])numberFullTimeAvailableInStationHash.get(
							String.valueOf(bikeStation.getBikeStationId()));
					logger.info("fullTimeAvailableInStation[]: " + fullTimeAvailableInStation);
					Double numberFullTimeInStation = Double.valueOf(fullTimeAvailableInStation[1]);
					//bikeStationDistanceHash = sortBikeStationDistanceHash();
					if (numberFullTimeInStation != 0) {
						bikeStationDistanceList.add(new double[] { bikeStation.getBikeStationId(),
								Double.valueOf(detailArray[0]),
								numberFullTimeInStation});
						/*bikeStationDistanceHash.put(String.valueOf(bikeStation.getBikeStationId()),
								new String[]{detailArray[0], String.valueOf(numberFullTimeInStation)});*/
					}
					else {
						bikeStationDistanceList.add(new double[] { bikeStation.getBikeStationId(),
								Double.valueOf(detailArray[0]),
								100});
						/*bikeStationDistanceHash.put(String.valueOf(bikeStation.getBikeStationId()),
								new String[]{"100","100"});*/
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//sort List
		List<double[]> bikeStationDistanceSortedList = sortBikeStationDistanceHash(bikeStationDistanceList);
		for (int i=0; i<bikeStationDistanceSortedList.size(); i++) {
			logger.info("Station ID: " + bikeStationDistanceSortedList.get(i)[0] +
					" | distance: " + bikeStationDistanceSortedList.get(i)[1] +
					" | number: " + bikeStationDistanceSortedList.get(i)[2]);
		}
		if ((double) bikeStationDistanceSortedList.get(0)[1] < 5.00) {
			for (int i=0; i<bikeStationDistanceSortedList.size(); i++) {
				if (bikeStationDistanceSortedList.get(i)[1] > 5.00) {
					bikeStationDistanceSortedList.remove(i);
				}
			}
			double[] temp = bikeStationDistanceSortedList.get(0);
			int initialNumberOfMessenger = (int) temp[2];
			if (bikeStationDistanceSortedList.size() > 1) {
				for (int i=1; i<bikeStationDistanceSortedList.size(); i++) {
					if (initialNumberOfMessenger > (int) bikeStationDistanceSortedList.get(i)[2]) {
						initialNumberOfMessenger = (int) bikeStationDistanceSortedList.get(i)[2];
						temp = bikeStationDistanceSortedList.get(i);
					}
				}
				result = bikeStationHashMap.get(String.valueOf( (int) temp[0] ) );
			}
			else {
				result = bikeStationHashMap.get(String.valueOf( (int) bikeStationDistanceSortedList.get(0)[0] ) );
			}
		}
		else {
			result = bikeStationHashMap.get(String.valueOf( (int) bikeStationDistanceSortedList.get(0)[0] ) );
		}
		/*return ResponseEntity.ok(new Response<BikeStation>(HttpStatus.OK.value(),"Get data successfully",
				result));*/
		return result;
	}

	private HashMap<String, String[]> convertNumberMessengerInStationListToHashMap(List<Object[]> numberMessengerInStation) {
		HashMap<String, String[]> result = new HashMap<String, String[]>();
		for (int i=0; i<numberMessengerInStation.size(); i++) {
			String[] intArray = new String[]{numberMessengerInStation.get(i)[0] + "",
					numberMessengerInStation.get(i)[1] + ""};
			result.put(String.valueOf(numberMessengerInStation.get(i)[0]), intArray);
		}
		return result;
	}

	private HashMap<String, BikeStation> convertBikeStationListToHashMap(List<BikeStation> bikeStationList) {
		HashMap<String, BikeStation> result = new HashMap<String, BikeStation>();
		for (int i=0; i<bikeStationList.size(); i++) {
			result.put(String.valueOf(bikeStationList.get(i).getBikeStationId()), bikeStationList.get(i));
		}
		return result;
	}

	private List<double[]> sortBikeStationDistanceHash(List<double[]> bikeStationDistance) {
		Collections.sort(bikeStationDistance, new Comparator() {
			public int compare(Object o1, Object o2) {
				Double distance1 = Double.valueOf(((double[]) o1)[1]);
				Double distance2 = Double.valueOf(((double[]) o2)[1]);
				double sComp = distance1.compareTo(distance2);
				if (sComp != 0) {
					return (int) sComp;
				}
				else {
					Double number1 = Double.valueOf(((double[]) o1)[2]);
					Double number2 = Double.valueOf(((double[]) o2)[2]);
					return number1.compareTo(number2);
				}
			}
		});
		return bikeStationDistance;
	}

	@RequestMapping(value={"/service/customer/regis"} ,method=RequestMethod.POST)
	public ResponseEntity<Response<HashMap>> regis(@RequestBody Customer cus){
		List<Customer> customer = null;
		HashMap<String,String> resultList = new HashMap<String, String>();

		try {
			logger.info("Check point 0");
			String cusName =  cus.getCusUserName();
			int count = customerDao.isCusEmailExist(cus.getCusUserName());
			logger.info("Count {}",count);

			if(count == 0){
				logger.info("Pass");
				Date currentDateTime = DateTime.getCurrentDateTime();
				cus.setCusCreatedDate(currentDateTime);
				customerDao.save(cus);
				resultList.put("msg",String.format(VariableText.REGISTER_SUCCESS, cusName));
				resultList.put("isPass",VariableText.Y_FLAG);
			}else{
				logger.info("Fail");
				resultList.put("msg",String.format(VariableText.REGISTER_UNSUCCESS, cusName));
				resultList.put("isPass",VariableText.N_FLAG);
			}
			return ResponseEntity.ok(new Response<HashMap>(HttpStatus.OK.value(),
					"Call register successfully", resultList));
		}
		catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			logger.info("Check point 2");
			return null;
		}


	}
}
