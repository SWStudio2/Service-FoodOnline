package com.fooddelivery.Controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import com.fooddelivery.Model.Customer;
import com.fooddelivery.Model.Merchants;

public class mainProg2 {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String landtitude = "13.733510";
		String longtitude = "100.531236";
		
		Merchants[] merList = new Merchants[14];
		for(int i = 0;i<merList.length;i++)
		{
			merList[i] = new Merchants();
		}

		merList[0].setMerName("มนต์นมสด เสาชิงช้า");
		merList[0].setMerLatitude("13.754198");
		merList[0].setMerLongtitude("100.501185");
		
		merList[1].setMerName("โกปี๊เฮี้ยะไถ่กี่ ณ เสาชิงช้า");
		merList[1].setMerLatitude("13.753173");
		merList[1].setMerLongtitude("100.504395");		

		merList[2].setMerName("รองเมือง เกาเหลา");
		merList[2].setMerLatitude("13.745967");
		merList[2].setMerLongtitude("100.518322");
		
		merList[3].setMerName("ทิพย์สมัย (ผัดไทยประตูผี)");
		merList[3].setMerLatitude("13.752774");
		merList[3].setMerLongtitude("100.504842");
				
		merList[4].setMerName("ฮ่องกง นู้ดเดิ้ล หัวลำโพง");
		merList[4].setMerLatitude("13.738376");
		merList[4].setMerLongtitude("100.517617");			
				
		merList[5].setMerName("ภัตตาคารตั้งจั๊วหลี หัวปลาหม้อไฟ");
		merList[5].setMerLatitude("13.736537");
		merList[5].setMerLongtitude("100.514813");
		
		merList[6].setMerName("เจ้แดง สามย่าน");
		merList[6].setMerLatitude("13.734588");
		merList[6].setMerLongtitude("100.526765");	
				
		merList[7].setMerName("ซานตาเฟ่ สเต็กเฮ้าส์ มาบุญครอง");
		merList[7].setMerLatitude("13.745660");
		merList[7].setMerLongtitude("100.530299");
		
		merList[8].setMerName("Baan Ying:Cafe and Meal Siam Sq1");
		merList[8].setMerLatitude("13.744638");
		merList[8].setMerLongtitude("100.5257969");
		
		merList[9].setMerName("ครัวดอกไม้ขาว Siam Sq1");
		merList[9].setMerLatitude("13.745399");
		merList[9].setMerLongtitude("100.533786");
		
		merList[10].setMerName("Shakariki 432 Taniya สุรวงศ์");
		merList[10].setMerLatitude("13.730481");
		merList[10].setMerLongtitude("100.532721");
													
		merList[11].setMerName("Katsu Shin");
		merList[11].setMerLatitude("13.729207");
		merList[11].setMerLongtitude("100.529971");
		
		merList[12].setMerName("ต้นกก สีลม");
		merList[12].setMerLatitude("13.725766");
		merList[12].setMerLongtitude("100.530970");
		
		merList[13].setMerName("ร้านลุงใหญ่");
		merList[13].setMerLatitude("13.763263");
		merList[13].setMerLongtitude("100.546167");
		
		HomeController home = new HomeController();
		
		ArrayList<String> disList = new ArrayList<String>();
		ArrayList<String> durationList = new ArrayList<String>();
		for(int i = 0;i<merList.length;i++)
		{
			Thread.sleep(1000);
			Merchants tmpMer = merList[i];
			String[] arrDetail = (String[])home.getDistanceDuration(landtitude, longtitude, tmpMer.getMerLatitude(), tmpMer.getMerLongtitude());
			System.out.println(tmpMer.getMerName());

			System.out.println("Distance : " + arrDetail[0]);
			System.out.println("Duration : " + arrDetail[1]);
			disList.add(arrDetail[0]);
			durationList.add(arrDetail[1]);
		}
		
		//addExcel(disList, durationList);
	
	}
	
	public static void addExcel(ArrayList<String> disList,ArrayList<String> durationList) throws EncryptedDocumentException, InvalidFormatException
	{
        String excelFilePath = "E:\\Template2.xls";
        
        try {
            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
            Workbook workbook = WorkbookFactory.create(inputStream);
 
            Sheet sheet = workbook.getSheetAt(0);
 
            int columnCount = 0;
                   
            for(int i = 0;i<durationList.size();i++)
            {
                Cell cell2Update = sheet.getRow(i+2).getCell(15);
                cell2Update.setCellValue(durationList.get(i));               
            }
 
            inputStream.close();
 
            FileOutputStream outputStream = new FileOutputStream("E:\\Template2.xls");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
             
        } catch (IOException  ex) {
            ex.printStackTrace();
        }
        return;
	}

}
