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
	
	private EditText username;
	private EditText password;
	private Button login;
	private Button register;
	private TextView loginLockedTV;
	private TextView attemptsLeftTV;
	private TextView numberOfRemainingLoginAttemptsTV;
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
		
        TextView groceryItem = ( TextView ) findViewById( R.id.groceryItem );
        Spinner categoryItem = ( Spinner ) findViewById( R.id.categoryItem );
        EditText priceItem = ( EditText ) findViewById( R.id.priceItem );
        EditText barcodeItem= ( EditText ) findViewById( R.id.barcodeItem );
        EditText dateItem = (EditText) findViewById(R.id.changeDateItem);
        EditText dateAddedItem = (EditText) findViewById(R.id.dateAddedItem);
        View imageItem = (View) findViewById(R.id.imageItem);
        
        groceryItem.setText(name);
        categoryItem.setSelection(Integer.parseInt(category));
        dateItem.setText(expiryDate);
        priceItem.setText(price);
        barcodeItem.setText(barcode);
        dateAddedItem.setText(dateAdded);
        imageItem.setBackgroundResource(CategoryId.getImageRes(Integer.parseInt(category)));
        
	}
	
	public void authenticateLogin(View view) throws JSONException {

		String result = null;
	    try {
	    	String call = "http://shelfe.host22.com/service/Service.php?method=checkLogin" +
	    			"&username=" + username.getText() +
	    			"&password=" + password.getText();
	    	result = new CallServer().execute(call).get();

	    } catch (Exception e) { 
	        // Oops
	    }
		if (result != null){
			    JSONObject jArray = new JSONObject(result);
		    	String success = jArray.getString("Success");
		    	
		    	if (success.equals("true")){
			
					Intent myIntent = new Intent(ItemView.this, LoggedIn.class);
					myIntent.putExtra("username",username.getText().toString());
					myIntent.putExtra("password",password.getText().toString());
					finish();
					startActivity(myIntent);
		    	}
		} else {
			Toast.makeText(getApplicationContext(), "Seems like you 're not admin!", 
					Toast.LENGTH_SHORT).show();
			numberOfRemainingLoginAttempts--;
			attemptsLeftTV.setVisibility(View.VISIBLE);
			numberOfRemainingLoginAttemptsTV.setVisibility(View.VISIBLE);
			numberOfRemainingLoginAttemptsTV.setText(Integer.toString(numberOfRemainingLoginAttempts));
			
			if (numberOfRemainingLoginAttempts == 0) {
				login.setEnabled(false);
				loginLockedTV.setVisibility(View.VISIBLE);
				loginLockedTV.setBackgroundColor(Color.RED);
				loginLockedTV.setText("LOGIN LOCKED!!!");
			}
		}
	}

	public void register(View view) {

			Intent myIntent = new Intent(ItemView.this, Register.class);

			startActivity(myIntent);

	}
	


}