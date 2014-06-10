package com.example.studentsystem;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LecturerRegisterLogsViewActivity extends Activity {
	Preferences p;
	String moduleName;
	String startTime;
	String endTime;
	String registerData;
	ListView l2;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecturer_logs_view);
		p = new Preferences(getApplicationContext());
		p.checkSession();
		Bundle b = getIntent().getExtras();
		if (b != null) {
			moduleName = b.getString("moduleName");
			startTime = b.getString("startTime");
			endTime = b.getString("endTime");
			registerData= b.getString("registerData").substring(1);
			System.out.println(registerData + "*****************");
			int total =0;
			for (int i=0;i<registerData.length();i++){
				 char c = registerData.charAt(i);            	 
 				if (c == '1'){
 					 total++;
 				}				    				
			}
			float perAtt = ((float)total/(float)registerData.length())*100;
			String s = String.format("%.2f",perAtt);
			TextView t1 = (TextView) findViewById(R.id.textView1);
			t1.setText(total + "/" + registerData.length() + " " + s + "% Attendance" );
			
			l2 = (ListView) findViewById(R.id.logNamesList);			
			getLogInformation();
			//System.out.println(registerData + "***************" + registerData.length());	
			
		
			   
			 /*
			 for (int i=0;i<registerData.length();i++){
    				char c = registerData.charAt(i);
    				System.out.println(c);
    				if (c == '1'){
    					System.out.println(l2.getChildAt(i).toString());
    				} else {
    					l2.getChildAt(i).setBackgroundColor(Color.RED);
    				}
    				}
				
			//l1.getChildAt(0).setBackgroundColor(Color.GREEN);
	*/
	   
		} else {
			// go back null
			Toast t = Toast.makeText(this, "Error Loading Log!",
					Toast.LENGTH_LONG);
			t.show();
			super.onBackPressed();
		}

	}
	public String getURL() {
		Urls url = new Urls();
		String URL = url.getRegisterNamesURL();
		return URL;
	}

	public void getLogInformation(){
		String URL = getURL();
		String data[] = { URL, "moduleName", moduleName, "startTime",
				startTime, "endTime", endTime };
		JSONTask jTask = new JSONTask(this.getApplicationContext());
		// gets the JSONObject from the task.
		try {
			JSONObject timetableResponse = jTask.execute(data).get();
			if (timetableResponse != null) {
				try {
					JSONArray jArray = timetableResponse
							.getJSONArray("register");
					ArrayList<String> lects = new ArrayList<String>();					
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject jObj = jArray.getJSONObject(i);
						String fName = jObj.getString("firstName");
						String lName = jObj.getString("lastName");											
						lects.add(fName + " " + lName + "\n");
					}
					String[] lectureList = new String[lects.size()];
					for (int i = 0; i < lects.size(); i++) {
						// fill with data
						lectureList[i] = lects.get(i);
					}
					// set to list view
					/*
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							this, android.R.layout.simple_list_item_1,
							lectureList);
							*/
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lects) {
					       @Override
					       public View getView(int position, View convertView, ViewGroup parent) {
					              View view=super.getView(position, convertView, parent); // we get the original view            
					              	
					              	 
					            	  char c = registerData.charAt(position);
					            	 
					    				if (c == '1'){
					    					 view.setBackgroundColor(Color.GREEN);
					    				} else {
					    					 view.setBackgroundColor(Color.RED);
					    				}					    				
					              return view;
					       }
					 };
					l2.setAdapter(adapter);
					
									
					
				} catch (JSONException e) {

				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			Toast.makeText(this.getApplicationContext(), "Execution exception",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lecturer_register_logs_view, menu);
		return true;
	}
	public void viewBack(View view){	
		super.onBackPressed();
	}
}
