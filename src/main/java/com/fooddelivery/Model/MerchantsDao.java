package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

@Transactional
public interface MerchantsDao extends CrudRepository<Merchants, Long> {

	  /**
	   * Syntax
	   * 
	   * @param 
	   */
//	  @Query("select m.mer_name from Merchants m where m.mer_id = :merId")
//	  public String findNameByMerchantId(@Param("mer_id") String merId);
		
	
	//	SELECT * 
	//	FROM merchants 
	//	WHERE 
	//	mer_open_status = 'open' 
	//		and mer_id not in (
	//			SELECT mer_id 
	//			FROM holidays_merchant 
	//			WHERE HOL_DATE = CURDATE()
	//		) 
	//		and mer_name like '%merName%'

	  @Query(value = "SELECT * FROM merchants "
	  		+ "WHERE mer_open_status = 'open' "
	  		+ "and mer_id not in ( SELECT mer_id FROM holidays_merchant WHERE HOL_DATE = CURDATE() ) " 
	  		, nativeQuery = true)
	  public List<Merchants> findMerchantsByStatus();
	  
	  
	  @Query(value = "SELECT * FROM merchants "
		  		+ "WHERE mer_open_status = 'open' "
		  		+ "and mer_id not in ( SELECT mer_id FROM holidays_merchant WHERE HOL_DATE = CURDATE() ) "
		  		+ "and mer_name like concat('%',:mername,'%') " 
		  		, nativeQuery = true)
	  public List<Merchants> findMerchantsByMerName(@Param("mername") String mername);
	  
	  
}
