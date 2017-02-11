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
@Table(name = "delivery_rate")
public class DeliveryRate {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long delivery_rate;
	
	public DeliveryRate() { }

	public DeliveryRate(long deliveryRate) { 
	   this.delivery_rate = deliveryRate;
	}
	
	public long getDeliveryRate() {
		return delivery_rate;
	}

	public void setDeliveryRate(long deliveryRate) {
		this.delivery_rate = deliveryRate;
	}
}
