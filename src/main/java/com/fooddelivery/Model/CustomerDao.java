package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface CustomerDao extends CrudRepository<Customer, Long> {

	  /**
	   * Syntax
	   * 
	   * @param 
	   */
	  @Query("select c.cus_name from Customer c where c.cus_email = :cus_email")
	  public String findNameByEmail(@Param("cus_email") String cusEmail);
	  
	  @Query("from Customer c where c.cus_email = :cus_email")
	  public List<User> findByCusEmail(@Param("cus_email") String cusEmail);
}
