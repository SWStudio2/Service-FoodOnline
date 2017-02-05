package com.fooddelivery.Model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface UserDao extends CrudRepository<User, Long> {

	  /**
	   * Return the user having the passed email or null if no user is found.
	   * 
	   * @param email the user email.
	   */
	  @Query("select u.name from User u where u.email = :email")
	  public String findNameByEmail(@Param("email") String email);
	  
	  @Query("from User u where u.email = :email")
	  public List<User> findByEmail(@Param("email") String email);
}
