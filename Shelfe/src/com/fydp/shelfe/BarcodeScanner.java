package com.fydp.shelfe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BarcodeScanner extends Fragment {
 
	private static final String TAG = "com.fydp.shelfe.BarcodeScanner";
    private Button scan;
    int RESULT_OK = 0;
    int RESULT_CANCELED = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	container.removeAllViews();
        View rootView = inflater.inflate(R.layout.take_photo, container, false);
         
        scan= (Button) rootView.findViewById(R.id.button);
         
        scan.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub  
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            }
        });
        return rootView;
    }
 
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
           if (requestCode == 0) {
              if (resultCode == RESULT_OK) {
                  
                 String contents = intent.getStringExtra("SCAN_RESULT");
                 String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
              
                 // Handle successful scan
                                           
              } else if (resultCode == RESULT_CANCELED) {
                 // Handle cancel
                 Log.i("App","Scan unsuccessful");
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

     
}
