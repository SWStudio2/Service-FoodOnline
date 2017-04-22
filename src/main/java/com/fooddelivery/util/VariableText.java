package com.fooddelivery.util;

public class VariableText {
	
	public static int ORDER_WAITING_RESPONSE_STATUS 	= 1;//"รอรับออร์เดอร์";
	public static int ORDER_COOKING_STATUS 				= 2; //กำลังทำอาหาร
	public static int ORDER_DELIVERING_STATUS 			= 3; //กำลังส่งอาหาร
	public static int ORDER_RECEIVED_STATUS 			= 4; //ส่งอาหารแล้ว
	
	public static int MESSENGER_WAITING_CONFIRM_STATUS 	= 5; //รอการคอนเฟิร์ม
	public static int MESSENGER_RECEIVING_STATUS 		= 6; //กำลังไปรับอาหาร
	public static int MESSENGER_IGNORE_STATUS 			= 7; //ไม่ทำ
	public static int MESSENGER_DELIVERING_STATUS		= 8; //กำลังไปส่งอาหาร
	public static int MESSENGER_DELIVERIED_STATUS		= 9; //ส่งอาหารแล้ว
	public static int MESSENGER_STATION_STATUS			= 10; //อยู่ที่จุดจอด
	
	public static int MERCHANT_WAITING_CONFIRM_STATUS	= 11; // รอการคอนเฟิร๋ม
	public static int MERCHANT_CONFIRMED_STATUS			= 12; //คอนเฟิร์มแล้ว
	public static int MERCHANT_IGNORE_STATUS			= 13; //ไม่ทำ
	public static int MERCHANT_RECEIVED_STATUS			= 14; //รับอาหารแล้ว
	
	public static String BIKE_PATH_TYPE = "bike";

}
