package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface MerchantsDao extends CrudRepository<Menu, Long> {

	  /**
	   * Syntax
	   * 
	   * @param 
	   */
	  @Query("select m.mer_name from Merchants m where m.mer_id = :merId")
	  public String findNameByMerchantId(@Param("mer_id") String merId);

}
