package com.example.shelf_e;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.shelf_e.R.drawable;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Inventory extends Fragment{

	private static final String TAG = "com.sim.Achievements";
	
	public ArrayList<Grocery> groceries;
	
	public Inventory() throws JSONException, URISyntaxException, ClientProtocolException, IOException{
	   	 Log.i(TAG, "[ACTIVITY] Achievements");
	   	 
	   	groceries = new ArrayList<Grocery>();
	   	 //select achievements from db and fill allAchievements
	   	 //test code, remove when implimenting db
	   	
	   	//get json
	   	getJSON();
		
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.inventory_fragment, container, false);
  
        
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
          		 120,
          		 120);
           
           // Now the layout parameters, these are a little tricky at first
           LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                   LinearLayout.LayoutParams.MATCH_PARENT,
                   LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
           
           // Now the layout parameters, these are a little tricky at first
           LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                   LinearLayout.LayoutParams.MATCH_PARENT,
                   LinearLayout.LayoutParams.WRAP_CONTENT);
           layoutParams.setMargins(0, 5, 0, 10);
           
   	     
         
           
   	     for (Grocery grocery : groceries){

   		     ImageView image = new ImageView(this.getActivity());
   		     image.setImageResource(getImageRes(Integer.parseInt(grocery.getCategory())));   

   	         TextView name = new TextView(this.getActivity());
   	         name.setTextSize(22);
   	         name.setGravity(Gravity.CENTER);
   	         name.setTypeface(Typeface.DEFAULT_BOLD);
   	         TextView amount = new TextView(this.getActivity());
   	         amount.setGravity(Gravity.CENTER);
   	         amount.setTextColor(Color.parseColor("#32CD32"));
   	         TextView expiration = new TextView(this.getActivity());
   	         expiration.setGravity(Gravity.CENTER);
   	      	expiration.setTextColor(Color.parseColor("#32CD32"));
   	         
   	         
   	         name.setText(grocery.getName());
   	         amount.setText(new DecimalFormat("##.##").format(100*(Double.parseDouble(grocery.getCurrentAmount())/Double.parseDouble(grocery.getInitialAmount()))) + "%");
   	         expiration.setText("Expiration: " + grocery.getExpiryDate());
   	         // Let's get the root layout and add our ImageView
   	         LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.main);
   	        
   	         LinearLayout title = new LinearLayout(this.getActivity());
   	         title.setOrientation(LinearLayout.VERTICAL);
   	         LinearLayout desc = new LinearLayout(this.getActivity());
   	         desc.setOrientation(LinearLayout.HORIZONTAL);
   	         desc.setBackgroundColor(Color.parseColor("#ededed"));
   	         
   	         title.addView(name);
   	         title.addView(amount);
   	         title.addView(expiration);
   	         
   	         desc.addView(image, 0, param);
   	         desc.addView(title, 1,textParams);
   	
   	   
   	
   	         layout.addView(desc, layoutParams);  

           }
        
        
        return rootView;
		
	}
    
    private void getJSON() throws JSONException, URISyntaxException, ClientProtocolException, IOException{


        InputStream inputStream = null;
	    String result = null;
	    try {

	    	inputStream = new CallServer().execute(inputStream).get();
	        //HttpEntity entity = response.getEntity();
	
	        //inputStream = entity.getContent();
	        // json is UTF-8 by default
	        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
	        StringBuilder sb = new StringBuilder();
	
	        String line = null;
	        while ((line = reader.readLine()) != null)
	        {
	            sb.append(line + "\n");
	        }
	        result = sb.toString();
	    } catch (Exception e) { 
	        // Oops
	    }
	    finally {
	        try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
	    }
	    
	    JSONArray jArray = new JSONArray(result);
	    
	    //assign grocery properties
	    for (int i=0; i < jArray.length(); i++)
	    {	    
	    		JSONObject oneShelf = jArray.getJSONObject(i);
		    	JSONArray inventory = oneShelf.getJSONArray("Inventory");
		    	
		    for (int j=0; j < inventory.length(); j++)
		    {
		    	Grocery grocery = new Grocery();
		        try {
		            JSONObject oneObject = inventory.getJSONObject(j);
		            // Pulling items from the array
		            grocery.setCurrentAmount(oneObject.getString("CurrentAmount"));
		            grocery.setInitialAmount(oneObject.getString("InitialAmount"));
		            grocery.setBarcode(oneObject.getString("Barcode"));
		            grocery.setCategory(oneObject.getString("CategoryId"));
		            grocery.setExpiryDate(oneObject.getString("ExpiryDate"));
		            grocery.setName(oneObject.getString("Name"));
		            grocery.setPrice(oneObject.getString("Price"));
		            
		        } catch (JSONException e) {
		            // Oops
		        }
		        groceries.add(grocery);
		    }
	    }
	 
   		
    }
    
    
    private int getImageRes(int Category){
   
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
		case 8:
			image = drawable.category8;
			break;
		}
    	
    	return image;
    }
}