package com.elementalgeeks.bootcampsw.activities;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.elementalgeeks.bootcampsw.R;
import com.elementalgeeks.bootcampsw.data.Event;
import com.elementalgeeks.bootcampsw.fragments.AboutFragment;
import com.elementalgeeks.bootcampsw.fragments.EventsListFragment;
import com.elementalgeeks.bootcampsw.fragments.EventsMapFragment;

public class MainActivity extends ActionBarActivity implements TabListener {
	private String TAG;
	private Fragment[] fragments = new Fragment[]{ new EventsListFragment(), 
			   new EventsMapFragment(),
	           new AboutFragment()};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TAG = getPackageName();
		
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		actionBar.addTab(
				actionBar.newTab()
					     .setText(getString(R.string.title_list))
					     .setTabListener(this));
		
		actionBar.addTab(
				actionBar.newTab()
					     .setText(getString(R.string.title_map))
					     .setTabListener(this));
		
		actionBar.addTab(
				actionBar.newTab()
					     .setText(getString(R.string.title_about))
					     .setTabListener(this));	
		
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		for (Fragment frag : fragments) {
			transaction.add(R.id.main, frag).hide(frag);
		}
		transaction.show(fragments[0]).commit();
		apiRequest();
	}

	@Override
	public void onTabReselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(Tab tab,
			android.support.v4.app.FragmentTransaction transaction) {
		for (Fragment frag : fragments) {
			transaction.hide(frag);
		}
		transaction.show(fragments[tab.getPosition()]);	
	}

	@Override
	public void onTabUnselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
	}
	
	public void apiRequest(){
	    Response.Listener<JSONArray> successListener = 
	    		new Response.Listener<JSONArray>() {
		            @Override
		            public void onResponse(JSONArray response) {
		            	ArrayList<Event> events = new ArrayList<Event>();
		            	for (int i = 0; i < response.length(); i++) {
		            		JSONObject JSONevent;
							try {
								JSONevent = response.getJSONObject(i);
								String website = "startupweekend.org";
								try {
									website = JSONevent.getString("website");
								} catch (JSONException ex) {
									Log.e(TAG,Log.getStackTraceString(ex));
								}

			            		Event e = new Event();
			            		e.setId(JSONevent.getString("id"));
			            		e.setCity(JSONevent.getString("city"));
			            		e.setCountry(JSONevent.getString("country"));				            		
			            		e.setWebsite(website);
			            		e.setLat(JSONevent.getJSONObject("location").getDouble("lat"));
			            		e.setLng(JSONevent.getJSONObject("location").getDouble("lng"));
			            		
			            		//http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html#timezone
			            		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
			            	            									    getResources().getConfiguration().locale);
			            		try {  
			            		    Date date = format.parse(JSONevent.getString("start_date"));  
			            		    e.setDate(date);  
			            		} catch (ParseException pe) {
			            			Log.e(TAG,Log.getStackTraceString(pe));
								}
			            		events.add(e);
			            		Log.i(TAG, "Event added " + e.toString());
							} catch (JSONException ex) {
								Log.e(TAG,Log.getStackTraceString(ex));
							}
		            	}
		            	

		            }
	    };

	    Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", getResources().getConfiguration().locale);
	    String sinceDate = formatter.format(calendar.getTime());	    
	    calendar.add(Calendar.DATE, 15);
	    String untilDate = formatter.format(calendar.getTime());
	    	    
	    String URL = "http://swoop.startupweekend.org/events?since=" + sinceDate + "&until=" + untilDate;
		JsonArrayRequest request = new JsonArrayRequest(URL, successListener, null);
		
		RequestQueue requestQueue  = Volley.newRequestQueue(this);
		requestQueue.add(request);			
	}	
}