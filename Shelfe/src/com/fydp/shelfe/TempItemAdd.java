package com.fydp.shelfe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.fydp.shelfe.RecieptScanner.ButtonClickHandler;
import com.googlecode.tesseract.android.TessBaseAPI;

public class TempItemAdd extends Fragment{

	
	protected Button _button;
	protected EditText _name;
	protected Spinner _category;
	protected Spinner _edDay;
	protected Spinner _edMonth;
	protected Spinner _edYear;
	protected EditText _price;
	
	protected static final String PHOTO_TAKEN	= "photo_taken";
		
	
	public TempItemAdd(){
		
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
    {
    	container.removeAllViews();
    	Log.i("PhotoCapture", "onCreateView" );
        //super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.temp_item_add_fragment, container, false);
       
        _name = ( EditText ) rootView.findViewById( R.id.name );
        _category = ( Spinner ) rootView.findViewById( R.id.category );
        _edDay = ( Spinner ) rootView.findViewById( R.id.day );
        _edMonth = ( Spinner ) rootView.findViewById( R.id.month );
        _edYear = ( Spinner ) rootView.findViewById( R.id.year );
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

    	boolean success = false;
        InputStream inputStream = null;
 	    String result = null;
 	    CategoryId catId = new CategoryId();
 	    try {

 	      String str = _edDay.getSelectedItem().toString() + "-" + _edMonth.getSelectedItem().toString() + "-" + _edYear.getSelectedItem().toString();
 	      SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
 	      Date date = df.parse(str);
 	      long epoch = date.getTime()/1000L;
 	      
 	    	String call = 
 	    			
 	    			"http://shelfe.netau.net/service/Service.php?method=addItem&username=test&password=test" +
 	    			"&Barcode=1234567888" + 
 	    			"&CategoryId=" + catId.getCategory(_category.getSelectedItem().toString()) + 
 	    			"&ExpiryDate=" + epoch + 
 	    			"&Name=" + _name.getText() + 
 	    			"&Price=" + _price.getText() + 
 	    			"&Override=false";
	 	    	
 	    	while (success == false){	 	    			
	 	    	inputStream = new CallServer().execute(call).get();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		        StringBuilder sb = new StringBuilder();
		
		        String line = null;
		        while ((line = reader.readLine()) != null)
		        {
		            sb.append(line + "\n");
		        }
		        result = sb.toString();
		        Log.i("TempItemAddAfter", result);
	 	    	final JSONObject oneObject=new JSONObject(result);
	 	    	
	 	    	if (oneObject.getString("Success") == "true"){
	 	    		success = true;
	 	    		Toast.makeText(getView().getContext(), R.string.item_added, Toast.LENGTH_LONG).show();
	 	    	}else if (oneObject.getString("Error") == "MissingItem"){
	 	    		
	 	    		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getView().getContext());

	 	    		// Setting Dialog Title
	 	    		alertDialog.setTitle("Grocery Warning!");

	 	    		// Setting Dialog Message
	 	    		alertDialog.setMessage("There is currently an item outstanding.");
	 	    		alertDialog.show();
	 	    		
	 	    	}
 	    	}
 	        //HttpEntity entity = response.getEntity();
 	
 	        //inputStream = entity.getContent();
 	        // json is UTF-8 by default
 	        //BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
 	        //StringBuilder sb = new StringBuilder();
 	
// 	        String line = null;
// 	        while ((line = reader.readLine()) != null)
// 	        {
// 	            sb.append(line + "\n");
// 	        }
// 	        result = sb.toString();
 	    } catch (Exception e) { 
 	        // Oops
 	    }
 	    finally {
 	        try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
 	    }
 	    _name.setText("");
 	    _category.setSelection(0);
 	    _edDay.setSelection(0);
 	    _edMonth.setSelection(0);
 	    _edYear.setSelection(0);
 	    _price.setText("");
 	    
    	
    	
    }
    

}
