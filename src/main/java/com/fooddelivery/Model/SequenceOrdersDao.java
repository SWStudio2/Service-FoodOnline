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

	@Query(value = "select sequence_orders.* from sequence_orders left join orders on sequence_orders.seqor_order_id = orders.order_id "
			+ "where order_id = :orderId and seqor_mess_id = :seqMessId"
			, nativeQuery = true)
	public List<SequenceOrders> getSequenceOrderByMessIdAndOrderId(@Param("orderId") long orderId, @Param("seqMessId") long seqMessId);

	@Query(value = "select * from sequence_orders"
			, nativeQuery = true)
	public List<SequenceOrders> findAll();
	
	@Query(value = "select * from sequence_orders where SEQOR_ORDER_ID = :order_id and SEQOR_MER_ID = :mer_id"
			, nativeQuery = true)
	public List<SequenceOrders> getSequenceOrderForUpdateStatus(@Param("order_id") int order_id,@Param("mer_id") int mer_id);
	
	//SQL B1
	@Modifying
	@Query(value="UPDATE sequence_orders SET" +
			" SEQOR_DELIVERY_DATETIME = NOW(),"
			+ "  SEQOR_RECEIVE_STATUS = :receive_status"
			+ " where SEQOR_ORDER_ID = :order_id"
			+ " and SEQOR_MESS_ID = :messId" ,
			nativeQuery = true)
	public void updateReceiveStatusSeqOrder(@Param("receive_status") int receive_status,@Param("order_id") int order_id,
									@Param("messId") int messId);	
	
	//SQL B5
	@Query(value = "select IF( "
			+ "( select count(seqor_id) "
			+ "from sequence_orders "
			+ "where seqor_order_id = :order_id "
			+ "and SEQOR_RECEIVE_STATUS = 9 "
			+ ") = ( select count(seqor_id) "
			+ "from sequence_orders "
			+ "where seqor_order_id = :order_id "
			+ "and seqor_cook_status = 12 "
			+ "),'Y','N') As CHK_RECALL"
			, nativeQuery = true)
	public String checkDeliveriedAllMenu(@Param("order_id") int order_id);
}
