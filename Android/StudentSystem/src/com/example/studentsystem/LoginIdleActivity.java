package com.example.studentsystem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginIdleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_login_idle);
		
	        
		Preferences p = new Preferences(getApplicationContext());
		String roomname = p.getRoomName();
		if (!roomname.equals("null")) {
			TextView roomNum = (TextView)findViewById(R.id.loginidle_roomname);
			roomNum.setText(roomname);
			Urls u = new Urls();
			String URL = u.getLoginIdleURL();
			// get timetable information based on this room
			String data[] = { URL, "roomname", roomname };
			processTimetableData(data);
		}
		// start the screen time, so that it updates with a runnable
		setScreenTime();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_idle, menu);
		return true;
	}

	public void processTimetableData(String[] data) {
		JSONTask jTask = new JSONTask(this.getApplicationContext());		
		try {
			JSONObject timetableResponse = jTask.execute(data).get();
			if (timetableResponse != null) {
				try {
					JSONArray jArray = timetableResponse
							.getJSONArray("lecture");
					ArrayList<String> lects = new ArrayList<String>();					
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject jObj = jArray.getJSONObject(i);
						String startTime = jObj.getString("startTime").substring(0,5);
						String moduleName = jObj.getString("moduleName");											
						lects.add(startTime + " " + moduleName + "\n");
					}
					String[] lectureList = new String[lects.size()];
					for (int i = 0; i < lects.size(); i++) {
						// fill with data
						lectureList[i] = lects.get(i);
					}
					ListView l1 = (ListView) findViewById(R.id.roomDataList);
					//ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					//		this, android.R.layout.simple_list_item_1,
					//		lectureList);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							this, R.layout.centerlistview,
							lectureList);
					l1.setAdapter(adapter);
				} catch (JSONException e){
					
				}
				System.out.println("data not null");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			Toast.makeText(this.getApplicationContext(), "Execution exception", Toast.LENGTH_LONG).show();
		}
		
	}

	void setScreenTime() {
		// handler to constantly update time.
		final Handler handler = new Handler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				// update time every second.
				SimpleDateFormat formatter = new SimpleDateFormat("E dd MMMM hh:mm a");
				String today = formatter.format(new Date());				
				TextView dt = (TextView) findViewById(R.id.loginidle_dateandtime);
				dt.setText(today);
				handler.postDelayed(this, 500); // set time here to refresh
												// textView
			}
		});
	}

	@Override
	public void onUserInteraction() {
		// on touch send back to login screen as we are no longer idle.
		Intent myIntent = new Intent(LoginIdleActivity.this,
				LoginActivity.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
		startActivity(myIntent);

	}

}
