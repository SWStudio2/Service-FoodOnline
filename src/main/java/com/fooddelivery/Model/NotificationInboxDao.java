package com.fooddelivery.Model;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.fooddelivery.Model.Order.OrderMerchant;

@Transactional
public interface NotificationInboxDao extends CrudRepository<Merchants, Long> {
	
	@Query(value = "select * "
			+ "from notification_inbox "
			+ " where noti_read_flag = 'N' and noti_ref_id = :noti_ref_id and noti_type = :noti_type" 
	  		, nativeQuery = true)
	 public List<NotificationInbox> findNotiNonRead(@Param("noti_ref_id") int ref_id ,@Param("noti_type") String noti_type);
		

}
