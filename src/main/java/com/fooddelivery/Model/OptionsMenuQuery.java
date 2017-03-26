package com.fooddelivery.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.querydsl.QPageRequest;

public class OptionsMenuQuery {
	public static List<OptionsMenu> getMenuQueryByMenuId(String menuId)
	{
		  Connection connect = null;
			Statement s = null;
			String URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc";
			URL_DB = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/ad_4e5de0567b1e3fc?useUnicode=true&amp;characterEncoding=UTF-8";
			String USER = "ba8167e655c97d";
			String PASSWORD = "fcc5664d";
			List<OptionsMenu> optMenu = null;
			
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
							  
				sqlBuffer.append("SELECT * from options_menu opm where opm.option_menu_id in ("+menuId+")  \n ");
				
				sql = sqlBuffer.toString();	
				
				ResultSet rec = s.executeQuery(sql);
				
				if(rec == null)
				{
					System.out.print(" record not found ");
				}
//				System.out.println("" + sql);
				optMenu = new ArrayList<OptionsMenu>();
				while((rec!=null) && (rec.next()))
	            {
					OptionsMenu tmpOptMenu = new OptionsMenu();
					tmpOptMenu.setOptionId(rec.getInt("option_id"));
					tmpOptMenu.setOptionMenuId(rec.getInt("option_menu_id"));
					tmpOptMenu.setOptionName(rec.getString("option_name"));
					tmpOptMenu.setOptionPrice(rec.getFloat("option_price"));
					optMenu.add(tmpOptMenu);
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
		  return optMenu;
	}
}
