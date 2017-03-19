package com.fooddelivery.Model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "delivery_rate")
public class DeliveryRate {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "rate_id")
	private long rate_id;
	
	@Column(name = "delivery_rate")
	private Float delivery_rate;
	
	public DeliveryRate() { }
	
	public DeliveryRate(long rate_id) { 
		   this.rate_id = rate_id;
		}

	public DeliveryRate(Float deliveryRate) { 
	   this.delivery_rate = deliveryRate;
	}
	
	public Float getDeliveryRate() {
		return delivery_rate;
	}

	public void setDeliveryRate(Float deliveryRate) {
		this.delivery_rate = deliveryRate;
	}

	public long getRate_id() {
		return rate_id;
	}

	public void setRate_id(long rate_id) {
		this.rate_id = rate_id;
	}
	
	
}
