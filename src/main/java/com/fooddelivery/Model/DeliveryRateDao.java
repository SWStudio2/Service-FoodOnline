package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface DeliveryRateDao extends CrudRepository<DeliveryRate, Long> {

	  /**
	   * Syntax
	   * 
	   * @param 
	   */
//	  @Query("select dr.delivery_rate from delivery_rate dr")
//	  public String findNameByEmail(@Param("delivery_rate") String deliveryRate);
}
