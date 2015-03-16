package com.fydp.shelfe;


import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ItemView extends ActionBarActivity {
	
	private String username;
	private String password;
	private Button login;
	private Button register;
	private TextView loginLockedTV;
	private TextView attemptsLeftTV;
	private TextView numberOfRemainingLoginAttemptsTV;
	private Button skip;
	int numberOfRemainingLoginAttempts = 3;


	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	static RecieptScanner rcFragment;
	static TempItemAdd pcFragment;
	static Inventory inFragment;
	static BarcodeScanner bsFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.item_view);
	    Bundle myIntent = getIntent().getExtras();
	    String name = myIntent.getString("name");
	    String price = myIntent.getString("price");	
	    String barcode = myIntent.getString("barcode");	
	    String expiryDate = myIntent.getString("expiryDate");	
	    String category = myIntent.getString("category");	
	    String dateAdded = myIntent.getString("dateAdded");
	    username = myIntent.getString("username");
	    password = myIntent.getString("password");
		
        TextView groceryItem = ( TextView ) findViewById( R.id.groceryItem );
        Spinner categoryItem = ( Spinner ) findViewById( R.id.categoryItem );
        EditText priceItem = ( EditText ) findViewById( R.id.priceItem );
        EditText barcodeItem= ( EditText ) findViewById( R.id.barcodeItem );
        EditText dateItem = (EditText) findViewById(R.id.changeDateItem);
        EditText dateAddedItem = (EditText) findViewById(R.id.dateAddedItem);
        View imageItem = (View) findViewById(R.id.imageItem);
        skip = (Button) findViewById(R.id.remove);
        skip.setOnClickListener( new View.OnClickListener(){
        	public void onClick( View view ){
        		try {
					removeItem();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        } );
	    
        
        groceryItem.setText(name);
        categoryItem.setSelection(Integer.parseInt(category));
        dateItem.setText(expiryDate);
        priceItem.setText(price);
        barcodeItem.setText(barcode);
        dateAddedItem.setText(dateAdded);
        imageItem.setBackgroundResource(CategoryId.getImageRes(Integer.parseInt(category)));
        
	}
	
	public void removeItem() throws JSONException{
		String result = null;
	    try {
	    	String call = "http://shelfe.host22.com/service/Service.php?method=removeItem" +
	    			"&username=" + username +
	    			"&password=" + password + 
	    			"&status=0";
	    	result = new CallServer().execute(call).get();

	    } catch (Exception e) { 
	        // Oops
	    }
		if (result != null){
			    JSONObject jArray = new JSONObject(result);
		    	String success = jArray.getString("Success");
		    	
		    	if (success.equals("true")){
		    		Toast.makeText(this, "Item Successfully Removed", Toast.LENGTH_LONG).show();

					finish();

		    	} else if (success.equals("false")){
		    		String error = jArray.getString("Error");
		    		Toast.makeText(this, error, Toast.LENGTH_LONG).show();
		    	}
		}
	}
	


	public void register(View view) {

			Intent myIntent = new Intent(ItemView.this, Register.class);

			startActivity(myIntent);

	}
	


}