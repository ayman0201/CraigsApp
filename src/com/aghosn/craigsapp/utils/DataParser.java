package com.aghosn.craigsapp.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.aghosn.craigsapp.ListItem;
import com.example.craigsapp.R;

import android.content.Context;
import android.os.Environment;
import android.text.Html;
import android.util.Log;

public class DataParser {

	static ArrayList<ListItem> listItems = new ArrayList<>();

	private static String[] parseNumOfTitle(String title){
		String num = "";
		String[] combo = new String[2];
		String tit = Html.fromHtml(title).toString();
		tit = tit.trim();
		int end = 0;
		if(tit.charAt(0) == '$'){
			boolean numeric = false;
			for(int i = 1; i < tit.length(); i++){
				String character = tit.charAt(i)+"";
				if(isNumeric(character)){
					end = i;
					num.concat(character);
					numeric = true;
				}
				else{
					numeric = false;
				}
				if(!numeric){
					break;
				}
			}
			if(!num.isEmpty()){
				String titl = tit.substring(end + 1, tit.length());
				titl = titl.trim();
				if(titl.charAt(0) == '/' || titl.charAt(0) == '\\' || titl.charAt(0) == '-'){
					String str = titl.substring(1, titl.length());
					combo[0] = str.trim();
				}
				else{
					combo[0] = titl;
				}
				combo[1] = num;
			}
			else{
				combo[0] = title;
				combo[1] = "";
			}
		}
		return combo;
	}

