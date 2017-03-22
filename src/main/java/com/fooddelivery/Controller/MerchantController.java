//package com.fooddelivery.Controller;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import com.fooddelivery.Model.Merchants;
//import com.fooddelivery.Model.MerchantsDao;
//
//@RestController
//public class MerchantController {
//	
//	// Wire the UserDao used inside this controller.
//	@Autowired
//	private MerchantsDao merchantsDao;
//	
//	@RequestMapping(value="/service/merchant/getall" , method = RequestMethod.POST)
//	@ResponseBody
//	public List<Merchants> getMerchants(@RequestParam(value = "mername" , required = false) Optional<String> mername ,@RequestParam(value = "date") String date ,@RequestParam(value = "time") String time){
//		
////		System.out.println(mername);
//		
//		try {
//			
//			List<Merchants> merchants = null;
//			if(mername.isPresent()){
//				merchants = merchantsDao.findMerchantsByMerName(mername.get(),date,time);
////				System.out.println(mername.get());
////				System.out.println(merchants);
//			}else{
//				merchants = merchantsDao.findMerchantsByStatus(date,time);
//			}
//			
//			
//			return merchants;
//			
//	    }
//	    catch (Exception ex) {
//	    	return null;
//	    }
//		
//	}
//	
//}
