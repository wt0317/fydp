package com.fydp.shelfe;

import java.io.File;
import java.io.IOException;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoCaptureExample extends Fragment 
{
	protected Button _button;
	protected ImageView _image;
	protected TextView _field;
	protected String _path;
	protected boolean _taken;
	
	protected static final String PHOTO_TAKEN	= "photo_taken";
		
	
	public PhotoCaptureExample(){
		
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
    {
    	Log.i("PhotoCapture", "onCreateView" );
        //super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.take_photo, container, false);
       
        _image = ( ImageView ) rootView.findViewById( R.id.image );
        _field = ( TextView ) rootView.findViewById( R.id.field );
        _button = ( Button ) rootView.findViewById( R.id.button );
        _button.setOnClickListener( new ButtonClickHandler() );
        
        _path = Environment.getExternalStorageDirectory() + "/images/make_machine_example.jpg";
        
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
    			break;
    	}
    }
    
    protected void onPhotoTaken() throws IOException
    {
    	Log.i( "MakeMachine", "onPhotoTaken" );
    	
    	_taken = true;
    	
    	BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
    	
    	Bitmap bitmap = BitmapFactory.decodeFile( _path, options );
    	
    	_image.setImageBitmap(bitmap);
    	
    	_field.setVisibility( View.GONE );
    	
    	// _path = path to the image to be OCRed
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
    	
    	
    	TessBaseAPI baseApi = new TessBaseAPI();
    	// DATA_PATH = Path to the storage
    	// lang = for which the language data exists, usually "eng"
    	baseApi.init("/mnt/sdcard/tesseract/tessdata/eng.traineddata", "eng");
    	baseApi.setImage(bitmap);
    	String recognizedText = baseApi.getUTF8Text();
    	baseApi.end();
    	
    	Log.i( "RecText", recognizedText);
    }
    
    protected void onRestoreInstanceState( Bundle savedInstanceState) throws IOException{
    	Log.i( "MakeMachine", "onRestoreInstanceState()");
    	if( savedInstanceState.getBoolean( PhotoCaptureExample.PHOTO_TAKEN ) ) {
    		onPhotoTaken();
    	}
    }
    
    @Override
	public void onSaveInstanceState( Bundle outState ) {
    	outState.putBoolean( PhotoCaptureExample.PHOTO_TAKEN, _taken );
    }
}