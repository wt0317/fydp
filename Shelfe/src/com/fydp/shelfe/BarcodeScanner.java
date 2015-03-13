package com.fydp.shelfe;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.fydp.shelfe.RecieptScanner.ButtonClickHandler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class BarcodeScanner extends Fragment {
 
	private static final String TAG = "com.fydp.shelfe.BarcodeScanner";
    private Button scan;
    int RESULT_OK = -1;
    int RESULT_CANCELED = 1;
    public int SCANNER_REQUEST_CODE = 123;
 
	public BarcodeScanner(){
		
	}    
    
    @SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	container.removeAllViews();
        View rootView = inflater.inflate(R.layout.take_photo, container, false);
         
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(R.color.FireBrick));
		
        scan= (Button) rootView.findViewById(R.id.button);
         
        scan.setOnClickListener(new ButtonClickHandler());
        return rootView;
    }
    public class ButtonClickHandler implements View.OnClickListener 
    {
    	public void onClick( View view ){
    		Log.i("BarcodeScanner", "ButtonClickHandler.onClick()" );
	        // TODO Auto-generated method stub  
	    	Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	    	intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
	        startActivityForResult(intent, SCANNER_REQUEST_CODE);		
    	}
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

           if (requestCode == SCANNER_REQUEST_CODE) {
              if (resultCode == RESULT_OK) {
                  // Handle successful scan
                  String contents = intent.getStringExtra("SCAN_RESULT");
                  Log.i("BarcodeScanner","Scan results: " + contents);

/*                  String formatName = intent.getStringExtra("SCAN_RESULT_FORMAT");
                  byte[] rawBytes = intent.getByteArrayExtra("SCAN_RESULT_BYTES");
                  int intentOrientation = intent.getIntExtra("SCAN_RESULT_ORIENTATION", Integer.MIN_VALUE);
                  Integer orientation = (intentOrientation == Integer.MIN_VALUE) ? null : intentOrientation;
                  String errorCorrectionLevel = intent.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL");
              */
				try {
					barcodeInfo(contents);
				} catch (JSONException
						| URISyntaxException | IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                 // Handle successful scan
                                           
              } else if (resultCode == RESULT_CANCELED) {
                 // Handle cancel
                 Log.i("BarcodeScanner","Scan unsuccessful");
              }
         }
      }
    
	private void barcodeInfo(String UPC) throws JSONException, URISyntaxException, ClientProtocolException, IOException, ParseException{
	
	
	    InputStream inputStream = null;
	    String result = null;
	    try {
	    	String call = "http://www.upcdatabase.com/item/" + UPC;
	    	result = new CallServer().execute(call).get();
	
	    } catch (Exception e) { 
	        // Oops
	    }
	    finally {
	        try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
	    }
	    if (result != null){
		    System.out.println(result);
		    if (result.contains("Description")){
		    	int desc = result.indexOf("Description");
		    	int nameEnd = result.indexOf("</td>", desc + 29);
		    	String name = result.substring(desc + 29, nameEnd); 
		    	
	    		Fragment itemFrag = new TempItemAdd(); 
		        Bundle bundle = new Bundle();
		        bundle.putString("name", name);
		        itemFrag.setArguments(bundle);
		        
		        // consider using Java coding conventions (upper first char class names!!!)
		        FragmentTransaction transaction = getFragmentManager().beginTransaction();
		        // Replace whatever is in the fragment_container view with this fragment,
		        // and add the transaction to the back stack
		        transaction.replace(R.id.container, itemFrag);
		        transaction.addToBackStack(null);

		        // Commit the transaction
		        transaction.commit(); 
		    }else{
                Toast toast = Toast.makeText(this.getActivity(), "Item Not Found", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
		    }
	    }
	}
    public void onDestroy() {
        Log.i(TAG, "[ACTIVITY] onDestroy");
        super.onDestroy();

    }
    
    public void onResume(){
    	super.onResume();
    	Log.i(TAG, "onResume");
    	
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }     
}
