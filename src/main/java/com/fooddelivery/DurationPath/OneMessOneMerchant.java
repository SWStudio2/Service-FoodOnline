package com.fooddelivery.DurationPath;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fooddelivery.Model.Merchants;
import com.fooddelivery.Model.MerchantsQuery;
import com.fooddelivery.util.NodeDetailVer2;

public class OneMessOneMerchant {
	
	int[] merchantsId;
	String latitudeCustomer;
	String longtitudeCustomer;
	
	@Autowired
	private MerchantsQuery merchantsQuery;
	
	public OneMessOneMerchant
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
		String mersIdString = "";
		for (int i=0; i<merchantsId.length; i++) {
			if (i != 0) {
				mersIdString += ",";
			}
			mersIdString += merchantsId[i];
		}
		System.out.println(mersIdString);
		Merchants[] merchantsList = merchantsQuery.queryMerChantByID(mersIdString);
		
		System.out.println(merchantsList);
		
		
		//Station -> Merchant
		
		
		
		return result;
	}

}