	private static boolean isNumeric(String str)  
	{  
		try  
		{  
			double d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;  
	}

	private static String getTimeOfPosting(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ssZ");
		String result = null;
		try {
			Date mDate = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			Date currentDate = calendar.getTime();

			DateTime startTime, endTime;
			startTime = new DateTime(mDate);
			endTime = new DateTime(currentDate);
			Period p = new Period(startTime, endTime);
			long diffHours = p.getHours();
			long diffMinutes = p.getMinutes();
			long diffSeconds = p.getSeconds();
			long diffDays = p.getDays();

			if (diffDays > 1) {
				result = diffDays+" days ago";
			} else if (diffHours >= 24 && diffHours < 48) {
				result = "1 day ago";
			} else if (diffHours < 24 && diffHours > 1) {
				result = diffHours+" hours ago";
			}else if(diffHours == 1){
				result = "1 hour ago";
			}else if(diffMinutes < 60 && diffMinutes > 1){
				result = diffMinutes+" minutes ago";
			}else if(diffMinutes == 1 || diffSeconds <= 60){
				result = "1 minute ago";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String[] parseLocation(String title){
		String[] titleAndLoc = new String[2];
		int end = title.lastIndexOf(')');
		if(end == -1){
			titleAndLoc[0] = title;
			titleAndLoc[1] = "";
			return titleAndLoc;
		}
		int start = 0;
		for(int i = end - 1; i > 0; i--){
			if(title.charAt(i) == '('){
				start = i;
				break;
			}
		}
		titleAndLoc[1] = title.substring(start+1, end);
		titleAndLoc[0] = title.substring(0, start - 1);
		return titleAndLoc;
	}

	//	public static Drawable parseContentImage(String content){
	//		Drawable img = getImageFromUrl(content);
	//		return img;
	//	}

	//	private static Drawable getImageFromUrl(String urlString) {
	//		try {
	//			Bitmap img;
	//			URL url = new URL(urlString);
	//			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	//			conn.setConnectTimeout(30000);
	//			conn.setReadTimeout(30000);
	//			conn.setInstanceFollowRedirects(true);
	//			InputStream is=conn.getInputStream();
	//			img = BitmapFactory.decodeStream(is);
	//			is.close();
	//			Drawable d = new BitmapDrawable(img);
	//			return d;
	//		} catch (Exception e) {
	//			return null;
	//		}
	//	}

	public static ArrayList<ListItem> parseRss(URL url){
		ListItem item = null;
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(url.openConnection().getInputStream(),null);

			String currentText = null;
			String currentTag = null;
			boolean insideItem = false;
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					currentTag = xpp.getName();
					if(currentTag.equalsIgnoreCase("enclosure")){
						int count = xpp.getAttributeCount();
						for(int i = 0; i < count ; i++){
							if(xpp.getAttributeName(i).equalsIgnoreCase("resource")){
								item.addImage(xpp.getAttributeValue(i));
								break;
							}
						}
					}
				} else if (eventType == XmlPullParser.TEXT) {
					currentText = xpp.getText();
					if (currentTag.equalsIgnoreCase("item")) {
						insideItem = true;
						item = new ListItem();
					}
					else if(insideItem){
						if (currentTag.equalsIgnoreCase("title")) {
							String title = Html.fromHtml(xpp.getText()).toString();
							if(!title.equalsIgnoreCase("\n") && !title.isEmpty()){
								String [] combo = parseLocation(title);
								if(combo == null){
									item.setTitle(title.trim());
								}
								else{
									item.setTitle(combo[0].trim());
									item.setLocation(combo[1].trim());
								}
							}
						}
						else if(currentTag.equalsIgnoreCase("link")){
							if(!currentText.equalsIgnoreCase("\n")){
								item.setLink(currentText);
							}
						}
						else if(currentTag.equalsIgnoreCase("description")){
							if(!currentText.equalsIgnoreCase("\n")){
								item.setDescription(Html.fromHtml(currentText).toString().trim());
							}
						}
						else if(currentTag.equalsIgnoreCase("date")){
							if(!currentText.equalsIgnoreCase("\n")){
								item.setTimeStamp(getTimeOfPosting(currentText));
							}
						}
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					if (xpp.getName().equalsIgnoreCase("item")) {
						insideItem = false;
						listItems.add(item);
					}
				}
				eventType = xpp.next();
			}
		}
		catch (Exception e) {
			System.err.println(e);
		}
		return listItems;
	}

	public static ListItem parsePage(String url){
		ListItem item = new ListItem();

		try {
			Document doc = Jsoup.connect(url).get();

			String title = doc.select("h2[class=postingtitle]").text();
			String[] combo = parseNumOfTitle(title);
			item.setTitle(combo[0]);
			item.setNumeric(combo[1]);

			Elements links = doc.select("figure[class=iw]").select("div[id=thumbs]").select("a[href]");
			for(Element el: links){
				String link = el.attr("href");
				if(isImage(link)){
					item.addImage(link);
				}
			}

			links = doc.select("figure[class=iw]").select("div[id=ci]").select("image[id=iwi]");
			for(Element el: links){
				String link = el.attr("src");
				if(isImage(link)){
					item.addImage(link);
				}
			}

			links = doc.select("img[src]");
			for(Element el: links){
				String link = el.attr("src");
				if(isImage(link)){
					item.addImage(link);
				}			
			}

			links = doc.select("section[id=postingbody]").select("img[src]");
			for(Element el: links){
				String link = el.attr("src");
				if(isImage(link)){
					item.addImage(link);
				}
			}

			Elements map = doc.select("section[class=userbody]").select("div[class=mapAndAttrs]").select("a[href]");
			for(Element el: map){
				String link = el.attr("href");
				if(link.contains("google")){
					item.setMapLocation(link);
				}
			}

			String content = doc.select("section[id=postingbody]").text();
			item.setContent(Html.fromHtml(content).toString().trim());


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return item;
	}

	private static boolean isImage(String url){
		if(url.endsWith(".jpeg") || url.endsWith(".jpg") || url.endsWith(".png")
				|| url.endsWith(".gif") || url.endsWith(".JPEG") || url.endsWith(".JPG")
				|| url.endsWith(".PNG") || url.endsWith(".GIF")){
			return true;
		}
		return false;
	}



	public static String parseLocations(){
		String data = "";
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(Environment.getExternalStorageDirectory()+"/Array/locations.xml");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(fis,null);

			String currentText = null;
			String currentTag = null;
			String arrayName = "";
			String arrayItem = "";


			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_TAG){
					currentTag = xpp.getName();
					if(currentTag.equalsIgnoreCase("a")){
						int count = xpp.getAttributeCount();
						for(int i = 0; i < count ; i++){
							if(xpp.getAttributeName(i).equalsIgnoreCase("href")){
								arrayItem = arrayItem.concat("<item>"+extractItem(xpp.getAttributeValue(i))+"</item>\n");
								break;
							}
						}
					}
				}
				else if (eventType == XmlPullParser.TEXT) {
					currentText = xpp.getText();
					if(currentText.contains("\n")){
						eventType = xpp.next();
						continue;
					}
					if (currentTag.equalsIgnoreCase("h4")) {
						arrayName = ("<string-array name=\""+currentText.replace(" ", "_")+"\">\n");
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					if (xpp.getName().equalsIgnoreCase("ul")) {
						data = data.concat(arrayName+arrayItem+"</string-array>\n");
						arrayName = "";
						arrayItem = "";
					}
				}
				eventType = xpp.next();
			}
		}
		catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	
	private static String extractItem(String str) {
		String result = "";
		for(int i = 7; i < str.length(); i++){
			if(str.charAt(i) != '.'){
				result = result.concat(str.charAt(i)+"");
			}
			else{
				return result;
			}
		}
		return result;
	}

	public static String parseUrl(){
		String data = "";
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(Environment.getExternalStorageDirectory()+"/Array/locations.xml");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(fis,null);

			String currentText = null;
			String currentTag = null;
			String link = "";
			String nameVar = "";


			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_TAG){
					currentTag = xpp.getName();
					if(currentTag.equalsIgnoreCase("a")){
						int count = xpp.getAttributeCount();
						for(int i = 0; i < count ; i++){
							if(xpp.getAttributeName(i).equalsIgnoreCase("href")){
								link = xpp.getAttributeValue(i);
								nameVar = extractItem(link);
								break;
							}
						}
					}
				}
				else if (eventType == XmlPullParser.TEXT) {
					currentText = xpp.getText();
					if(currentText.contains("\n")){
						eventType = xpp.next();
						continue;
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					if (xpp.getName().equalsIgnoreCase("a")) {
						data =data.concat("<string name=\""+nameVar+"\">"+link+"</string>\n");
						nameVar = "";
						link = "";
					}
				}
				eventType = xpp.next();
			}
		}
		catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	
	public static String parseNames(){
		String data = "";
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(Environment.getExternalStorageDirectory()+"/Array/locations.xml");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(fis,null);

			String currentText = null;
			String currentTag = null;
			String nameVar = "";
			String value = "";

			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_TAG){
					
					currentTag = xpp.getName();
					if(currentTag.equalsIgnoreCase("a")){
						int count = xpp.getAttributeCount();
						for(int i = 0; i < count ; i++){
							if(xpp.getAttributeName(i).equalsIgnoreCase("href")){
								String link = xpp.getAttributeValue(i);
								nameVar = extractItem(link);
								break;
							}
						}
					}
				}
				else if (eventType == XmlPullParser.TEXT) {
					currentText = xpp.getText();
					if(currentText.contains("\n")){
						eventType = xpp.next();
						continue;
					}
					else if(currentTag.equalsIgnoreCase("a")){
						value = currentText;
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					if (xpp.getName().equalsIgnoreCase("a")) {
						data =data.concat("<string name=\""+nameVar+"_string\">"+value+"</string>\n");
						nameVar = "";
						value = "";
					}
				}
				eventType = xpp.next();
			}
		}
		catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}
	
	
	public static void writeToFile(String data, Context ctx) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ctx.openFileOutput("newlocs.txt", Context.MODE_PRIVATE));
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}
	
	public static int parseArrays(String array){
		switch (array) {
		case "Argentina":
			return R.array.Argentina;
		case "Australia":
			return R.array.Australia;
		case "Austria":
			return R.array.Austria;
		case "Bangladesh":
			return R.array.Argentina;
		case "Belgium":
			return R.array.Belgium;
		case "Bolivia":
			return R.array.Bolivia;
		case "Brazil":
			return R.array.Brazil;
		case "Bulgaria":
			return R.array.Bulgaria;
		case "Caribbean Islands":
			return R.array.Caribbean_Islands;
		case "Chile":
			return R.array.Chile;
		case "China":
			return R.array.China;
		case "Colombia":
			return R.array.Colombia;
		case "Costa Rica":
			return R.array.Costa_Rica;
		case "Croatia":
			return R.array.Croatia;
		case "Czech Republic":
			return R.array.Czech_Republic;
		case "Denmark":
			return R.array.Denmark;
		case "Dominican Republic":
			return R.array.Dominican_Republic;
		case "Ecuador":
			return R.array.Ecuador;
		case "Egypt":
			return R.array.Egypt;
		case "El Salvador":
			return R.array.El_Salvador;
		case "Ethiopia":
			return R.array.Ethiopia;
		case "Finland":
			return R.array.Finland;
		case "France":
			return R.array.France;
		case "Germany":
			return R.array.Germany;
		case "Ghana":
			return R.array.Ghana;
		case "Greece":
			return R.array.Greece;
		case "Guam / Micronesia":
			return R.array.Guam_Micronesia;
		case "Guatemala":
			return R.array.Guatemala;
		case "Hong Kong":
			return R.array.Hong_Kong;
		case "Hungary":
			return R.array.Hungary;
		case "Iceland":
			return R.array.Iceland;
		case "India":
			return R.array.India;
		case "Indonesia":
			return R.array.Indonesia;
		case "Iran":
			return R.array.Iran;
		case "Iraq":
			return R.array.Iraq;
		case "Ireland":
			return R.array.Ireland;
		case "Israel and Palestine":
			return R.array.Israel_and_Palestine;
		case "Italy":
			return R.array.Italy;
		case "Japan":
			return R.array.Japan;
		case "Kenya":
			return R.array.Kenya;
		case "Korea":
			return R.array.Korea;
		case "Kuwait":
			return R.array.Kuwait;
		case "Lebanon":
			return R.array.Lebanon;
		case "Luxembourg":
			return R.array.Luxembourg;
		case "Malaysia":
			return R.array.Malaysia;
		case "Mexico":
			return R.array.Mexico;
		case "Morocco":
			return R.array.Morocco;
		case "Netherlands":
			return R.array.Netherlands;
		case "New Zealand":
			return R.array.New_Zealand;
		case "Nicaragua":
			return R.array.Nicaragua;
		case "Norway":
			return R.array.Norway;
		case "Pakistan":
			return R.array.Pakistan;
		case "Panama":
			return R.array.Panama;
		case "Peru":
			return R.array.Peru;
		case "Puerto Rico":
			return R.array.Puerto_Rico;
		case "Philippines":
			return R.array.Philippines;
		case "Poland":
			return R.array.Poland;
		case "Portugal":
			return R.array.Portugal;
		case "Romania":
			return R.array.Romania;
		case "Russian Federation":
			return R.array.Russian_Federation;
		case "Singapore":
			return R.array.Singapore;
		case "South Africa":
			return R.array.South_Africa;
		case "Spain":
			return R.array.Spain;
		case "Sweden":
			return R.array.Sweden;
		case "Switzerland":
			return R.array.Switzerland;
		case "Taiwan":
			return R.array.Taiwan;
		case "Thailand":
			return R.array.Thailand;
		case "Tunisia":
			return R.array.Tunisia;
		case "Turkey":
			return R.array.Turkey;
		case "Ukraine":
			return R.array.Ukraine;
		case "United Arab Emirates":
			return R.array.United_Arab_Emirates;
		case "United Kingdom":
			return R.array.United_Kingdom;
		case "Uruguay":
			return R.array.Uruguay;
		case "Venezuela":
			return R.array.Venezuela;
		case "Vietnam":
			return R.array.Vietnam;
		case "Virgin Islands US":
			return R.array.Virgin_Islands_US;

		default:
			return -1;
		}
	}
}
