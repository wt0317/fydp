package com.fydp.shelfe;

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

public class CallServer extends AsyncTask<String, InputStream, InputStream> {
	

	
	protected InputStream doInBackground(String...params){
		InputStream instream = null;
		String call = params[0];
		
    	HttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
    	HttpGet request = new HttpGet();
    	
	    URI website = null;
		call.replaceAll("\\s","");
		try {
				website = new URI(call);
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
		    entity.consumeContent();
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

    }


}
