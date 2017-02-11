package com.fooddelivery.util;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
public class MyClass {

	public static void main(String[] args) {
		
		Connection connect = null;
		Statement s = null;
		String URL_DB = "jdbc:mysql://localhost:3307/test1?&useSSL=false";
		String USER = "root";
		String PASSWORD = "root";
		System.out.println("Test.");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(URL_DB, USER, PASSWORD);

			if(connect != null){
				System.out.println("Database Connected.");
			} else {
				System.out.println("Database Connect Failed.");
			}
			s = connect.createStatement();
			
			String CusID = "001";			
//			String sql = "SELECT * FROM  customer WHERE cus_id = '" + CusID + "' ";
			String sql = "SELECT * FROM  customer";
			ResultSet rec = s.executeQuery(sql);
			if(rec == null)
			{
				System.out.print(" record not found ");
			}

			while((rec!=null) && (rec.next()))
            {
                System.out.print(rec.getString("cus_id"));
                System.out.print(rec.getString("cus_name"));
                System.out.print(" - ");

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
		
	}
	
}