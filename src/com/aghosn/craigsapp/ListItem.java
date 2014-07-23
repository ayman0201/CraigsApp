package com.aghosn.craigsapp;

import java.util.ArrayList;

public class ListItem {

	private String title;
	private String content;
	private String timeStamp;
	private ArrayList<String> images;
	private String location;
	private String link;
	private String description;
	private String numeric;
	private String mapLocation;
	
	public ListItem(){
		images = new ArrayList<String>();
	}
	
	public String getMapLocation() {
		return mapLocation;
	}

	public void setMapLocation(String mapLocation) {
		this.mapLocation = mapLocation;
	}

	public String getNumeric() {
		return numeric;
	}

	public void setNumeric(String numeric) {
		this.numeric = numeric;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLink() {
		return link;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public ArrayList<String> getImages() {
		return images;
	}
	public void addImage(String image) {
		images.add(image);
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	
}
