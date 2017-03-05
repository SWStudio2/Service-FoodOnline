package com.fooddelivery.Controller;

import com.fooddelivery.Model.User;
import com.fooddelivery.util.Response;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

//	@RequestMapping("/")
//	public String index(){
//		return "Hello World";
//	}
	
	@RequestMapping(value="/service/{id}", method=RequestMethod.GET)
	public String getID(@PathVariable int id){
		return "Your ID is "+id;
	}
	
	@RequestMapping(value={"/service/auth"} ,method=RequestMethod.POST)
	public ResponseEntity<Response<User>> updateID(@RequestBody User oldUser){

		User user = new User();
		user.setId(1);
		user.setName("Test1");

		return ResponseEntity.ok(new Response<User>(HttpStatus.OK.value(),
				"Login successfully", user));

	}
	
	@RequestMapping(value="service/distanceMatrix/{oriLat}/{oriLng}/{desLat}/{desLng}" , method=RequestMethod.GET)
	public Integer getDistanceMatrix(@PathVariable(value="oriLat") String oriLat,
							@PathVariable(value="oriLng") String oriLng,
							@PathVariable(value="desLat") String desLat,
							@PathVariable(value="desLng") String desLng){
//	public String getDistanceMatrix(){
		
//		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCQQmDCGFkJ4bR3sslC1f9OXFIcXNveStU");
//		DistanceMatrix dt = null;
		try{
//			dt = DistanceMatrixApi.newRequest(context)
//			        .origins(new LatLng(13.7325473, 100.5295583))
//			        .destinations(new LatLng(13.7266196, 100.5281344)).mode(TravelMode.DRIVING)
//			        .awaitIgnoreError();
			
			String url = "https://maps.googleapis.com/maps/api/distancematrix/json?key=AIzaSyCQQmDCGFkJ4bR3sslC1f9OXFIcXNveStU&mode=driving&destinations="+desLat+"%2C"+desLng+"&origins="+oriLat+"%2C"+oriLng;

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");
			
			int responseCode = con.getResponseCode();
			
//			System.out.println("\nSending 'GET' request to URL : " + url);
//			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
//			System.out.println(response.toString());
			
			JSONObject objJson = new JSONObject(response.toString());
			//JSONObject json = (JSONObject) JSONSerializer.toJSON(response.toString()); 
			List<String> list = new ArrayList<String>();
			
			String duration = objJson.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("text");
			int durationNum = 0; 
			if(duration.length() > 7){
				//hour
				String copyDuration = duration;
				String hourStr = "";
				int hour = Integer.valueOf(copyDuration.substring(0, copyDuration.indexOf(" hour")));
				if(hour == 1){
					hourStr = duration.substring(0, copyDuration.indexOf(" hour")) + " hour ";
				}else{
					hourStr = duration.substring(0, copyDuration.indexOf(" hour")) + " hours ";
				}
				String minStr = copyDuration.replace(hourStr, "");
				int min = Integer.valueOf(minStr.substring(0, minStr.indexOf(" min")));
				durationNum = min+(hour*60);
			}else{
				//min && sec
				durationNum = Integer.valueOf(duration.substring(0, duration.indexOf(' ')));
			}
			//System.out.print(durationNum);
			return durationNum;
			//return response.toString();
		}catch (Exception ex){
			return null;
		}
	    
	}
	
//	public static void main(String[] args){
//		HomeController home = new HomeController();
////		home.getDistanceMatrix("13.7325473","100.5295583","13.7266196","100.5281344");
//		home.getDistanceMatrix("13.73254730","100.52955830","13.1959121","99.6216668");
//	}
	
}
