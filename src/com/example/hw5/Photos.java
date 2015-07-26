/*********************************
 * HW #5
 * FileName: Photos.java
 *********************************
 * Team Members:
 * Richa Kandlikar
 * Sai Phaninder Reddy Jonnala
 * *******************************
 */

package com.example.hw5;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Parcel;
import android.os.Parcelable;

public class Photos implements Parcelable{

	String title,url;
	int views;
	
	public Photos(String title, int views, String url){
		this.title = title;
		this.views = views;
		this.url = url;
	}
	
	private Photos(Parcel in){
		this.title = in.readString();
		this.views = in.readInt();
		this.url = in.readString();
	}
	
	public Photos(JSONObject photoJSONObject) throws JSONException{
		this.title = photoJSONObject.getString("title");
		this.views = photoJSONObject.getInt("views");
		this.url = photoJSONObject.getString("m_url");
	}
	
	public String getURL()	{
		return url;
	}

	public int getViews() {
		return views;
	}

	public String getTitle() {
		return title;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel p, int flags) {
		// TODO Auto-generated method stub
		p.writeString(title);
		p.writeInt(views);
		p.writeString(url);
	}
	
	public static final Parcelable.Creator<Photos> CREATOR = new Parcelable.Creator<Photos>() {
		public Photos createFromParcel(Parcel in) {
			return new Photos(in);
		}

		public Photos[] newArray(int size) {
			return new Photos[size];
		}
	};
}
