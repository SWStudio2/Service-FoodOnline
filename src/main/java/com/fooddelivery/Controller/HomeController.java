package com.fooddelivery.Controller;

import com.fooddelivery.Model.User;
import com.fooddelivery.util.Response;
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
		user.setCusid(1);
		user.setName("Test1");


		return ResponseEntity.ok(new Response<User>(HttpStatus.OK.value(),
				"Login successfully", user));

	}
	
}
