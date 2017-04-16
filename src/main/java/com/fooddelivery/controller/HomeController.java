package com.fooddelivery.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.fooddelivery.MySpecialListener;
import com.fooddelivery.json.model.placeorder.PlaceOrder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fooddelivery.Model.BikePath;
import com.fooddelivery.Model.BikePathDao;
import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.CustomerDao;
import com.fooddelivery.Model.DeliveryRate;
import com.fooddelivery.Model.DeliveryRateDao;
import com.fooddelivery.json.model.Directions;
import com.fooddelivery.util.Response;
import com.google.gson.Gson;

@RestController
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	// Wire the UserDao used inside this controller.
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private DeliveryRateDao delvDao;
	@Autowired
	private BikePathDao bikePathDao;

//	@RequestMapping("/")
//	public String index(){
//		return "Hello World";
//	}
	
	@RequestMapping(value="/service/findBikePath", method=RequestMethod.GET)
	@ResponseBody
	public BikePath getBikePath(@RequestParam("sID") int sourceId,@RequestParam("dID") int destId){
		return bikePathDao.findBikePathFromId(sourceId, destId);
	}
	
	@RequestMapping(value="/service/{id}", method=RequestMethod.GET)
	public String getID(@PathVariable int id){
		return "Your ID is "+id;
	}
	
	public String[] getDistanceDuration(@PathVariable(value="oriLat") String oriLat,
							@PathVariable(value="oriLng") String oriLng,
							@PathVariable(value="desLat") String desLat,
							@PathVariable(value="desLng") String desLng){

		try{
			
//			String url = "https://maps.googleapis.com/maps/api/distancematrix/json?key=AIzaSyCQQmDCGFkJ4bR3sslC1f9OXFIcXNveStU&mode=driving&destinations="+desLat+"%2C"+desLng+"&origins="+oriLat+"%2C"+oriLng;
			String url = "https://maps.googleapis.com/maps/api/directions/json?destination="+desLat+"%2C"+desLng+"&origin="+oriLat+"%2C"+oriLng+"&units=imperial&alternatives=true";
//			logger.info(">>" + url);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			//logger.info(response.toString());
			
			Gson gson = new Gson();  
			Directions directionArray = gson.fromJson(response.toString(), Directions.class);
			
			JSONObject objJson = new JSONObject(response.toString());
			//JSONObject json = (JSONObject) JSONSerializer.toJSON(response.toString()); 
			//List<String> list = new ArrayList<String>();
			
			//String duration = objJson.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text");
			//logger.info("duration: " + duration);
			
			//=====================
			JSONArray jsonMainArr = objJson.getJSONArray("routes");
			//logger.info(">>" + jsonMainArr.length());
			
			List<Double> distanceList = new ArrayList<Double>();
			List<Integer> durationList = new ArrayList<Integer>();
		
			for (int i=0; i<jsonMainArr.length(); i++) {
//					logger.info("--" + jsonMainArr.get(i));

//					logger.info(objJson.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text"));
//					logger.info(objJson.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("text"));
					
					String dist = objJson.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text") ;
					String dura = objJson.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("text");					
					double distF = Double.valueOf(dist.substring(0, dist.indexOf(" mi"))) * 1.609; // change mi to km
					distanceList.add(distF);
					int duraF = Integer.valueOf(dura.substring(0, dura.indexOf(" ")));	
					
					//check time unit
					String timeU = dura.substring(dura.indexOf(" "), dura.length());
					String timeURmspace = "";
					if(timeU.length() > 6){
						timeURmspace = "morethanHours";
					}else{
						timeURmspace = timeU.replaceAll(" ", "");// remove space
					}
					
					//logger.info(distF);
					//System.out.print(duraF);
							
					
					//cal time in minute
					if(timeURmspace.equalsIgnoreCase("hours")){
						duraF = duraF*60;
					}else if(timeURmspace.equalsIgnoreCase("morethanHours")){
						String minAfHrStr = timeU.replaceAll(" hour ", "");
						minAfHrStr = minAfHrStr.replaceAll(" mins", "");
						int minAfHr = Integer.valueOf(minAfHrStr);
						duraF = duraF*60;
						duraF += minAfHr;
						
					}
					durationList.add(duraF);

			}
			
			//get minimum distance and duration
			double distMin = 0;
			for(int i=0;i<distanceList.size();i++){
				if(i == 0){
					distMin = distanceList.get(i);
				}else if(distanceList.get(i) < distMin){
					distMin = distanceList.get(i);
				}
			}

			int duraMin = 0;
			for(int i=0;i<durationList.size();i++){
				if(i == 0){
					duraMin = durationList.get(i);
				}else if(durationList.get(i) < duraMin){
					duraMin = durationList.get(i);
				}
			}
			
			BigDecimal bigDistMin = new BigDecimal(distMin);
			bigDistMin = bigDistMin.setScale(1, RoundingMode.HALF_UP);
			
			BigDecimal bigDuraMin = new BigDecimal(duraMin);
			bigDuraMin = bigDuraMin.setScale(1, RoundingMode.HALF_UP);


			String distDecm = bigDistMin.toString();
			String duraStr = bigDuraMin.toString();

			String[] resultArry = new String[2];
			resultArry[0] = distDecm;
			resultArry[1] = duraStr;
			
			return resultArry;

		}catch (Exception ex){
			return null;
		}
	    
	}
	@RequestMapping(value="service/getdistancematrix", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<Map<String, Object>>> getDistanceMatrix(@RequestBody Map<String, Object> mapRequest) {

		String oriLat = (String) mapRequest.get("oriLat");
		String oriLng = (String) mapRequest.get("oriLng");
		String desLat = (String) mapRequest.get("desLat");
		String desLng = (String) mapRequest.get("desLng");
		try{
			//Thread.sleep(300);
			String[] arr = getDistanceDuration(oriLat, oriLng,desLat,desLng);
			String distDecm = arr[0];
			String duraStr = arr[1];


			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("distance",distDecm);
			dataMap.put("duration",duraStr);

			return ResponseEntity.ok(new Response<Map<String, Object>>(HttpStatus.OK.value(),"Get successfully", dataMap));


		}
		catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			return null;
		}


	}
	
	
//	public static void main(String[] args){
//		HomeController home = new HomeController();
		//mins
		//home.getDistance("13.7325473","100.5295583","13.7266196","100.5281344");
		// > hours min
//		home.getDistance("13.7325473","100.5295583","12.583369","99.681333");
//	}
	
}
