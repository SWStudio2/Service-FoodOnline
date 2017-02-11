package com.fooddelivery.Model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "holidays_merchant")
public class HolidaysMerchant {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long hol_mer_id;
	
	@NotNull
	private Date hol_date;
	
	public HolidaysMerchant() { }

	public HolidaysMerchant(long holidaysMerchantId) { 
	   this.hol_mer_id = holidaysMerchantId;
	}
	
	public HolidaysMerchant(Date holidaysMerchantDate) {
		this.hol_date = holidaysMerchantDate;
	}
	
	public Date getHolidaysMerchantDate() {
		return hol_date;
	}

	public void setHolidaysMerchantDate(Date holidaysMerchantDate) {
		this.hol_date = holidaysMerchantDate;
	}
}
