/*********************************
 * HW #5
 * FileName: MainActivity.java
 *********************************
 * Team Members:
 * Richa Kandlikar
 * Sai Phaninder Reddy Jonnala
 * *******************************
 */

package com.example.hw5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONException;
import org.xml.sax.SAXException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

	RadioGroup rg;
	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rg = (RadioGroup) findViewById(R.id.radioGroup1);
		i = new Intent(MainActivity.this, PhotoActivity.class);

		findViewById(R.id.btnGallery).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				i.putExtra("MODE", 1);
				int id = rg.getCheckedRadioButtonId();
				if (id == R.id.radio0)
					new GetPhotosAsyncXML().execute();
				else if (id == R.id.radio1)
					new GetPhotosAsyncJSON().execute();
			}
		});

		findViewById(R.id.btnSlideShow).setOnClickListener(
				new OnClickListener() {

					public void onClick(View v) {
						i.putExtra("MODE", 2);
						int id = rg.getCheckedRadioButtonId();
						if (id == R.id.radio0)
							new GetPhotosAsyncXML().execute();
						else if (id == R.id.radio1)
							new GetPhotosAsyncJSON().execute();
					}
				});

	}


	public class GetPhotosAsyncXML extends AsyncTask<Void, Integer, ArrayList<Photos>> {

		String xURL = "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=9cdb8e2955dff3243e7e4351e143a8be&tags=uncc&extras=views%2Curl_m&per_page=100&format=rest";
		ProgressDialog pdMain;

		@Override
		protected ArrayList<Photos> doInBackground(Void... params) {
			try {
				URL url = new URL(xURL);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.connect();
				int statusCode = con.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_OK) {
					InputStream in = con.getInputStream();
					return PhotosUtil.PhotosSAXParser.parsePhotos(in);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			pdMain = new ProgressDialog(MainActivity.this);
			pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pdMain.setCancelable(false);
			pdMain.setMessage("Retriving Image Info");
			pdMain.show();
		}

		protected void onPostExecute(ArrayList<Photos> result) {
			if (result != null)
				Log.d("demo", result.toString());
			else
				Log.d("demo", "result is NULL");
			pdMain.dismiss();
			i.putParcelableArrayListExtra("photos",
					PhotosUtil.photolist);
			startActivity(i);
		}
	}

	public class GetPhotosAsyncJSON extends AsyncTask<Void, Void, ArrayList<Photos>> {

		String jURL = "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=9cdb8e2955dff3243e7e4351e143a8be&tags=uncc&extras=views%2Curl_m&per_page=100&format=json&nojsoncallback=1";
		ProgressDialog pdMain;

		@Override
		protected ArrayList<Photos> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				URL url = new URL(jURL);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setRequestMethod("GET");
				con.connect();
				int statusCode = con.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_OK) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = reader.readLine();
					while (line != null) {
						sb.append(line);
						line = reader.readLine();
					}
					Log.d("DEBUG", sb.toString());
					ArrayList<Photos> photolist = PhotosUtil.PhotosJSONParser
							.parsePhotos(sb.toString());
					return photolist;
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // catch (JSONException e) {
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			pdMain = new ProgressDialog(MainActivity.this);
			pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pdMain.setCancelable(false);
			pdMain.setMessage("Retriving Image Info");
			pdMain.show();
		}

		protected void onPostExecute(ArrayList<Photos> result) {
			if (result != null)
				Log.d("demo", result.toString());
			else
				Log.d("demo", "result is NULL");
			pdMain.dismiss();
			i.putParcelableArrayListExtra("photos",
					PhotosUtil.photolist);
			startActivity(i);
		}
	}
}
