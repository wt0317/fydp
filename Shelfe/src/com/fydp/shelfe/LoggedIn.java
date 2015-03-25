package com.fydp.shelfe;


import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoggedIn extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	public static final int MENU_SETTINGS = 8;
	private SharedPreferences mSettings;
	private Preferences mPreferences;
	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	static RecieptScanner rcFragment;
	static TempItemAdd pcFragment;
	static Inventory inFragment;
	static BarcodeScanner bsFragment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(R.color.notclicked));
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		case 5:
			mTitle = getString(R.string.title_section5);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			menu.add(0, MENU_SETTINGS, 0, R.string.action_settings)
	        .setShortcut('8', 's')
	        .setIntent(new Intent(this, Settings.class));
			menu.add(0, 9, 0, R.string.graphical_view)
	        .setShortcut('9', 's')
	        .setIntent(new Intent(this, GraphicalView.class));
			if (mPreferences.getAdminMode()){
				menu.add(0, 6, 0, R.string.hard_reset)
				.setShortcut('6', 'h');
			}
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			
			
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}		
		if (id == 9) {
			Intent myIntent = new Intent(LoggedIn.this, GraphicalView.class);
			Bundle bundle = getIntent().getExtras();
			myIntent.putExtras(bundle);
			/*finish();*/
			startActivity(myIntent);
			return true;
		}
		if (id == 6){
	        InputStream inputStream = null;
		    try {
		    	String call = "http://shelfe.host22.com/service/Service.php?method=adminClear" +
		    			"&username=admin" +
		    			"&password=admin";
		    	new CallServer().execute(call).get();

		    } catch (Exception e) { 
		        // Oops
		    }
		    finally {
		        try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
		    }
		    
			try {
				Inventory inventory = new Inventory();
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.container, inventory).commit();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return false;
		}
		if (id == R.id.action_refresh) {
			try {
				Inventory inventory = new Inventory();
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.container, inventory).commit();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState){
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
			
			
	           switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
	            case 1:
	            	if (inFragment == null) {
	            		try {
							inFragment = new Inventory();
						} catch (JSONException | URISyntaxException | IOException | ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            	}
               	getFragmentManager().beginTransaction()
           		.add(R.id.container, inFragment)
           		.commit();
	                
	                	            	
	            	break;
	            case 2:	
	            	
               	if (rcFragment == null) {
            		rcFragment = new RecieptScanner();
            	}	
           	getFragmentManager().beginTransaction()
       		.add(R.id.container, rcFragment)
       		.commit();
	                break;
	                
	           case 3:	

               	if (bsFragment == null) {
            		bsFragment = new BarcodeScanner();
               	}	
            if (!bsFragment.isAdded()){
           	getFragmentManager().beginTransaction()
       		.add(R.id.container, bsFragment)
       		.commit();
            }
               	
               	break;
	           case 4:
	            	if (pcFragment == null) {
           		pcFragment = new TempItemAdd();
           	}	
          	getFragmentManager().beginTransaction()
      		.add(R.id.container, pcFragment)
      		.commit();
	                break;
	
	           }
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((LoggedIn) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	@Override
	public void onResume(){
		super.onResume();
        
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferences = new Preferences(mSettings);
	}
}
