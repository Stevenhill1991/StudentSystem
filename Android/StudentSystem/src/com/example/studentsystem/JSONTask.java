package com.example.studentsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class JSONTask extends AsyncTask<String[], Void, JSONObject> {
	InputStream is = null;
	JSONObject result = null;
	String jsonString = "";
	Context c;

	public JSONTask(Context c) {
		this.c = c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 * 
	 * @param String[] data = {url of json request, pairname values for POST
	 * request}.
	 * 
	 * @return Returns a JSON object containing the data from the request.
	 */
	@Override
	protected JSONObject doInBackground(String[]... data) {
		String[] strings = data[0];
		String URL = strings[0];
		int postLength = data[0].length - 1;
		for (int i = 0; i < data[0].length; i++) {
			System.out.println(strings[i]);
		}
		// Making HTTP request
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
			HttpConnectionParams.setSoTimeout(httpParameters, 5000);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(URL);
			try {
				// if > 0 must post data
				if (postLength > 0) {
					List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
					for (int i = 1; i < postLength; i += 2) {
						System.out.println("POSTING "+strings[i] + strings[i + 1]);
						postParameters.add(new BasicNameValuePair(strings[i],
								strings[i + 1]));
					}
					UrlEncodedFormEntity form = new UrlEncodedFormEntity(
							postParameters);
					httpPost.setEntity(form);
				} else {
					// else don't add post parameters
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (ConnectTimeoutException e) {
			makeToastOnUIThread("HTTP Error: Connection Timed Out!");
			return null;
		} catch (SocketTimeoutException e) {
			makeToastOnUIThread("HTTP Error: Connection Timed Out!");
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			System.out.println("CLIENT PROTOCOL EXCEPTION");
			makeToastOnUIThread("HTTP Error: Connection Refused!");
			// stop! error
			return null;
		} catch (IOException e) {
			System.out.println("IOEXCEPTION");
			makeToastOnUIThread("HTTP Error: Check Network Connection!");
			// stop! error
			return null;
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			jsonString = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		// error case, returns null.
		if (jsonString.substring(0, 1).equals("E")) { // Error: JSON starts with
														// {
			// JSON doesnt exist for these variables or URL.
			result = null;
			processNullMessage(strings[1]);
			return result;
		} else {
			// try parse the string to a JSON object
			try {
				result = new JSONObject(jsonString);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// return JSON Object
			return result;
		}

	}

	public void makeToastOnUIThread(final String message) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				// display on main thread
				Toast.makeText(c, message, Toast.LENGTH_LONG).show();
			}
		});
	}

	public void processNullMessage(String error) {
		final String type = error;
		// display on main thread toast
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				String message = "";
				if (type.equals("username")) {
					message = "Invalid Username or Password!";
				} else if (type.equals("uid")) {
					message = "Invalid Card!";
				} else if (type.equals("timetable")) {
					message = "Error Getting Timetable Data!";
				} else if (type.equals("roomname")) {
					message = "Error Getting Room Data!";
				} 
				System.out.println("MESSAGE =======" + type);
				Toast.makeText(c, message, Toast.LENGTH_LONG).show();
			}
		});
	}

	protected void onPostExecute(JSONObject result) {
		// nothing to do here, calling the method will return the JSONobject
		// the class will test if it is null or not
		// and will deal with the data itself.
	}

}
