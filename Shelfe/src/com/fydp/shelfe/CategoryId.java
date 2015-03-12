package com.fydp.shelfe;

import com.fydp.shelfe.R.drawable;

public class CategoryId {

	public static int GROCERY = 1;
	public static int DAIRY  = 2;
	public static int PRODUCE = 3;
	public static int BEER = 4;
	public static int BEVERAGES = 5;
	public static int BAKED_GOODS= 6;
	public static int HOME = 7;
	

	public int getCategory(String category){
		
	   	int catId = CategoryId.GROCERY;
			switch (category) {
			case "Grocery":  
				catId = CategoryId.GROCERY;
				break;
			case "Dairy":
				catId = CategoryId.DAIRY;
				break;
			case "Produce":
				catId = CategoryId.PRODUCE;
				break;
			case "Beer":
				catId = CategoryId.BEER;
				break;
			case "Beverages":
				catId = CategoryId.BEVERAGES;
				break;
			case "Baked Goods":
				catId = CategoryId.BAKED_GOODS;
				break;
			case "Home":
				catId = CategoryId.HOME;
				break;
			}
	    	
	    	return catId;
	}
}
