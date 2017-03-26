package com.fooddelivery.Model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "menu")
public class Menu {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int menu_id;
	
	@NotNull
	private int menu_mer_id;
	
	@NotNull
	private String menu_name;
	
	@NotNull
	private float menu_price;
	
	@NotNull
	private String menu_status;
	
	
	@NotNull
	private String menu_picName;
	
	public Menu() { }

	public Menu(int menuId) { 
	   this.menu_id = menuId;
	}
	  
	public Menu
	(	
			int menuMerId, 
			String menuName, 
			float menuPrice, 
			String menuStatus,
			String menu_PicName
	) {
	  this.menu_mer_id = menuMerId;
	  this.menu_name = menuName;
	  this.menu_price = menuPrice;
	  this.menu_status = menuStatus;
	  this.menu_picName = menu_PicName;
	}
	
	public long getMenuId() {
		return menu_id;
	}

	public void setMenuId(int menuId) {
		this.menu_id = menuId;
	}

	public long getMenuMerId() {
		return menu_mer_id;
	}

	public void setMenuMerId(int menuMerId) {
		this.menu_mer_id = menuMerId;
	}

	public String getMenuName() { 
		return menu_name; 
	}

	public void setMenuName(String menuName) {
		this.menu_name = menuName;
	}
	
	public float getMenuPrice() {
		return menu_price;
	}
	
	public void setMenuPrice(float menuPrice) {
		this.menu_price = menuPrice;
	} 

	public String getMenuStatus() {
		return menu_status;
	}

	public void setMenuStatus(String menuStatus) {
		this.menu_status = menuStatus;
	}

	public String getMenu_picName() {
		return menu_picName;
	}

	public void setMenu_picName(String menu_picName) {
		this.menu_picName = menu_picName;
	}
	
	
}
