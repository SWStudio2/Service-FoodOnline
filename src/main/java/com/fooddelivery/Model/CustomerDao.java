package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface CustomerDao extends CrudRepository<Customer, Long> {

	  /**
	   * Syntax
	   * 
	   * @param 
	   */
//	  @Query("select c.cus_name from Customer c where c.cus_email = :cus_email")
//	  public String findNameByEmail(@Param("cus_email") String cusEmail);
//	  
//	  @Query("from Customer c where c.cus_email = :cus_email")
//	  public List<User> findByCusEmail(@Param("cus_email") String cusEmail);
	
	
//	 select c.*,(select delivery_rate from delivery_rate)  as delivery_rate
//	 from customer c 
//	 where cus_username = 'user1@test.com' and cus_password = 'pass'
	@Query(value = "select * "
			+ "from customer "
			+ "where cus_username = :username and cus_password = :pass" 
	  		, nativeQuery = true)
	 public List<Customer> findByCusEmail(@Param("username") String username , @Param("pass") String pass);
	
	
	 //SQL B1
	 @Query(value="select IF("+
			 "(Select ORDER_ID from orders where ORDER_ID = :order_id"+
			 "and ORDER_CONFIRM_CODE = :confirmcode ),'Y','N') As CHK_CONFIRM" , 
	nativeQuery = true)
	 public String verifyConfirmCodeCustomer(@Param("order_id") long order_id,
			 @Param("confirmcode") String confirm_code);
	 
	 //SQL A2
	 @Modifying
	 @Query(value="UPDATE orders SET" +
	"ORDER_RECEIVE_DATETIME = :confirmDateTime"
	+ "ORDER_STATUS = :receive_status"
	+ "where ORDER_ID  = :order_id" , 
	nativeQuery = true)
	 public void updateReceiveStatusCustomer(@Param("confirmDateTime") String confirmDateTime,
			 @Param("receive_status") int receive_status,@Param("order_id") long order_id);	

	 @Query(value="select order_cus_id from orders where order_id = :order_id", 
	nativeQuery = true)
	 public int getCustomerIdByOrderId(@Param("order_id") long order_id);
	 
}
