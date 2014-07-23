package com.aghosn.craigsapp;

import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.aghosn.craigsapp.utils.Tags;
import com.example.craigsapp.R;

public class CategoriesActivity extends Activity{

	HashMap<String, String[]> map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		WindowManager.LayoutParams windowManager = getWindow().getAttributes();
		windowManager.dimAmount = 0.82f;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		ExpandableListView list = (ExpandableListView) findViewById(R.id.expandableCategoriesList);
		prepareCategories(list);
	}
	
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_category, container,
					false);
			return rootView;
		}
	}
	
	private void prepareCategories(ExpandableListView list){
		map = new HashMap<>();
		String[] communityArray = getResources().getStringArray(R.array.community_category);
		String[] jobsArray = getResources().getStringArray(R.array.jobs_category);
		String[] housingArray = getResources().getStringArray(R.array.housing_category);
		map.put(Tags.COMMUNITY, communityArray);
		map.put(Tags.HOUSING, housingArray);
		map.put(Tags.JOBS, jobsArray);
		
		String[] array = new String[]{Tags.COMMUNITY, Tags.HOUSING, Tags.JOBS};
		
		CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(this, array, map);
		list.setAdapter(adapter);
		list.setOnChildClickListener(categoryListener);
	}
	private OnChildClickListener categoryListener = new OnChildClickListener() {
		
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			String cat = map.keySet().toArray()[groupPosition].toString();
			String categoryItem =  map.get(cat)[childPosition];
			getIntent().putExtra(Tags.CAT, categoryItem);
			setResult(RESULT_OK, getIntent());
			finish();
			return false;
		}
	};
}
