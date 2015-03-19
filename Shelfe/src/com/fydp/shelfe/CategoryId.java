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
	
    public static int getImageRes(int Category){
    	   
    	int image = drawable.category1;
		switch (Category) {
		case 1:  
			image = drawable.category1;
			break;
		case 2:
			image = drawable.category2;
			break;
		case 3:
			image = drawable.category3;
			break;
		case 4:
			image = drawable.category4;
			break;
		case 5:
			image = drawable.category5;
			break;
		case 6:
			image = drawable.category6;
			break;
		case 7:
			image = drawable.category7;
			break;
		}
    	
    	return image;
    }
}
