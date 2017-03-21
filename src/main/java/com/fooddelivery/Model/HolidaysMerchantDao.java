package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface HolidaysMerchantDao extends CrudRepository<HolidaysMerchant, Long> {

	  /**
	   * Syntax
	   * 
	   * @param 
	   */
//	  @Query("select hm.hol_date from holidays_merchant hm where hm.hol_mer_id = :holidaysMerchantId")
//	  public String findHolidaysDateByMerchantId(@Param("hol_mer_id") String holidaysMerchantId);
}
