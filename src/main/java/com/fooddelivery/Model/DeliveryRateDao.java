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
	@Query(value = "select * from delivery_rate" , nativeQuery = true )
	 public List<DeliveryRate> findAllDeliveryRate();
}
