package com.fooddelivery.Controller;

import com.fooddelivery.Model.User;
import com.fooddelivery.json.model.Directions;
import com.fooddelivery.util.Response;
import com.google.api.client.json.Json;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.maps.DirectionsApi;
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
import java.text.DecimalFormat;
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
	public String[] getDistanceDuration(@PathVariable(value="oriLat") String oriLat,
							@PathVariable(value="oriLng") String oriLng,
							@PathVariable(value="desLat") String desLat,
							@PathVariable(value="desLng") String desLng){

		try{
			
//			String url = "https://maps.googleapis.com/maps/api/distancematrix/json?key=AIzaSyCQQmDCGFkJ4bR3sslC1f9OXFIcXNveStU&mode=driving&destinations="+desLat+"%2C"+desLng+"&origins="+oriLat+"%2C"+oriLng;
			String url = "https://maps.googleapis.com/maps/api/directions/json?destination="+desLat+"%2C"+desLng+"&origin="+oriLat+"%2C"+oriLng+"&units=imperial&alternatives=true";
			System.out.println(">>" + url);
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
			//System.out.println(response.toString());
			
			Gson gson = new Gson();  
			Directions directionArray = gson.fromJson(response.toString(), Directions.class);
			
			JSONObject objJson = new JSONObject(response.toString());
			//JSONObject json = (JSONObject) JSONSerializer.toJSON(response.toString()); 
			//List<String> list = new ArrayList<String>();
			
			//String duration = objJson.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text");
			//System.out.println("duration: " + duration);
			
			//=====================
			JSONArray jsonMainArr = objJson.getJSONArray("routes");
			//System.out.println(">>" + jsonMainArr.length());
			
			List<Double> distanceList = new ArrayList<Double>();
			List<Integer> durationList = new ArrayList<Integer>();
			
			for (int i=0; i<jsonMainArr.length(); i++) {
//					System.out.println("--" + jsonMainArr.get(i));

//					System.out.println(objJson.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text"));
//					System.out.println(objJson.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("text"));
					
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
					
					//System.out.println(distF);
					//System.out.print(duraF);
							
					
					//cal time in minute
					if(timeURmspace.equalsIgnoreCase("hours")){
						duraF = duraF*60;
					}else if(timeURmspace.equalsIgnoreCase("morethanHours")){
						String minAfHrStr = timeU.replaceAll(" hours ", "");
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
			
			//set 1 digit decimal of distance
			DecimalFormat df2 = new DecimalFormat(".#");
			String distDecm = df2.format(distMin);
			String duraStr = String.valueOf(duraMin);

			String[] resultArry = new String[2];
			resultArry[0] = distDecm;
			resultArry[1] = duraStr;
			
			return resultArry;

		}catch (Exception ex){
			return null;
		}
	    
	}
	
	
	
//	public static void main(String[] args){
//		HomeController home = new HomeController();
//		//mins
//		//home.getDistance("13.7325473","100.5295583","13.7266196","100.5281344");
//		// > hours min
//		home.getDistance("13.7325473","100.5295583","12.583369","99.681333");
//	}
	
}
