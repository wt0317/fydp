package com.fydp.shelfe;

import com.fydp.shelfe.RecieptScanner.ButtonClickHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
    int RESULT_OK = 0;
    int RESULT_CANCELED = 1;
    public int SCANNER_REQUEST_CODE = 123;
 
	public BarcodeScanner(){
		
	}    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	container.removeAllViews();
        View rootView = inflater.inflate(R.layout.take_photo, container, false);
         
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
	    	intent.putExtra("SCAN_MODE", "SCAN_MODE");
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
                  Toast toast = Toast.makeText(this.getActivity(), "Content: " + contents, Toast.LENGTH_LONG);
                  toast.setGravity(Gravity.TOP, 25, 400);
                  toast.show();
                  String formatName = intent.getStringExtra("SCAN_RESULT_FORMAT");
                  byte[] rawBytes = intent.getByteArrayExtra("SCAN_RESULT_BYTES");
                  int intentOrientation = intent.getIntExtra("SCAN_RESULT_ORIENTATION", Integer.MIN_VALUE);
                  Integer orientation = (intentOrientation == Integer.MIN_VALUE) ? null : intentOrientation;
                  String errorCorrectionLevel = intent.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL");
              
                 // Handle successful scan
                                           
              } else if (resultCode == RESULT_CANCELED) {
                 // Handle cancel
                 Log.i("BarcodeScanner","Scan unsuccessful");
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
