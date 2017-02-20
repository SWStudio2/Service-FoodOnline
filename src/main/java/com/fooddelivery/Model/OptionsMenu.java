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
@Table(name = "options_menu")
public class OptionsMenu {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long option_id;
	
	@NotNull
	private long option_menu_id;
	
	@NotNull
	private String option_name;
	
	@NotNull
	private float option_price;
	
	@NotNull
	private String option_status;
	
	public OptionsMenu() { }

	public OptionsMenu(long optionId) { 
	   this.option_id = optionId;
	}
	  
	public OptionsMenu
	(	
			long optionMenuId, 
			String optionName, 
			float optionPrice, 
			String optionStatus
	) {
	  this.option_menu_id = optionMenuId;
	  this.option_name = optionName;
	  this.option_price = optionPrice;
	  this.option_status = optionStatus;
	}
	
	public long getOptionId() {
		return option_id;
	}

	public void setOptionId(long optionId) {
		this.option_id = optionId;
	}

	public long getOptionMenuId() {
		return option_menu_id;
	}

	public void setOptionMenuId(long optionMenuId) {
		this.option_menu_id = optionMenuId;
	}

	public String getOptionName() { 
		return option_name; 
	}

	public void setOptionName(String optionName) {
		this.option_name = optionName;
	}
	
	public float getOptionPrice() {
		return option_price;
	}
	
	public void setOptionPrice(float optionPrice) {
		this.option_price = optionPrice;
	} 

	public String getOptionStatus() {
		return option_status;
	}

	public void setOptionStatus(String optionStatus) {
		this.option_status = optionStatus;
	}
}
