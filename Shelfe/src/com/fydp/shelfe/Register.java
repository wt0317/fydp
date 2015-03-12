package com.fydp.shelfe;


import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
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
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

public class Register extends ActionBarActivity {
	
	private EditText username;
	private EditText password;
	private EditText confirmPassword;
	private EditText email;
	private Button register;
	private Button verifyUser;
	private TextView passText;
	private EditText passcode;
	private TextView userText;
	private TextView passwordText;
	private TextView cpasswordText;



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
		
		setContentView(R.layout.register);
		setupVariables();		
		
	}
	
	public void authenticateUser(View view) throws JSONException{
		if (passcode.getVisibility() == View.GONE){
			if (username != null && email != null && password != null && confirmPassword != null){
				if (confirmPassword.getText() == password.getText()){
					Toast.makeText(getApplicationContext(), "Passwords do not match", 
					Toast.LENGTH_SHORT).show();
				} else {
					String result = null;
				    try {
				    	String call = "http://shelfe.host22.com/service/Service.php?method=newUser" +
				    			"&username=" + username.getText() +
				    			"&password=" + password.getText() +
				    			"&email=" + email.getText();
				    	call = call.replace(" ","_");
				    	result = new CallServer().execute(call).get();
	
				    } catch (Exception e) { 
				        // Oops
				    }
				    if (result != null){
					    JSONObject jArray = new JSONObject(result);
				    	String success = jArray.getString("Success");
				    	
				    	if (success.equals("true")){
				    	
				    		Toast.makeText(getApplicationContext(), "Please check your email for your passcode", 
								Toast.LENGTH_LONG).show();
				    			verifyUser(view);
				    	}else{
				    		Toast.makeText(getApplicationContext(), "Username and/or email already taken", 
									Toast.LENGTH_LONG).show();
				    	}
				    }
				}
			}else{
				Toast.makeText(getApplicationContext(), "Please populate all fields", 
				Toast.LENGTH_SHORT).show();
			}
		}else{
			String result = null;
		    try {
		    	String call = "http://shelfe.host22.com/service/Service.php?method=confirmNewUser" +
		    			"&email=" + email.getText() +
		    			"&secret=" + passcode.getText();
		    	result = new CallServer().execute(call).get();

		    } catch (Exception e) { 
		        // Oops
		    }
		    if (result != null){
			    JSONObject jArray = new JSONObject(result);
		    	String success = jArray.getString("Success");
		    	
		    	if (success.equals("true")){
		    		Toast.makeText(getApplicationContext(), "Account Created", 
						Toast.LENGTH_SHORT).show();
		    		this.finish();
		    		Intent myIntent = new Intent(Register.this, MainActivity.class);
		    		startActivity(myIntent);
		    	}
		    }
		}
	}
	
	public void verifyUser(View view){
		passText.setVisibility(View.VISIBLE);
		passcode.setVisibility(View.VISIBLE);
		userText.setVisibility(View.GONE);
		username.setVisibility(View.GONE);
		passwordText.setVisibility(View.GONE);
		password.setVisibility(View.GONE);
		cpasswordText.setVisibility(View.GONE);
		confirmPassword.setVisibility(View.GONE);
		register.setVisibility(View.GONE);
		verifyUser.setText("VERIFY");
		verifyUser.setOnClickListener( new View.OnClickListener(){
        	public void onClick( View view ){
        		try {
					authenticateUser(view);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        } );
	}
	
	
	private void setupVariables() {
		userText = (TextView) findViewById(R.id.userText);
		username = (EditText) findViewById(R.id.username);
		email = (EditText) findViewById(R.id.email);
		passwordText = (TextView) findViewById(R.id.passwordText);
		password = (EditText) findViewById(R.id.password);
		cpasswordText = (TextView) findViewById(R.id.cpasswordText);
		confirmPassword = (EditText) findViewById(R.id.passwordConfirm);
		register = (Button) findViewById(R.id.register);
		verifyUser = (Button) findViewById(R.id.verifyUser);
		passText = (TextView) findViewById(R.id.passcodeText);
		passcode = (EditText) findViewById(R.id.passcode);
		

	}


}