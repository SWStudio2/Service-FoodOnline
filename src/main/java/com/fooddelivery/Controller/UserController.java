package com.fooddelivery.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.Model.User;
import com.fooddelivery.Model.UserDao;

@RestController
public class UserController {
	
	  
	  // Wire the UserDao used inside this controller.
	  @Autowired
	  private UserDao userDao;
	  
	// ------------------------
	  // PUBLIC METHODS
	  // ------------------------
	  
	  /**
	   * /create  --> Create a new user and save it in the database.
	   * 
	   * @param email User's email
	   * @param name User's name
	   * @return A string describing if the user is succesfully created or not.
	   */
	  @RequestMapping(value="/create"  , method=RequestMethod.POST)
	  @ResponseBody
	  public String create(@RequestBody User user) {
	
	    try {
	      userDao.save(user);
	    }
	    catch (Exception ex) {
	      return "Error creating the user: " + ex.toString();
	    }
	    return "User succesfully created! (id = " + user.getId() + ")";
	  }
	  
	  /**
	   * /delete  --> Delete the user having the passed id.
	   * 
	   * @param id The id of the user to delete
	   * @return A string describing if the user is succesfully deleted or not.
	   */
	  @RequestMapping("/delete")
	  @ResponseBody
	  public String delete(long id) {
	    try {
	      User user = new User(id);
	      userDao.delete(user);
	    }
	    catch (Exception ex) {
	      return "Error deleting the user: " + ex.toString();
	    }
	    return "User succesfully deleted!";
	  }
	  
	  /**
	   * /get-by-email  --> Return the id for the user having the passed email.
	   * 
	   * @param email The email to search in the database.
	   * @return The user id or a message error if the user is not found.
	   */
	  @RequestMapping(value="/check" , method=RequestMethod.GET)
	  @ResponseBody
	  public String getByEmail(String email) {
	    try {
	      List<User> user = userDao.findByEmail(email);
	      if(user.size() > 0){
	    	  return "found"; 
	      }else{
	    	  return "User Not found";
	      }
	    }
	    catch (Exception ex) {
	      return ex.toString();
	    }
	 
	  }
	  
	  @RequestMapping(value="/checkName" , method=RequestMethod.GET)
	  @ResponseBody
	  public String getNameByEmail(String email) {
	    try {
	      String name = userDao.findNameByEmail(email);
	      if(name == null){
	    	  return "not found";
	      }else{
	    	  return "found " + name;
	      }
	    }
	    catch (Exception ex) {
	      return ex.toString();
	    }
	 
	  }
	  
	  /**
	   * /update  --> Update the email and the name for the user in the database 
	   * having the passed id.
	   * 
	   * @param id The id for the user to update.
	   * @param email The new email.
	   * @param name The new name.
	   * @return A string describing if the user is succesfully updated or not.
	   */
	  @RequestMapping("/update")
	  @ResponseBody
	  public String updateUser(long id, String email, String name) {
	    try {
	      User user = userDao.findOne(id);
	      user.setEmail(email);
	      user.setName(name);
	      userDao.save(user);
	    }
	    catch (Exception ex) {
	      return "Error updating the user: " + ex.toString();
	    }
	    return "User succesfully updated!";
	  }


	  
}	
