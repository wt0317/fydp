package com.fydp.shelfe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
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
import com.fydp.shelfe.R.drawable;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShoppingList extends Fragment{

	private static final String TAG = "com.sim.ItemView";
	
	public ArrayList<Grocery> groceries;
	private String lastName = "";
	private String username = null;
	private String password = null;
	public ShoppingList() throws JSONException, URISyntaxException, ClientProtocolException, IOException, ParseException{
	   	 Log.i(TAG, "[ACTIVITY] Inventory");
	   	 
	   	 //select achievements from db and fill allAchievements
	   	 //test code, remove when implimenting db
	   	
		
	}
	
    @SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	Log.i(TAG, "onCreateView");
    	setHasOptionsMenu(true);
    	container.removeAllViews();
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(R.color.notclicked));
		
	    Bundle myIntent = getActivity().getIntent().getExtras();
	    username = myIntent.getString("username");
	    password = myIntent.getString("password");
    	groceries = new ArrayList<Grocery>();
	   	try {
			getJSON();
		} catch (JSONException | URISyntaxException
				| IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	container.removeAllViews();
        View rootView = inflater.inflate(R.layout.shopping_list, container, false);
  
    	//container.removeAllViews();
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
          
  	     
        
          
  	     for (final Grocery grocery : groceries){

  		     ImageView image = new ImageView(this.getActivity());
  		     image.setImageResource(CategoryId.getImageRes(Integer.parseInt(grocery.getCategory())));   

  	         TextView name = new TextView(this.getActivity());
  	         name.setTextSize(22);
  	         name.setGravity(Gravity.CENTER);
  	         name.setTypeface(Typeface.DEFAULT_BOLD);
  	         TextView amount = new TextView(this.getActivity());
  	         amount.setGravity(Gravity.CENTER);
  	         amount.setTextColor(amountColor(grocery.getCurrentAmount(), grocery.getInitialAmount()));
  	         TextView expiration = new TextView(this.getActivity());
  	         expiration.setGravity(Gravity.CENTER);
  	      	expiration.setTextColor(expiryColor(String.valueOf(System.currentTimeMillis()/1000), grocery.getExpiryDate()));
  	         
  	         
  	         name.setText(grocery.getName());
  	         double percent = Double.parseDouble(new DecimalFormat("##.#").format(100*(Double.parseDouble(grocery.getCurrentAmount())/Double.parseDouble(grocery.getInitialAmount()))));
  	         amount.setText(new DecimalFormat("##.#").format(100*(Double.parseDouble(grocery.getCurrentAmount())/Double.parseDouble(grocery.getInitialAmount()))) + "%");
	         if (percent < 50){ 
	  	         Date dateEx = new Date(Long.parseLong(grocery.getExpiryDate())*1000L);
	  	         Date dateAd = new Date(Long.parseLong(grocery.getDateAdded())*1000L);
		         DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		         format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
		         String formatted = format.format(dateEx);
		         String formattedAdded = format.format(dateAd);
		         grocery.setExpiryDate(formatted);
		         grocery.setDateAdded(formattedAdded);
	  	         expiration.setText("Expiration: " + formatted);
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
	  	
	  	   
	  	         desc.setOnClickListener( new View.OnClickListener(){
		        	public void onClick( View view ){
		        		prepareItem(grocery);
	
		        	}
	  	         } );
	  	         layout.addView(desc, layoutParams);  
	         }
  	         
          }       
 
        
        return rootView;
		
	}
    private void prepareItem(Grocery grocery){
		Intent myIntent = new Intent(getActivity(), ShoppingList.class);
		myIntent.putExtra("name", grocery.getName());
		myIntent.putExtra("barcode",grocery.getBarcode());	
		myIntent.putExtra("category",grocery.getCategory());
		myIntent.putExtra("expiryDate",grocery.getExpiryDate());
		myIntent.putExtra("dateAdded", grocery.getDateAdded());
		myIntent.putExtra("price",grocery.getPrice());
		startActivity(myIntent);
    }
    private void getJSON() throws JSONException, URISyntaxException, ClientProtocolException, IOException, ParseException{


        InputStream inputStream = null;
	    String result = null;
	    lastName = "";
	    try {
	    	String call = "http://shelfe.host22.com/service/Service.php?method=getInventory" +
	    			"&username=" + username +
	    			"&password=" + password;
	    	result = new CallServer().execute(call).get();

	    } catch (Exception e) { 
	        // Oops
	    }
	    finally {
	        try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
	    }
	    if (result != null){
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
			            if (!oneObject.getString("Name").equals(lastName)){
			            	
				            
				            grocery.setCurrentAmount(oneObject.getString("CurrentAmount"));
				            grocery.setInitialAmount(oneObject.getString("InitialAmount"));
				            grocery.setBarcode(oneObject.getString("Barcode"));
				            grocery.setCategory(oneObject.getString("CategoryId"));
				            grocery.setDateAdded(oneObject.getString("DateAdded"));
				            
				                
				            // Formats the date in the CET timezone   
				           //String myDate = df2.format(oneObject.getString("ExpiryDate"));
				            
				            grocery.setExpiryDate(oneObject.getString("ExpiryDate"));
				            grocery.setName(oneObject.getString("Name").replace("_", " "));
				            grocery.setPrice(oneObject.getString("Price"));
				            groceries.add(grocery);
				            lastName = oneObject.getString("Name");
			            }else{
			            	lastName = oneObject.getString("Name");
			            }
			        } catch (JSONException e) {
			            // Oops
			        }
			        
			    }
		    }
	    }
	 
   		
    }
    
    private int expiryColor(String first, String second){
    	//defaultt green color
    	String color = "#32CD32";
    	int firstInt = Integer.parseInt(first);
    	int secondInt =  Integer.parseInt(second);
    	if ((secondInt-firstInt) <= 172800){
    		color = "#FF0000";
    	}else if((secondInt-firstInt) <= 604800){
    		color = "#999900";
    	}
    	
    	return Color.parseColor(color);
    }
    
    private int amountColor(String first, String second){
    	//default green color
    	String color = "#32CD32";
    	double firstInt = Double.parseDouble(first);
    	double secondInt =  Double.parseDouble(second);
    	
    	if ( firstInt == 0 || ((double)(firstInt/secondInt) <= .25)){
    		color = "#FF0000";
    	}else if((double)(firstInt/secondInt) <= .50){
    		color = "#999900";
    	}
    	
    	return Color.parseColor(color);
    }
    

    
    public void onDestroy() {
        Log.i(TAG, "[ACTIVITY] onDestroy");
        super.onDestroy();

    }
    
    public void onResume(){
    	super.onResume();
    	Log.i(TAG, "onResume");
    	


  	     
    }
}
