package com.google.maps;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

@RestController
public class DistanceMatrixApiTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCQQmDCGFkJ4bR3sslC1f9OXFIcXNveStU");
//		String[] origin = {"13.222222","100.876555"};
//		String[] destination = {"13.222222","100.876555"};
//		DistanceMatrixApi.getDistanceMatrix(context, origin, destination);
		
	    DistanceMatrixApi.newRequest(context)
        .origins(new LatLng(13.7325473, 100.5295583))
        .destinations(new LatLng(13.7266196, 100.5281344)).mode(TravelMode.DRIVING)
        .awaitIgnoreError();
	    
	}

}
