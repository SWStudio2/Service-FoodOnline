package com.fooddelivery.DurationPath;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsDao;
import com.fooddelivery.Model.MerchantsQuery;
import com.fooddelivery.util.NodeDetailVer2;

public class DurationPathService {
	
	int[] merchantsId;
	String latitudeCustomer;
	String longtitudeCustomer;
	
	/*@Autowired
	private MerchantsQuery merchantsQuery;*/
	@Autowired
	private MerchantsDao merchantsDao;
	
	public DurationPathService
	(
			int[] merchantsId,
			String latitudeCustomer,
			String longtitudeCustomer
	) {
		this.merchantsId = merchantsId;
		this.latitudeCustomer = latitudeCustomer;
		this.longtitudeCustomer = longtitudeCustomer;
	}
	
	public List<NodeDetailVer2> getResult() {
		List<NodeDetailVer2> result = new ArrayList<NodeDetailVer2>();
		//info Merchants
		/*String mersIdString = "";
		for (int i=0; i<merchantsId.length; i++) {
			if (i != 0) {
				mersIdString += ",";
			}
			mersIdString += merchantsId[i];
		}
		System.out.println(mersIdString);*/
		List<Integer> merchantsIdList = new ArrayList<Integer>();
		for (int i=0; i<merchantsId.length; i++) {
			merchantsIdList.add(merchantsId[i]);
		}
		System.out.println("size: " + merchantsIdList.size());
		try {
			List<Merchants> merchantsList = merchantsDao.findAllMerchants();
			System.out.println(merchantsList);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		
		
		
		//Station -> Merchant
		
		
		
		return result;
	}

}
