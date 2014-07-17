package com.example.shelf_e;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.os.AsyncTask;

public class CallServer extends AsyncTask<InputStream, InputStream, InputStream> {
	
	InputStream instream;
	
	protected InputStream doInBackground(InputStream...params){
		this.instream = params[0];
		
    	HttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
    	HttpGet request = new HttpGet();
    	
	    URI website = null;
		try {
			website = new URI("http://shelfe.netau.net/service/Service.php?method=getInventory&username=test&password=test");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    request.setURI(website);
        HttpResponse response = null;
		try {
			response = httpclient.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	    InputStream inputStream = null;
           
	    HttpEntity entity = response.getEntity();
	
	    try {
			inputStream = entity.getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
        return inputStream;
    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(InputStream inputStream) {
        //showDialog("Downloaded " + result + " bytes");
    	instream = inputStream;
    }


}
