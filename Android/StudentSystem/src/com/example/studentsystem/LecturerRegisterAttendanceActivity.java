	package com.example.studentsystem;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LecturerRegisterAttendanceActivity extends Activity {
	// setupNFC();
	NfcAdapter mNfcAdapter;
	PendingIntent pendingIntent;
	IntentFilter[] mFilters;
	String[][] mTechLists;
	//
	Preferences p;
	String moduleName;
	String startTime;
	String endTime;
	ListView l1;
	ArrayList<String> tagIdList;
	int attendanceList[];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecturer_attendance);
		p = new Preferences(getApplicationContext());
		p.checkSession();
		Bundle b = getIntent().getExtras();
		if (b != null) {
			moduleName = b.getString("moduleName");
			startTime = b.getString("startTime");
			endTime = b.getString("endTime");
			getRegisterInformation();
			setupNFC();
		} else {
			// go back null
			Toast t = Toast.makeText(this, "Error Loading Register!",
					Toast.LENGTH_LONG);
			t.show();
			super.onBackPressed();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher_attendance, menu);
		return true;
	}

	public String getURL() {
		Urls url = new Urls();
		String URL = url.getRegisterNamesURL();
		return URL;
	}

	public void getRegisterInformation() {
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
					tagIdList = new ArrayList<String>();
					attendanceList = new int[jArray.length()];
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject jObj = jArray.getJSONObject(i);
						String fName = jObj.getString("firstName");
						String lName = jObj.getString("lastName");
						tagIdList.add(jObj.getString("uid"));						
						lects.add(fName + " " + lName);
					}
					String[] lectureList = new String[lects.size()];
					for (int i = 0; i < lects.size(); i++) {
						// fill with data
						lectureList[i] = lects.get(i);
					}
					// set to list view
					l1 = (ListView) findViewById(R.id.namesList);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							this, android.R.layout.simple_list_item_1,
							lectureList);
					l1.setAdapter(adapter);

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
	public void onPause() {
		super.onPause();		
		// disable NFC scans while program is in the background
		mNfcAdapter.disableForegroundDispatch(this);				
	}

	@Override
	public void onResume() {
		super.onResume();		
		// enable NFC scans while program is in the foreground
		mNfcAdapter.enableForegroundDispatch(this, pendingIntent, mFilters,
				mTechLists);		
	}

	public void setupNFC() {
		// filter for NFC scans
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
			// Stop here program automatically closes, we definitely need NFC
			Toast.makeText(this, "This device doesn't support NFC.",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		if (!mNfcAdapter.isEnabled()) {
			Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();
		} else {
			// nfc enabled
		}

		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		// Setup an intent filter for all MIME based dispatches
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		IntentFilter td = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		mFilters = new IntentFilter[] { ndef, td };

		// setup tech list for my cards, mifareclassic/nfcA
		mTechLists = new String[][] { new String[] { NfcA.class.getName(),
				MifareClassic.class.getName() } };
	}

	/*
	 * Get Tag ID from NFC Card
	 */
	public String getTagID(Intent intent) {
		// Create a String to store the TagId
		String tagId;
		// create a new conversion to parse the byte to a hex code in string
		// form
		Conversion cv = new Conversion();
		Tag t = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		tagId = cv.convertByteToHex(t.getId());
		// return String tagId as hex
		return tagId;
	}
	public void processScan(String tagId){

		for (int i=0;i<tagIdList.size();i++){
			System.out.println("tagIdList" + tagIdList.get(i));
			if(tagId.equals(tagIdList.get(i))){
				l1.getChildAt(i).setBackgroundColor(Color.GREEN);
				attendanceList[i]=1;
			}

		}

	}
	@Override
	public void onNewIntent(Intent intent) {
		System.out.println("Scanning card");
		// Intent Changes when card is scanned.
		String tagId = getTagID(intent);
		// get the url for the NFC login
		System.out.println(tagId);
		processScan(tagId);
	}
	public void viewCancel(View view){	
		// warn with dialog about logging out!
		new AlertDialog.Builder(this)
		.setCancelable(false)
		.setTitle("Cancel!")
		.setMessage("All current timetable data will be discarded?")
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				// continue with cancel       	      	
				Intent intentSwitch = new Intent(LecturerRegisterAttendanceActivity.this,LecturerRegisterActivity.class);
				intentSwitch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				intentSwitch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intentSwitch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intentSwitch);       	    	
			}
		})
		.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				// do nothing resume back to application
			}
		})
		.show();

	}
	public void viewFinish(View view){	
		// warn with dialog about logging out!
		new AlertDialog.Builder(this)
		.setCancelable(false)
		.setTitle("Finished!")
		.setMessage("Are you sure you have finished the register?")
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				// continue with finish
				updateRegister();
				// update database with finish data
				Intent intentSwitch = new Intent(LecturerRegisterAttendanceActivity.this,LecturerRegisterActivity.class);
				intentSwitch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				intentSwitch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intentSwitch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intentSwitch);       	    	
			}
		})
		.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				// do nothing resume back to application
			}
		})
		.show();

	}
	public void updateRegister(){
		System.out.println("STARTING UPDATE REGISTER");
		Urls url = new Urls();
		String URL = url.getRegisterUpdateURL();
		String registerData ="a";
		for(int i =0;i<attendanceList.length;i++){
			if(attendanceList[i]==1){
			registerData=registerData+attendanceList[i];
			} else {
				registerData=registerData+"0";
			}
		}
		System.out.println("REGISTER DATA " + registerData);

		String data[] = {URL,"registerData",registerData,"mid",moduleName,"startTime",startTime,"endTime",endTime};	
		JSONTask jTask = new JSONTask(this.getApplicationContext());
		// gets the JSONObject from the task.

		try {
			JSONObject timetableResponse = jTask.execute(data).get();
			if (timetableResponse != null) {
				
					System.out.println("RESPONSE NOT NULL WORKS");
				
			} 
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			Toast.makeText(this.getApplicationContext(), "Execution exception",
					Toast.LENGTH_LONG).show();
		}
	}
}

