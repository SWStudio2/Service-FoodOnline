package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface FullTimeMessengerDao extends CrudRepository<FullTimeMessenger, Long> {

	  /**
	   * Syntax
	   * 
	   * @param 
	   */
	  
	  @Query("select * from fulltime_messenger f where f.Full_status = 'ว่าง'")
	  public List<FullTimeMessenger> findEmptyFullTimeMessenger();
}
