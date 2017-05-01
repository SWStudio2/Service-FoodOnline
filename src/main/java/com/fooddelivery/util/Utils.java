package com.fooddelivery.util;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	
	public static List<List<Integer>> listPermutations(List<Integer> list) {

	    if (list.size() == 0) {
	        List<List<Integer>> result = new ArrayList<List<Integer>>();
	        result.add(new ArrayList<Integer>());
	        return result;
	    }

	    List<List<Integer>> returnMe = new ArrayList<List<Integer>>();

	    Integer firstElement = list.remove(0);

	    List<List<Integer>> recursiveReturn = listPermutations(list);
	    for (List<Integer> li : recursiveReturn) {

	        for (int index = 0; index <= li.size(); index++) {
	            List<Integer> temp = new ArrayList<Integer>(li);
	            temp.add(index, firstElement);
	            returnMe.add(temp);
	        }

	    }
	    return returnMe;
	}
	
	public static int toIntExact(long value) {
	    if ((int)value != value) {
	        throw new ArithmeticException("integer overflow");
	    }
	    return (int)value;
	}

}
