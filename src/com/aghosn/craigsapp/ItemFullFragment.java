package com.aghosn.craigsapp;

import java.util.ArrayList;

import com.aghosn.craigsapp.utils.DataParser;
import com.example.craigsapp.R;
import com.xtremelabs.imageutils.ImageLoader;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemFullFragment extends Fragment{
	
	TextView title;
	TextView content;
	Gallery gallery;
	int width;
	private ImageLoader mImageLoader;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mImageLoader = ImageLoader.buildImageLoaderForFragment(this);
		return inflater.inflate(R.layout.item_full_view, container, false);
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		mImageLoader.destroy();
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		title = (TextView) getActivity().findViewById(R.id.item_full_title);
		content = (TextView) getActivity().findViewById(R.id.item_full_content);
		gallery = (Gallery) getActivity().findViewById(R.id.item_full_images);
		gallery.setSpacing(2);
		width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
		String link = getActivity().getIntent().getExtras().getString("link");
		new PageLoader().execute(link);
	}
	
	private class PageLoader extends AsyncTask<String, Integer, ListItem>{

		@Override
		protected ListItem doInBackground(String... params) {
			ListItem item = DataParser.parsePage(params[0]);
			return item;
		}
		
		@Override
		protected void onPostExecute(ListItem result) {
			super.onPostExecute(result);
			title.setText(result.getTitle());
			content.setText(result.getContent());
			Linkify.addLinks(content, Linkify.ALL);
			ArrayList<String> imageList = result.getImages();
			gallery.setAdapter(new ImageAdapter(getActivity(), imageList));
		}
	}
	
	public class ImageAdapter extends BaseAdapter {

        public ImageAdapter(Context c, ArrayList<String> imageList) {
            mContext = c;
            this.images = imageList;
        }

        public int getCount() {
        	return images.size();
        }

        public Object getItem(int position) {
            return images.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	ImageView i = new ImageView(mContext);
        	i.setLayoutParams(new Gallery.LayoutParams(width, 200));
        	mImageLoader.loadImage(i, images.get(position));
            return i;
        }

        private Context mContext;
        private ArrayList<String> images;
	
	}
//	private class ImageLoader extends AsyncTask<String, Integer, ArrayList<Drawable>>{
//		private ArrayList<Drawable> images = new ArrayList<>();
//		
//		@Override
//		protected ArrayList<Drawable> doInBackground(String... params) {
//			for(String param: params){
//				Drawable image = DataParser.parseContentImage(param);
//				images.add(image);
//			}
//			return images;
//		}
//		
//		@Override
//		protected void onPostExecute(ArrayList<Drawable> result) {
//			super.onPostExecute(result);
//			gallery.setAdapter(new ImageAdapter(getActivity(), result));
//		}
//		
//	}
}
