package com.fydp.shelfe;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

public class CallServer extends AsyncTask<String, String, String> {
	

	
	protected String doInBackground(String...params){
		InputStream inputStream = null;
		String call = params[0];

	    	HttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
	    	HttpGet request = new HttpGet();

		    URI website = null;
			
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
			String str = null;
			try {
				str = EntityUtils.toString(response.getEntity());
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	           
		    //HttpEntity entity = response.getEntity();
		
//		    try {
//		    	//Log.i("CallServer", "before");
//				//inputStream = entity.getContent();
//				//Log.i("CallServer", "after");
//			    //entity.consumeContent();
//			} catch (IllegalStateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		    
		
        return str;
    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(InputStream inputStream) {
        //showDialog("Downloaded " + result + " bytes");

    }


}
