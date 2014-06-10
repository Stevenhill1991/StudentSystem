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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;

public class LecturerRegisterActivity extends Activity {
	Preferences p;
	String userName;
	String userType;
	ListView l1;	
	List<HashMap<String, String>> registerdata;
	HashMap<String,String> hm;
	boolean registerInformation = false;
	
	ListView l2;	
	List<HashMap<String, String>> registerdatalogs;
	HashMap<String,String> hmlogs;
	boolean registerlogs = false;
	
	String moduleName;
	String startTime;
	String endTime;
	String registerData[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_register);
		
		p = new Preferences(getApplicationContext());
		p.checkSession();
		userName = p.getUserName();
		Log.v("USERNAME", userName);
		
		TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
		tabHost.setup();
		TabSpec spec1= tabHost.newTabSpec("Tab 1");
		spec1.setIndicator("Take Attendance");	
		spec1.setContent(R.id.tab1);
		tabHost.addTab(spec1);
		// get list of todays lectures:
		getRegisterInformation();		
		if (registerInformation){
		 l1.setOnItemClickListener(new OnItemClickListener(){			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {			
				System.out.println(arg2);
				hm = registerdata.get(arg2);
				moduleName = hm.get("moduleName");
				startTime = hm.get("startTime");
				endTime = hm.get("endTime");
				System.out.println(moduleName + " " + startTime + " " + endTime);
				Intent intentSwitch = new Intent(LecturerRegisterActivity.this,LecturerRegisterAttendanceActivity.class);					
				intentSwitch.putExtra("moduleName", moduleName);
				intentSwitch.putExtra("startTime", startTime);
				intentSwitch.putExtra("endTime", endTime);				
				startActivity(intentSwitch);					
			}			
		});
		
		}
		TabSpec spec2 = tabHost.newTabSpec("Tab 2");
		spec2.setIndicator("Attendance Logs");
		spec2.setContent(R.id.tab2);		
		tabHost.addTab(spec2);
		
		getRegisterLogs();
		if (registerlogs){
			 l2.setOnItemClickListener(new OnItemClickListener(){			
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {			
					System.out.println(arg2);
					
					hmlogs = registerdatalogs.get(arg2);
					moduleName = hmlogs.get("moduleName");
					startTime = hmlogs.get("startTime");
					endTime = hmlogs.get("endTime");
					System.out.println(moduleName + " " + startTime + " " + endTime + registerData);
					 
					Intent intentSwitch2 = new Intent(LecturerRegisterActivity.this,LecturerRegisterLogsViewActivity.class);	
					//Intent intentSwitch2 = new Intent(LecturerRegisterActivity.this,LecturerLogsView.class);
					intentSwitch2.putExtra("moduleName", moduleName);
					intentSwitch2.putExtra("startTime", startTime);
					intentSwitch2.putExtra("endTime", endTime);		
					intentSwitch2.putExtra("registerData",registerData[arg2]);
					startActivity(intentSwitch2);		
					
					
				}			
			});
			
			}
	}	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_teacher_actionbar, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {	
		case R.id.action_logout:
			//Logout button:
			p.logoutSession(this);
			return true;
			default:
				return super.onOptionsItemSelected(item);
		}		
	}
	@Override
	public void onBackPressed() {
		// when back is pressed assume logout is wanted.
		p.logoutSession(this);
	}
	public String getURL(int tab) {
		Urls url = new Urls();
		String URL ="";
		if (tab==1){
		URL = url.getRegisterURL();
		}
		else {
	    URL = url.getRegisterLogsURL();
		}
		return URL;
	}
	public void getRegisterInformation(){
		String URL = getURL(1);
	
	String data[] = { URL, "userName", userName};
	JSONTask jTask = new JSONTask(this.getApplicationContext());
	// gets the JSONObject from the task.
			try {
				JSONObject timetableResponse = jTask.execute(data).get();
				if (timetableResponse != null) {
					registerInformation=true;
					try {
						JSONArray jArray = timetableResponse
								.getJSONArray("lecture");
						ArrayList<String> lects = new ArrayList<String>();
						registerdata = new ArrayList<HashMap<String, String>>();					
						for (int i = 0; i < jArray.length(); i++) {
							JSONObject jObj = jArray.getJSONObject(i);
							String mName = jObj.getString("moduleName");
							String sTime = jObj.getString("startTime").substring(0,5); 
							String eTime = jObj.getString("endTime").substring(0,5);	
							lects.add(sTime + " - " +eTime + " "+ mName);
							// create a new hash map to store lesson data to use when grabbing names of students.
							hm = new HashMap<String,String>();
							hm.put("moduleName", jObj.getString("mid"));
							hm.put("startTime", jObj.getString("startTime"));
							hm.put("endTime", jObj.getString("endTime"));
							registerdata.add(hm);						
						}				
						String[] lectureList = new String[lects.size()];
						for (int i = 0; i<lects.size();i++){
							// fill with data
							lectureList[i] = lects.get(i);
						}						
						//set to list view
						l1=(ListView)findViewById(R.id.namesList);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lectureList);
						l1.setAdapter(adapter);						
					}catch (JSONException e) {

					}
				} else {
					registerInformation = false;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				Toast.makeText(this.getApplicationContext(), "Execution exception",
						Toast.LENGTH_LONG).show();
			}
	}
	public void getRegisterLogs(){
    String URL = getURL(2);	
	String data[] = { URL, "userName", userName};
	JSONTask jTask = new JSONTask(this.getApplicationContext());
	// gets the JSONObject from the task.
			try {
				JSONObject timetableResponse = jTask.execute(data).get();
				if (timetableResponse != null) {
					registerlogs=true;
					try {
						JSONArray jArray = timetableResponse
								.getJSONArray("lecture");
						ArrayList<String> lects2 = new ArrayList<String>();
						registerdatalogs = new ArrayList<HashMap<String, String>>();	
						registerData  = new String[jArray.length()];
						for (int i = 0; i < jArray.length(); i++) {
							JSONObject jObj = jArray.getJSONObject(i);
						
								registerData[i] = jObj.getString("registerData");
								System.out.println("REGISTER DATA" + registerData);
							
							String mName = jObj.getString("moduleName");
							String sTime = jObj.getString("startTime").substring(0,5); 
							String eTime = jObj.getString("endTime").substring(0,5);	
							lects2.add(sTime + " - " +eTime + " "+ mName);
							// create a new hash map to store lesson data to use when grabbing names of students.
							hmlogs = new HashMap<String,String>();
							hmlogs.put("moduleName", jObj.getString("mid"));
							hmlogs.put("startTime", jObj.getString("startTime"));
							hmlogs.put("endTime", jObj.getString("endTime"));
							registerdatalogs.add(hmlogs);						
						}			
						
						String[] lectureList = new String[lects2.size()];
						for (int i = 0; i<lects2.size();i++){
							// fill with data
							lectureList[i] = lects2.get(i);
						}						
						//set to list view
						l2=(ListView)findViewById(R.id.listView2);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lectureList);
						l2.setAdapter(adapter);						
					}catch (JSONException e) {

					}
				} else {
					registerlogs = false;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				Toast.makeText(this.getApplicationContext(), "Execution exception",
						Toast.LENGTH_LONG).show();
			}
	}
	
}
