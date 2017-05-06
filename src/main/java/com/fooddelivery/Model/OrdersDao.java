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
public interface OrdersDao extends CrudRepository<Orders, Long> {

	Logger logger = (Logger) LoggerFactory.getLogger(OrdersDao.class);

	/*
	 * "insert into commit_activity_link (commit_id, activity_id) VALUES (?1, ?2)"*/
	@Query(value = "insert into orders (order_cus_id, order_address, order_address_latitude,"
			+ "order_address_longtitude, order_datetime, order_datetime_delivery, "
			+ "order_delivery_rate, order_price, order_distance, order_status) values ("
			+ ":order_cus_id, :order_address, :order_address_latitude, :order_address_longtitude, "
			+ ":order_datetime, :order_datetime_delivery, :order_delivery_rate, "
			+ ":order_price, :order_distance, :order_status)"
			, nativeQuery = true)
	public void insertOrder(@Param("order_cus_id") long order_cus_id,
							@Param("order_address") String order_address,
							@Param("order_address_latitude") String order_address_latitude,
							@Param("order_address_longtitude") String order_address_longtitude,
							@Param("order_datetime") String order_datetime,
							@Param("order_datetime_delivery") String order_datetime_delivery,
							@Param("order_delivery_rate") int order_delivery_rate,
							@Param("order_price") double order_price,
							@Param("order_distance") double order_distance,
							@Param("order_status") String order_status);


	@Query(value="select * from orders o where o.order_cus_id = :order_cus_id" , nativeQuery = true)
	public List<Orders> findByOrderCusId(@Param("order_cus_id") Long order_cus_id);

	@Query(value="select * from orders o where o.order_cus_id = :order_cus_id order by order_id desc limit 1" , nativeQuery = true)
	public List<Orders> findLastOrderCusId(@Param("order_cus_id") Long order_cus_id);

	//SQL A1
	@Query(value="select IF(" +
			"(Select seqor_id  from sequence_orders where seqor_order_id = :seqorder"
			+ " and seqor_mer_id = :merid"
			+ " and seqor_confirm_code = :confirmcode"
			+ "),'Y','N') As CHK_CONFIRM" ,
			nativeQuery = true)
	public String verifyConfirmCodeMerchant(@Param("seqorder") int order_id,
											@Param("merid") int mer_id,
											@Param("confirmcode") String confirm_code);

	//SQL A2
	@Modifying
	@Query(value="UPDATE sequence_orders SET" +
			"SEQOR_RECEIVE_DATETIME = NOW(),"
			+ "SEQOR_RECEIVE_STATUS = :receive_status"
			+ "where SEQOR_ORDER_ID = :order_id"
			+ "and SEQOR_MER_ID = :mer_id" ,
			nativeQuery = true)
	public void updateReceiveStatus(@Param("receive_status") int receive_status,@Param("order_id") int order_id,
									@Param("mer_id") int mer_id);

	//SQL A3
	@Query(value="select IF(" +
			" (select count(seqor_id)" +
			" from sequence_orders" +
			" where seqor_order_id = :seqorder" +
			" and SEQOR_RECEIVE_STATUS = :receive_status" +
			" ) =  ("+
			" select count(seqor_id)"+
			" from sequence_orders" +
			" where seqor_order_id = :seqorder" +
			" and seqor_cook_status in (:seqor_cook_status)" +
			" ),'Y','N') As CHK_RECALL" ,
			nativeQuery = true)
	public String chkReceiveAllMerchantOrder(@Param("seqorder") long order_id,
											 @Param("receive_status") int receive_status,
											 @Param("seqor_cook_status") String seqor_cook_status);

	//SQL A4
	@Modifying
	@Query(value="UPDATE orders" +
			" SET"
			+ " ORDER_STATUS = :orderStatus"
			+ " where ORDER_ID = :order_id",
			nativeQuery = true)
	public void updateOrderStatus(@Param("orderStatus") int orderStatus,
								  @Param("order_id") long order_id);

	//SQL A5
	@Query(value="select IF(" +
			" (select count(seqor_id)"+
			" from sequence_orders"+
			" where seqor_order_id = :seqorder "+
			" and seqor_mess_id = :fulltimeid "+
			" and SEQOR_RECEIVE_STATUS = :receivestatus "+
			" ) = ("+
			" select count(seqor_id)"+
			" from sequence_orders"+
			" where seqor_order_id = :seqorder "+
			" and seqor_mess_id = :fulltimeid "+
			" and seqor_cook_status in (:seqorcookstatus )"+
			" ),'Y','N') As CHK_RECALL" ,
			nativeQuery = true)
	public String chkReceiveAllMerchantForMessenger(@Param("seqorder") int seqorder,
													@Param("fulltimeid") int fulltimeid,
													@Param("receivestatus") int receivestatus,
													@Param("seqorcookstatus") String seqorcookstatus);

	@Query(value = "select * from orders "
			+ "where order_id = :orderId"
			, nativeQuery = true)
	public Orders getOrderByOrderId(@Param("orderId") long orderId);

	@Query(value = "select * from orders "
			+ "where order_cus_id = :cusId order by order_id desc limit 1"
			, nativeQuery = true)
	public List<Orders> getCurrentOrderByCusId(@Param("cusId") long cusId);

	@Query(value = "select * from orders "
			+ "where order_cus_id = :cusId order by order_id desc"
			, nativeQuery = true)
	public List<Orders> getHistoryOrderByCusId(@Param("cusId") long cusId);

}
