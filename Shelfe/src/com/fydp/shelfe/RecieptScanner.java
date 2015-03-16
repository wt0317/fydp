package com.fydp.shelfe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecieptScanner extends Fragment 
{
	protected Button _button;
	protected ImageView _image;
	protected TextView _field;
	protected String _path;
	protected String textResult;
	protected boolean _taken;
	
	protected static final String PHOTO_TAKEN	= "photo_taken";
	
	public RecieptScanner(){
		
	}
	
    @SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
    {
    	Log.i("PhotoCapture", "onCreateView" );
        //super.onCreate(savedInstanceState);
    	container.removeAllViews();
        View rootView = inflater.inflate(R.layout.take_photo, container, false);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(R.color.DarkOrchid));
		
        _image = ( ImageView ) rootView.findViewById( R.id.image );
        _field = ( TextView ) rootView.findViewById( R.id.field );
        _button = ( Button ) rootView.findViewById( R.id.button );
        _button.setOnClickListener( new ButtonClickHandler() );
        
        _path = Environment.getExternalStorageDirectory() + "/tessdata/make_machine_example.jpg";

        return rootView;
    }
    
    public class ButtonClickHandler implements View.OnClickListener 
    {
    	public void onClick( View view ){
    		Log.i("MakeMachine", "ButtonClickHandler.onClick()" );
    		startCameraActivity();
    	}
    }
    
    protected void startCameraActivity()
    {
    	Log.i("MakeMachine", "startCameraActivity()" );
        File file = new File( _path );
        File f = new File(Environment.getExternalStorageDirectory(),
                "tessdata");
        if (!f.exists()) {
            f.mkdirs();
        }
        Uri outputFileUri = Uri.fromFile( file );
        	
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
        intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
        	
        startActivityForResult( intent, 0 );
        
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {	
    	
    	Log.i( "MakeMachine", "resultCode: " + resultCode );
        switch( resultCode )
        {
        	case 0:
        		Log.i( "MakeMachine", "User cancelled" );
        		break;
        			
        	case -1:
			try {
				onPhotoTaken();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
    }
 
    protected void onPhotoTaken() throws IOException
    {
    	Log.i( "RecieptScanner", "onPhotoTaken" );
        _taken = true;
    	
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        	
        Bitmap bitmap = BitmapFactory.decodeFile( _path, options );
        _image.setImageBitmap(bitmap);
        	
        _field.setVisibility( View.GONE );
        
        //_path = path to the image to be OCRed
    		ExifInterface exif = new ExifInterface(_path);
    		int exifOrientation = exif.getAttributeInt(
    		        ExifInterface.TAG_ORIENTATION,
    		        ExifInterface.ORIENTATION_NORMAL);

    		int rotate = 0;

    		switch (exifOrientation) {
    		case ExifInterface.ORIENTATION_ROTATE_90:
    		    rotate = 90;
    		    break;
    		case ExifInterface.ORIENTATION_ROTATE_180:
    		    rotate = 180;
    		    break;
    		case ExifInterface.ORIENTATION_ROTATE_270:
    		    rotate = 270;
    		    break;
    		}

    		if (rotate != 0) {
    		    int w = bitmap.getWidth();
    		    int h = bitmap.getHeight();

    		    // Setting pre rotate
    		    Matrix mtx = new Matrix();
    		    mtx.preRotate(rotate);

    		    // Rotating Bitmap & convert to ARGB_8888, required by tess
    		    bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
    		}
    		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
    		
    		copyAssets();
    		
    		TessBaseAPI baseApi = new TessBaseAPI();
    		// DATA_PATH = Path to the storage	
    		// lang = for which the language data exists, usually "eng"
    		baseApi.init((Environment.getExternalStorageDirectory()).toString(), "eng");
    		// Eg. baseApi.init("/mnt/sdcard/tesseract/tessdata/eng.traineddata", "eng");
    		baseApi.setImage(bitmap);
    		String recognizedText = baseApi.getUTF8Text();
    		baseApi.end();
    		
    		itemSearch(recognizedText);
    }

    public void itemSearch(String text){
    	if (text.contains("SUBTOTAL")){
    		String items = text.split("SUBTOTAL")[0];

		    		System.out.println("text: " + items );
		    		Pattern p = Pattern.compile("(.+)([^$]\\d+(\\.|\\‘|\\,)\\d{2})");
		    		Matcher mr = p.matcher(items);
			    	while (mr.find()) {
			    		String name = mr.group(1);
			    		String price = mr.group(2);
			    		Fragment itemFrag = new TempItemAdd(); 
				        Bundle bundle = new Bundle();
				        if (price.contains(",")){
				        	price.replace(",", ".");
				        }else if (price.contains("‘")){
				        	price.replace("‘", ".");
				        }
				        bundle.putString("price", price );
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
			    	}
		    	
	    	}else{
	    		Toast.makeText(getView().getContext(), "Reciept not detected", Toast.LENGTH_LONG).show();
	    	}
    	
    }
    
	public int getCategory(String category){
	       
		if (category.contains("GRO")){
           	return 1;
		}else if (category.contains("DAI")){
			return 2;
		}else if (category.contains("PRO")){
			return 3;
		}else if (category.contains("BEE")){
			return 4;
		}else if (category.contains("BEV")){
			return 5;
		}else if (category.contains("DEL")){
			return 6;
		}else if (category.contains("HOM")){
			return 7;
		}	
		return 1;
	}
    protected void onRestoreInstanceState( Bundle savedInstanceState) throws IOException{
    	Log.i( "MakeMachine", "onRestoreInstanceState()");
    	if( savedInstanceState.getBoolean( RecieptScanner.PHOTO_TAKEN ) ) {
    		onPhotoTaken();
    	}
    }
    
    @Override
	public void onSaveInstanceState( Bundle outState ) {
    	outState.putBoolean( RecieptScanner.PHOTO_TAKEN, _taken );
    }
    private void copyAssets() {
        AssetManager assetManager = this.getActivity().getAssets();
        String[] files = null;
        String root = "tessdata";
        try {
            files = assetManager.list(root);
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }

        for(String filename : files) {
   
	            InputStream in = null;
	            OutputStream out = null;
	            try {
	              in = assetManager.open(root + "/" + filename);
	              File outFile = new File(Environment.getExternalStorageDirectory(), root + "/" + filename);
	              if(!outFile.exists()){
		              out = new FileOutputStream(outFile);
		              copyFile(in, out);
	             }
	            } catch(IOException e) {
	                Log.e("tag", "Failed to copy asset file: " + filename, e);
	            }     
	            finally {
	                if (in != null) {
	                    try {
	                        in.close();
	                    } catch (IOException e) {
	                        // NOOP
	                    }
	                }
	                if (out != null) {
	                    try {
	                        out.close();
	                    } catch (IOException e) {
	                        // NOOP
	                    }
	                }
	            }  
        	//}
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
          out.write(buffer, 0, read);
        }
    }       
}