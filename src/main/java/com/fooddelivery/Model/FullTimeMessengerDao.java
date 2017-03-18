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
	  
//	  @Query("select full_id , full_name , full_contact_number , full_email , full_password , full_status , full_lastest_lattitude , full_lastest_longtitude , full_recommend_lattitude , full_recommend_longtitude from fulltime_messenger f where f.Full_status = 'ว่าง'")
//	  public List<FullTimeMessenger> findEmptyFullTimeMessenger();
}
