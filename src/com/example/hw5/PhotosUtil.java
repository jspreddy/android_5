/*********************************
 * HW #5
 * FileName: PhotoUtil.java
 *********************************
 * Team Members:
 * Richa Kandlikar
 * Sai Phaninder Reddy Jonnala
 * *******************************
 */

package com.example.hw5;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.util.Xml;

public class PhotosUtil {

	public static ArrayList<Photos> photolist;
	public static Photos photo;

	static public class PhotosSAXParser extends DefaultHandler {

		
		static public ArrayList<Photos> parsePhotos(InputStream in) throws IOException, SAXException {
			PhotosSAXParser parser = new PhotosSAXParser();
			Xml.parse(in, Xml.Encoding.UTF_8, parser);
			return parser.getPhotolist();
		}

		public ArrayList<Photos> getPhotolist() {
			Collections.sort(photolist, new Comparator<Photos>() {
				@Override
				public int compare(Photos p1, Photos p2) {
					return p2.getViews() - p1.getViews(); 
				}
			});
			return photolist;
		}

		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			photolist = new ArrayList<Photos>();
			super.startDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			super.startElement(uri, localName, qName, attributes);
			if (localName.equals("photo")) {
				photo = new Photos(attributes.getValue("title"),Integer.parseInt(attributes.getValue("views")),attributes.getValue("url_m"));
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			super.characters(ch, start, length);
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			// TODO Auto-generated method stub
			super.endElement(uri, localName, qName);
			if (localName.equals("photo"))
				photolist.add(photo);
		}
	}
	
	static public class PhotosJSONParser {
		public static ArrayList<Photos> parsePhotos(String jsonString) throws JSONException {
			
			JSONArray photoJSONArray = new JSONArray(jsonString);
			
			for (int i = 0; i < photoJSONArray.length(); i++) {
				JSONObject photoJSONObject = photoJSONArray.getJSONObject(i);
				photo = new Photos(photoJSONObject);
				photolist.add(photo);
			}
			
			Collections.sort(photolist, new Comparator<Photos>() {
				@Override
				public int compare(Photos p1, Photos p2) {
					return p2.getViews() - p1.getViews(); 
				}
			});
			return photolist;
		}
	}

}
