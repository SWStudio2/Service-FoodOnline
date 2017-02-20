package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface OptionsMenuDao extends CrudRepository<OptionsMenu, Long> {

	  /**
	   * Syntax
	   * 
	   * @param 
	   */
	  @Query("select op.option_name from Options_Menu op where op.option_id = :option_Id")
	  public String findNameById(@Param("option_id") String optionId);
}
