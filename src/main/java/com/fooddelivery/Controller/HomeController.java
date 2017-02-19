package com.fooddelivery.Controller;

import com.fooddelivery.Model.User;
import com.fooddelivery.util.Response;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import java.net.HttpURLConnection;
import java.net.URL;

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
	
	//@RequestMapping(value="service/distanceMatrix/{oriLat}/{oriLng}/{desLat}/{desLng}" , method=RequestMethod.GET)
	public DistanceMatrix getDistanceMatrix(@PathVariable(value="oriLat") String oriLat,
							@PathVariable(value="oriLng") String oriLng,
							@PathVariable(value="desLat") String desLat,
							@PathVariable(value="desLng") String desLng){
		
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCQQmDCGFkJ4bR3sslC1f9OXFIcXNveStU");
		DistanceMatrix dt = null;
		try{
			dt = DistanceMatrixApi.newRequest(context)
			        .origins(new LatLng(13.7325473, 100.5295583))
			        .destinations(new LatLng(13.7266196, 100.5281344)).mode(TravelMode.DRIVING)
			        .awaitIgnoreError();
			
			String url = "https://maps.googleapis.com/maps/api/distancematrix/json?key=AIzaSyCQQmDCGFkJ4bR3sslC1f9OXFIcXNveStU&mode=driving&destinations=13.72661960%2C100.52813440&origins=13.73254730%2C100.52955830";

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");
			
			int responseCode = con.getResponseCode();
			
		}catch (Exception ex){
			return null;
		}
	 
	    return dt;
	    
	}
	
	public static void main(){
		
	}
	
}
