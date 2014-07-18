package com.fydp.shelfe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fydp.shelfe.RecieptScanner.ButtonClickHandler;
import com.googlecode.tesseract.android.TessBaseAPI;

public class TempItemAdd extends Fragment{

	
	protected Button _button;
	protected EditText _name;
	protected Spinner _category;
	protected EditText _expiryDate;
	protected EditText _price;
	
	protected static final String PHOTO_TAKEN	= "photo_taken";
		
	
	public TempItemAdd(){
		
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
    {
    	Log.i("PhotoCapture", "onCreateView" );
        //super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.temp_item_add_fragment, container, false);
       
        _name = ( EditText ) rootView.findViewById( R.id.name );
        _category = ( Spinner ) rootView.findViewById( R.id.category );
        _expiryDate = ( EditText ) rootView.findViewById( R.id.expiryDate );
        _price = ( EditText ) rootView.findViewById( R.id.price );
        _button = ( Button ) rootView.findViewById( R.id.add );
        _button.setOnClickListener( new View.OnClickListener(){
        	public void onClick( View view ){
        		Log.i("TempItemAdd", "ButtonClickHandler.onClick()" );
        		buttonHandler();
        	}
        } );

        
        return rootView;
    }
    

    
    protected void buttonHandler()
    {
    	Log.i("TempItemAdd", "buttonHandler()" );

        InputStream inputStream = null;
 	    String result = null;
 	    CategoryId catId = new CategoryId();
 	    try {

 	    	String call = 
 	    			
 	    			"http://shelfe.netau.net/service/Service.php?method=addItem&username=test&password=test" +
 	    			"&Barcode=1234567888" + 
 	    			"&CategoryId=" + catId.getCategory(_category.getSelectedItem().toString()) + 
 	    			"&ExpiryDate=" + _expiryDate.getText() + 
 	    			"&Name=" + _name.getText() + 
 	    			"&Price=" + _price.getText() + 
 	    			"&Override=false";
 	    			
 	    	inputStream = new addItem().execute(call).get();
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

    	
    	
    }
    

}
