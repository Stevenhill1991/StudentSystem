package com.example.studentsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.support.v4.app.NavUtils;

public class StudentTimetableActivity extends Activity {
	String userName = "Error";
	String userType = "Error";
	Preferences p;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_timetable);
		// Show the Up button in the action bar.
		setupActionBar();
		p = new Preferences(getApplicationContext());
		p.checkSession();		
	    userName = p.getUserName();
		userType = p.getUserType();

		Spinner spinner = (Spinner) findViewById(R.id.timetable_spinner);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String selectedItem = parent.getItemAtPosition(position)
						.toString();
				System.out.println(selectedItem);
				getTimetableData(selectedItem);
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	public void getTimetableData(String selected) {
		String type = null;
		if (selected.equals("Today")) {
			type = "1";
		} else if (selected.equals("This week")) {
			type = "7";
		}
		String URL = getURL();
		String data[] = { URL, "timetable", type, "userName", userName };
		JSONTask jTask = new JSONTask(this.getApplicationContext());
		// gets the JSONObject from the task.
		try {
			JSONObject timetableResponse = jTask.execute(data).get();
			if (timetableResponse != null) {
				try {
					if (type.equals("1")){
						//get days of week the student studies:
						JSONArray jArray = timetableResponse
								.getJSONArray("lecture");
						
						//create days array.						
						String days[] = {"Today"};
						List<HashMap<String, String>> groupData = new ArrayList<HashMap<String, String>>();
						for (int i = 0; i < days.length; i++) {							
							HashMap<String, String> hm = new HashMap<String, String>();
							hm.put("day", days[i]);
							groupData.add(hm);
						}
						String fromParent[] = {"day"};
						int toParent[]= {R.id.dayName};						
						
						//child data
						
						List<List<HashMap<String, String>>> childData = new ArrayList<List<HashMap<String, String>>>();
						for (int i=0;i<1;i++){
							childData.add(new ArrayList<HashMap<String,String>>());
						}
						for (int i = 0; i < jArray.length(); i++) {	
							JSONObject jObj = jArray.getJSONObject(i);	
							
							HashMap<String, String> hm = new HashMap<String, String>();
							hm.put("moduleName", jObj.getString("moduleName"));
							hm.put("startTime",
									(jObj.getString("startTime")).substring(0, 5));
							hm.put("endTime",
									(jObj.getString("endTime")).substring(0, 5));
							hm.put("group", jObj.getString("group"));
							hm.put("roomName", jObj.getString("roomName"));
							hm.put("firstName", jObj.getString("firstName"));
							hm.put("lastName", jObj.getString("lastName"));		
							
							
								childData.get(0).add(hm);
							
																	
						}						
							if (childData.get(0).isEmpty()){
								HashMap<String, String> hm = new HashMap<String, String>();
								hm.put("moduleName","No Lectures today!");
								childData.get(0).add(hm);
							}						
						
						String[] fromChild = { "moduleName", "startTime", "endTime",
								"group", "roomName", "firstName", "lastName" };
						int[] toChild = { R.id.moduleName, R.id.startTime, R.id.endTime,
								R.id.group, R.id.roomName, R.id.firstName,
								R.id.lastName };
						SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this,
								groupData,R.layout.timetable_parent_layout,fromParent,toParent,
								childData,R.layout.timetable_layout,fromChild,toChild);
						
						ExpandableListView listitems = (ExpandableListView) findViewById(R.id.timetable_list);						
						listitems.setAdapter(adapter);
						listitems.expandGroup(0);		
						
						
					// WEEKLY TIMETABLE:
					} else if (type.equals("7")){
						//get days of week the student studies:
						JSONArray jArray = timetableResponse
								.getJSONArray("lecture");
						
						//create days array.						
						String days[] = {"Monday", "Tuesday","Wednesday","Thursday", "Friday","Saturday","Sunday"};
						List<HashMap<String, String>> groupData = new ArrayList<HashMap<String, String>>();
						for (int i = 0; i < 7; i++) {							
							HashMap<String, String> hm = new HashMap<String, String>();
							hm.put("day", days[i]);
							groupData.add(hm);
						}
						String fromParent[] = {"day"};
						int toParent[]= {R.id.dayName};						
						
						//child data
						
						List<List<HashMap<String, String>>> childData = new ArrayList<List<HashMap<String, String>>>();
						for (int i=0;i<7;i++){
							childData.add(new ArrayList<HashMap<String,String>>());
						}
						for (int i = 0; i < jArray.length(); i++) {	
							JSONObject jObj = jArray.getJSONObject(i);	
							
							HashMap<String, String> hm = new HashMap<String, String>();
							hm.put("moduleName", jObj.getString("moduleName"));
							hm.put("startTime",
									(jObj.getString("startTime")).substring(0, 5));
							hm.put("endTime",
									(jObj.getString("endTime")).substring(0, 5));
							hm.put("group", jObj.getString("group"));
							hm.put("roomName", jObj.getString("roomName"));
							hm.put("firstName", jObj.getString("firstName"));
							hm.put("lastName", jObj.getString("lastName"));		
							
							if (Integer.parseInt(jObj.getString("day")) == 0){
								childData.get(6).add(hm);	
							} else {
								childData.get((Integer.parseInt(jObj.getString("day")))-1).add(hm);
							}
																	
						}
						for (int i=0;i<7;i++){
							if (childData.get(i).isEmpty()){
								HashMap<String, String> hm = new HashMap<String, String>();
								hm.put("moduleName","No Lectures today!");
								childData.get(i).add(hm);
							}
						}
						
						String[] fromChild = { "moduleName", "startTime", "endTime",
								"group", "roomName", "firstName", "lastName" };
						int[] toChild = { R.id.moduleName, R.id.startTime, R.id.endTime,
								R.id.group, R.id.roomName, R.id.firstName,
								R.id.lastName };
						SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this,
								groupData,R.layout.timetable_parent_layout,fromParent,toParent,
								childData,R.layout.timetable_layout,fromChild,toChild);
						
						ExpandableListView listitems7 = (ExpandableListView) findViewById(R.id.timetable_list);						
						listitems7.setAdapter(adapter);
					}
				} catch (JSONException e) {

				}
			} else {
				// invalid request for timetable
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			Toast.makeText(this.getApplicationContext(), "Execution exception",
					Toast.LENGTH_LONG).show();
		}
	}

	public String getURL() {
		Urls url = new Urls();
		String URL = url.getTimetableURL();
		return URL;
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_student_actionbar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			p.logoutSession(this);
			/*
			// NavUtils.navigateUpFromSameTask(this);
			Intent intentSwitch = NavUtils.getParentActivityIntent(this);
			intentSwitch.putExtra("userName", userName);
			intentSwitch.putExtra("userType", userType);
			NavUtils.navigateUpTo(this, intentSwitch);
			*/
			return true;
		case R.id.action_logout:
			//Logout button:
			p.logoutSession(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		// when back is pressed assume logout is wanted.
		p.logoutSession(this);
	}
	
	

}
