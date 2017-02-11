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
	  @Query("select ft.full_name from fulltime_messenger ft where ft.full_email = :full_email")
	  public String findNameByEmail(@Param("full_email") String fullEmail);
	  
	  @Query("from fulltime_messenger ft where ft.full_email = :full_email")
	  public List<User> findByCusEmail(@Param("full_email") String fullEmail);
}
