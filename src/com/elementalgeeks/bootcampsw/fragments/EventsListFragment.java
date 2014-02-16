package com.elementalgeeks.bootcampsw.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.elementalgeeks.bootcampsw.R;
import com.elementalgeeks.bootcampsw.data.Event;

public class EventsListFragment extends ListFragment {
	private SimpleAdapter adapter;
	private ProgressBar progressBar;
	private final static String DATE_KEY = "date";
	private final static String PLACE_KEY = "place";		
	private List<Map<String, String>> events = new ArrayList<Map<String, String>>();
	
	public void showEventsOnList() {
		Event e = new Event();
		e.setCity("Example city");
		e.setCountry("Example country");
		e.setDate(new Date());

		Map<String, String> event;
		event = new HashMap<String, String>(2);
		event.put(PLACE_KEY, e.getCity() + ", " + e.getCountry());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd 'de' MMM", getResources().getConfiguration().locale);            
		event.put(DATE_KEY, dateFormat.format(e.getDate()));
		events.add(event);
		
		progressBar.setVisibility(View.GONE);
		getListView().setVisibility(View.VISIBLE);		
		adapter.notifyDataSetChanged();		
	}
	
	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		adapter = new SimpleAdapter(inflater.getContext(), events,
                android.R.layout.simple_list_item_2, 
                new String[] {PLACE_KEY, DATE_KEY }, 
                new int[] {android.R.id.text1, android.R.id.text2 });
		
		setListAdapter(adapter);
 
		View view = inflater.inflate(R.layout.fragment_events_list, container, false);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		return view;
	}  
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		showEventsOnList();
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		super.onListItemClick(l, v, pos, id);
		@SuppressWarnings("unchecked")
		HashMap<String, String> event = (HashMap<String, String>) getListAdapter().getItem(pos);
		Toast.makeText(getActivity(), event.get(PLACE_KEY), Toast.LENGTH_SHORT).show();
	}	
}
