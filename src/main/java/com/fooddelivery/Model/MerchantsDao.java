package com.fooddelivery.Model;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface MerchantsDao extends CrudRepository<Merchants, Long> {

	//	SELECT * 
	//	FROM merchants 
	//	WHERE 
	//	mer_open_status = 'open' 
	//		and mer_id not in (
	//			SELECT mer_id 
	//			FROM holidays_merchant 
	//			WHERE HOL_DATE = CURDATE()
	//		) 
	//		and mer_id in (
	//			select mer_id from working_day_merchant 
	//			where work_day = DAYNAME('$date')
	//			and work_open_time < '$time'
	//			and work_close_time > '$time'
	//			and work_day_status = 'open’
	//		)
	//		and mer_name like '%merName%'

	  @Query(value = "SELECT * FROM merchants "
	  		+ "WHERE mer_open_status = 'open' "
	  		+ "and mer_id not in ( SELECT mer_id FROM holidays_merchant WHERE HOL_DATE = CURDATE() ) "
	  		+ "and mer_id in ( select mer_id from working_day_merchant where work_day = DAYNAME(:date) "
	  		+ "and work_open_time < :time "
	  		+ "and work_close_time > :time "
	  		+ "and work_day_status = 'open' )" 
	  		, nativeQuery = true)
	  public List<Merchants> findMerchantsByStatus(@Param("date") String date, @Param("time") String time);
	  
	  
	  @Query(value = "SELECT * FROM merchants "
		  		+ "WHERE mer_open_status = 'open' "
		  		+ "and mer_id not in ( SELECT mer_id FROM holidays_merchant WHERE HOL_DATE = CURDATE() ) "
		  		+ "and mer_id in ( select mer_id from working_day_merchant where work_day = DAYNAME(:date) "
		  		+ "and work_open_time < :time "
		  		+ "and work_close_time > :time "
		  		+ "and work_day_status = 'open' )"
		  		+ "and mer_name like concat('%',:mername,'%') " 
		  		, nativeQuery = true)
	  public List<Merchants> findMerchantsByMerName(@Param("mername") String mername , @Param("date") String date, @Param("time") String time);
	  
}
