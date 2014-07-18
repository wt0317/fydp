package com.fydp.shelfe;

import com.fydp.shelfe.R.drawable;

public class CategoryId {

	public static int BABY_FOOD = 1;
	public static int BAKING  = 2;
	public static int BEER = 3;
	public static int BEVERAGES = 4;
	public static int DAIRY = 5;
	public static int FRUITS = 6;
	public static int BREAD = 7;
	public static int VEGETABLES = 8;
	

	public int getCategory(String category){
		
	   	int catId = CategoryId.BABY_FOOD;
			switch (category) {
			case "Baby Food":  
				catId = CategoryId.BABY_FOOD;
				break;
			case "Baking":
				catId = CategoryId.BAKING;
				break;
			case "Beer":
				catId = CategoryId.BEER;
				break;
			case "Beverages":
				catId = CategoryId.BEVERAGES;
				break;
			case "Dairy":
				catId = CategoryId.DAIRY;
				break;
			case "Fruits":
				catId = CategoryId.FRUITS;
				break;
			case "Bread":
				catId = CategoryId.BREAD;
				break;
			case "Vegetables":
				catId = CategoryId.VEGETABLES;
				break;
			}
	    	
	    	return catId;
	}
}
