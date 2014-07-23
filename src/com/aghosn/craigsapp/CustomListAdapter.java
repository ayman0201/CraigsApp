package com.aghosn.craigsapp;

import java.util.ArrayList;

import com.example.craigsapp.R;
import com.xtremelabs.imageutils.ImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<Object> {

	private ArrayList<ListItem> items;
	private Context context;
	private int resourceId;
	private ImageLoader imageLoader;
	
	public CustomListAdapter(Context ctx, int resourceId, ArrayList<ListItem> items, ImageLoader mImageLoader) {
		super(ctx, resourceId);
		this.items = items;
		this.context = ctx;
		this.resourceId = resourceId;
		this.imageLoader = mImageLoader;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolderItem viewHolder;
		    if(convertView==null){

		    	LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		        convertView = inflater.inflate(resourceId, parent, false);

		        viewHolder = new ViewHolderItem();
		        viewHolder.title = (TextView) convertView.findViewById(R.id.item_title);
		        viewHolder.description = (TextView) convertView.findViewById(R.id.item_description);
		        viewHolder.location = (TextView) convertView.findViewById(R.id.item_loc);
		        viewHolder.date = (TextView) convertView.findViewById(R.id.item_date);
		        viewHolder.image = (ImageView) convertView.findViewById(R.id.item_image);
		        convertView.setTag(viewHolder);

		    }else{
		        viewHolder = (ViewHolderItem) convertView.getTag();
		    }
		    ListItem item = items.get(position);
		    if(item != null) {
		    	viewHolder.title.setText(item.getTitle());
		    	viewHolder.description.setText(item.getDescription());
		    	viewHolder.location.setText(item.getLocation());
		    	viewHolder.date.setText(item.getTimeStamp());
		    	viewHolder.image.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_menu_camera));
		    	if(item.getImages() != null && !item.getImages().isEmpty()){
		    		imageLoader.loadImage(viewHolder.image, item.getImages().get(0));
		    	}
		    }
		    return convertView;
	}
	
	private static class ViewHolderItem{
		TextView title;
		TextView description;
		TextView date;
		TextView location;
		ImageView image;
	}
	
//	private class ImageLoader extends AsyncTask<String, Integer, Drawable>{
//		ImageView imageView;
//		
//		@Override
//		protected Drawable doInBackground(String... params) {
//			Drawable img = DataParser.parseContentImage(params[0]);
//			return img;
//		}
//		
//		public void setImageView(ImageView imageView) {
//			this.imageView = imageView;
//		}
//
//		@Override
//		protected void onPostExecute(Drawable result) {
//			super.onPostExecute(result);
//			imageView.setImageDrawable(result);
//		}
//	}
}