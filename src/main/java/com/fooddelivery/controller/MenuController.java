package com.fooddelivery.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fooddelivery.Model.Menu;
import com.fooddelivery.Model.MenuQuery;
import com.fooddelivery.Model.OptionsMenu;
import com.fooddelivery.Model.OptionsMenuQuery;
import com.fooddelivery.util.Response;

public class MenuController {
	@RequestMapping(value = "/service/merchant/{merId}", method = RequestMethod.GET)
    public ResponseEntity<Response<List<Menu>>> getMenuByMerId(
            @PathVariable("merId") String merId) {

        List<Menu> menus = null;
        List<OptionsMenu> optionsMenu = null;
        
        try {
            MenuQuery tmpMenuQueryDB = new MenuQuery();
            menus = tmpMenuQueryDB.getMenuByID(merId);
            
            String menuIdAll = "";
            for(int i =  0;i<menus.size();i++)
            {
            	Menu tmp = menus.get(i);
            	if(i == 0)
            	{
            		menuIdAll = ""+tmp.getMenuId();
            	}
            	else
            	{
            		menuIdAll += ","+tmp.getMenuId();
            	}
            }
            
            OptionsMenuQuery optMenuQuery = new OptionsMenuQuery();
            optionsMenu = optMenuQuery.getMenuQueryByMenuId(menuIdAll);
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        
        for(int i = 0;i<menus.size();i++)
        {
        	Menu tmpMenu = menus.get(i);
        	List<OptionsMenu> arrOpt = new ArrayList<OptionsMenu>();
        	for(int j = 0;j<optionsMenu.size();j++)
        	{
        		OptionsMenu optMenu = optionsMenu.get(j);
        		
        		if(tmpMenu.getMenuId() == optMenu.getOptionMenuId())
        		{
        			arrOpt.add(optMenu);
        		}
        	}
        	
        	if(arrOpt.size() > 1)
        	{
        		tmpMenu.setOpt_menuList(arrOpt);
        	}
        }
        

        return new ResponseEntity<Response<List<Menu>>>(new Response<List<Menu>>(HttpStatus.OK.value(),
                "RM users fetched successfully", menus), HttpStatus.OK);
    }
}
