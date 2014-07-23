package com.aghosn.craigsapp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.aghosn.craigsapp.utils.DataParser;
import com.example.craigsapp.R;
import com.xtremelabs.imageutils.ImageLoader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class ResultsActivity extends FragmentActivity{
	private ImageLoader mImageLoader;

	ArrayList<ListItem> listItems;
	ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		mImageLoader = ImageLoader.buildImageLoaderForActivity(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mImageLoader.destroy();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            onBackPressed();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	public class PlaceholderFragment extends ListFragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_results, container,
					false);
			return rootView;
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			list = (ListView) getActivity().findViewById(android.R.id.list);
			RSSTask task = new RSSTask();
			task.execute("http://newyork.craigslist.org/nfb/index.rss");
		}
	}
	private OnItemClickListener itemClickedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent i = new Intent(ResultsActivity.this, ItemFullActivity.class);
			String link = listItems.get(position).getLink();
			i.putExtra("link", link);
			startActivity(i);
//			Intent i = new Intent(ResultsActivity.this, WebActivity.class);
//			String link = listItems.get(position).getLink();
//			i.putExtra("url", link);
//			startActivity(i);
		}
	};
	
	private class RSSTask extends AsyncTask<String, Integer, ArrayList<ListItem>>{

		@Override
		protected ArrayList<ListItem> doInBackground(String... params) {
			ArrayList<ListItem> items = null;
			try {
				list.getEmptyView().setVisibility(View.GONE);
				items = DataParser.parseRss(new URL(params[0]));
			} catch (MalformedURLException e) {
				System.err.println(e.getMessage());
			}
			return items;
		}
		
		@Override
		protected void onPostExecute(ArrayList<ListItem> result) {
			super.onPostExecute(result);
			listItems = result;
			CustomListAdapter adapter = new CustomListAdapter(ResultsActivity.this, R.layout.item_view, result, mImageLoader);
			list.setAdapter(adapter);
			list.setOnItemClickListener(itemClickedListener);
			list.setVisibility(View.VISIBLE);
		}
	}
}
