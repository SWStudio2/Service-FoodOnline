package com.fooddelivery.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fooddelivery.Model.Menu;
import com.fooddelivery.Model.MenuQuery;
import com.fooddelivery.util.Response;

public class MenuController {
	@RequestMapping(value = "/service/merchant/{merId}", method = RequestMethod.GET)
    public ResponseEntity<Response<List<Menu>>> getMenuByMerId(
            @PathVariable("merId") String merId) {

        List<Menu> menus = null;
        try {
            MenuQuery tmpMenuQueryDB = new MenuQuery();
            menus = tmpMenuQueryDB.getMenuByID(merId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return new ResponseEntity<Response<List<Menu>>>(new Response<List<Menu>>(HttpStatus.OK.value(),
                "RM users fetched successfully", menus), HttpStatus.OK);
    }
}
