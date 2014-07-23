package com.aghosn.craigsapp.location;

import java.util.Stack;

import com.aghosn.craigsapp.utils.DataParser;
import com.example.craigsapp.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class LocationPickerFragment extends Fragment{
	
	ListFragment primary;
	ListFragment states;
	Stack<ArrayAdapter<Object>> adapters = new Stack<>();
	String[] currentArray;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_location_picker, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		primary = (ListFragment) getFragmentManager().findFragmentById(R.id.primary_fragment);
		states = (ListFragment) getFragmentManager().findFragmentById(R.id.states_fragment);
		currentArray = getResources().getStringArray(R.array.primary_locations);
		listFragmentAdapter primaryAdapter = new listFragmentAdapter(getActivity(), android.R.layout.simple_list_item_1, currentArray, false);
		adapters.push(primaryAdapter);
		primary.setListAdapter(adapters.pop());
		primary.getListView().setOnItemClickListener(itemListener);
	}
	
	private class listFragmentAdapter extends ArrayAdapter<Object>{
		
		int resourceId;
		Object[] objects;
		ViewHolderItem viewHolder;
		boolean showCheck;

		public listFragmentAdapter(Context context, int resource, Object[] objects, boolean showCheck) {
			super(context, resource, objects);
			resourceId = resource;
			this.objects = objects;
			this.showCheck = showCheck;
		}
		
//		@Override
//		public int getCount() {
//			return objects.length;
//		}
//		
//		@Override
//		public Object getItem(int position) {
//			return objects[position];
//		}
//		
//		@Override
//		public long getItemId(int position) {
//			return super.getItemId(position);
//		}
//		
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			if(convertView==null){
//
//		    	LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//		        convertView = inflater.inflate(resourceId, parent, false);
//
//		        viewHolder = new ViewHolderItem();
//		        viewHolder.text = (TextView) convertView.findViewById(R.id.location_text);
//		        viewHolder.box = (CheckBox) convertView.findViewById(R.id.location_box);
//		        convertView.setTag(viewHolder);
//
//		    }else{
//		        viewHolder = (ViewHolderItem) convertView.getTag();
//		    }
//						
//			viewHolder.text.setText(objects[position].toString());
//			if(!showCheck){
//				viewHolder.box.setVisibility(View.GONE);
//			}
//			//iplemetn ceckbox listener
//			
//			return convertView;
//		}
		
	}
	
	private static class ViewHolderItem{
		TextView text;
		CheckBox box;
	}
	private OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			String location = currentArray[position];
			listFragmentAdapter adapter = new listFragmentAdapter(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(DataParser.parseArrays(location)), true);
			adapters.push(adapter);
			primary.setListAdapter(adapters.pop());
			currentArray = getResources().getStringArray(DataParser.parseArrays(location));
		}
	};
}
