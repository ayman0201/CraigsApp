package com.aghosn.craigsapp;

import java.util.HashMap;

import com.aghosn.craigsapp.location.LocationPickerActivity;
import com.aghosn.craigsapp.utils.DataParser;
import com.aghosn.craigsapp.utils.Tags;
import com.example.craigsapp.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int CATEGORY_REQUEST_CODE = 1;
	String categorySelected ;
	Button categoriesBtn;
	Button searchBtn;
	Button pickLoc;
	SharedPreferences prefs;

	CheckBox bronxCB;
	CheckBox brookCB;
	CheckBox manhCB;
	CheckBox statenCB;
	CheckBox queensCB;
	
	String locations = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
		prefs = this.getSharedPreferences(
				"com.aghosn.craigsapp", Context.MODE_PRIVATE);
		toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.no_location_set), Toast.LENGTH_SHORT);
		LoadLocationsTask task = new LoadLocationsTask();
		task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		searchBtn = (Button) findViewById(R.id.search_btn);
		searchBtn.setOnClickListener(searchListener);
		pickLoc = (Button) findViewById(R.id.pick_location_btn);
		pickLoc.setOnClickListener(picklocListener);
		categoriesBtn = (Button) findViewById(R.id.categories_btn);
		categoriesBtn.setOnClickListener(openCategoriesListener);
		categoriesBtn.setText(prefs.getString(Tags.CAT, "For Sale"));
		CheckBox nycCB = (CheckBox) findViewById(R.id.cbAll);
		bronxCB = (CheckBox) findViewById(R.id.cbBronx);
		brookCB = (CheckBox) findViewById(R.id.cbBrooklyn );
		manhCB = (CheckBox) findViewById(R.id.cbManh);
		queensCB = (CheckBox) findViewById(R.id.cbQueens);
		statenCB = (CheckBox) findViewById(R.id.cbStaten);
		OnClickListener nycCBListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(((CheckBox)v).isChecked()){
					bronxCB.setChecked(true);
					brookCB.setChecked(true);
					manhCB.setChecked(true);
					queensCB.setChecked(true);
					statenCB.setChecked(true);
					bronxCB.setChecked(true);
				}else{
					bronxCB.setChecked(false);
					brookCB.setChecked(false);
					manhCB.setChecked(false);
					queensCB.setChecked(false);
					statenCB.setChecked(false);
					bronxCB.setChecked(false);
				}
			}
		};
		nycCB.setOnClickListener(nycCBListener );
	}

	private OnClickListener openCategoriesListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainActivity.this, CategoriesActivity.class);
			startActivityForResult(i, CATEGORY_REQUEST_CODE);			
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CATEGORY_REQUEST_CODE){
			if(resultCode == RESULT_OK){
				categorySelected = data.getStringExtra(Tags.CAT);
				categoriesBtn.setText(categorySelected);
				prefs.edit().putString(Tags.CAT, categorySelected).apply();
			}
		}
	};
	Toast toast;
	private OnClickListener searchListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if(atLeastOne()){
				Intent i = new Intent(MainActivity.this, ResultsActivity.class);
				startActivity(i);
			}
			else{
				if(!toast.getView().isShown()){
					toast.show();
				}
			}
		}
	};

	private boolean atLeastOne(){
		return (queensCB.isChecked() || manhCB.isChecked() 
				|| bronxCB.isChecked() || brookCB.isChecked() 
				|| statenCB.isChecked());
	}
	
	private class LoadLocationsTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			return DataParser.parseLocations();
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			DataParser.writeToFile(result, MainActivity.this);
		}
	}
	private OnClickListener picklocListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainActivity.this, LocationPickerActivity.class);
			startActivity(i);
		}
	};
}
