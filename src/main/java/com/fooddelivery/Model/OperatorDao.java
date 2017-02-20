package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface OperatorDao extends CrudRepository<Operator, Long> {

	/**
	   * Syntax
	   * 
	   * @param 
	   */
	  @Query("select op.oper_name from Operator op where op.oper_id = :operId")
	  public String findNameById(@Param("oper_id") String operId);
}
