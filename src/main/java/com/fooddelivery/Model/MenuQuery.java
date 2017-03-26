package com.fooddelivery.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MenuQuery {
	public static List<Menu> getMenuByID(String merId)
	{
		  Connection connect = null;
			Statement s = null;
			String URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc";
			URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc?useUnicode=true&amp;characterEncoding=UTF-8";
			String USER = "ba8167e655c97d";
			String PASSWORD = "fcc5664d";
			List<Menu> menus = null;
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(URL_DB, USER, PASSWORD);
				
				if(connect != null){
					System.out.println("Database Connected.");
				} else {
					System.out.println("Database Connect Failed.");
				}
				s = connect.createStatement();
							
				String sql = "";
				
				StringBuffer sqlBuffer = new StringBuffer();
							  
				sqlBuffer.append("SELECT m.MENU_ID,m.MENU_NAME,m.MENU_PRICE,m.MENU_PIC_NAME,m.MENU_MER_ID FROM menu m WHERE menu_mer_id = '" + merId + "' AND menu_status = '1'  \n ");
				
				sql = sqlBuffer.toString();	
				
				ResultSet rec = s.executeQuery(sql);
				
				if(rec == null)
				{
					System.out.print(" record not found ");
				}
//				System.out.println("" + sql);
				menus = new ArrayList<Menu>();
				while((rec!=null) && (rec.next()))
	            {
					Menu tmpMenu = new Menu();
					tmpMenu.setMenuId(rec.getInt("MENU_ID"));
					tmpMenu.setMenuName(rec.getString("MENU_NAME"));
					tmpMenu.setMenuPrice(rec.getFloat("MENU_PRICE"));
					tmpMenu.setMenu_picName(rec.getString("MENU_PIC_NAME"));
					tmpMenu.setMenuMerId(rec.getInt("MENU_MER_ID"));
					menus.add(tmpMenu);
	            }
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Close
			try {
				if(connect != null){
					connect.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				  
		  return menus;
	}
}
