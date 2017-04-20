package com.fooddelivery.Model;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import ch.qos.logback.classic.Logger;

@Transactional
public interface SequenceOrdersDao extends CrudRepository<SequenceOrders, Long> {
	
	Logger logger = (Logger) LoggerFactory.getLogger(SequenceOrdersDao.class);

	@Query(value = "select seqor_id from sequence_orders "
	  		+ "where seqor_order_id = :orderId and seqor_mer_id = :merId and seqor_confirm_code = :confirmCode"
	  		, nativeQuery = true)
	public List<Integer> checkConfirmCode(@Param("orderId") long orderId, @Param("merId") long merId,
			  @Param("confirmCode") String confirmCode);
	
	@Query(value = "select * from sequence_orders "
	  		+ "where seqor_id = :seqId"
	  		, nativeQuery = true)
	public SequenceOrders getSequenceOrderById(@Param("seqId") long seqOrderId);
	
	@Query(value = "select * from sequence_orders "
			+ "where seqor_mess_id = :seqMessId"
			, nativeQuery = true)
	public SequenceOrders getSequenceOrderByMessId(@Param("seqMessId") long seqMessId);
	
	@Query(value = "select * from sequence_orders"
			, nativeQuery = true)
	public List<SequenceOrders> findAll();
}
