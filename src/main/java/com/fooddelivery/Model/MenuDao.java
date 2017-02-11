package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface MenuDao extends CrudRepository<Menu, Long> {

	  /**
	   * Syntax
	   * 
	   * @param 
	   */
	  @Query("select m.menu_name from Menu m where m.menu_mer_id = :menuMerId")
	  public String findNameByMerchantId(@Param("menu_mer_id") String menuMerId);
}
