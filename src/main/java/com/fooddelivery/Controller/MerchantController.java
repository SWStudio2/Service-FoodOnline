package com.fooddelivery.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MerchantController {
	
	@RequestMapping(value="/service/merchant/getall"  , method=RequestMethod.POST)
	@ResponseBody
	public String getMerchants(@RequestParam("merID") String merIDArr){
		return "test";
	}
}
