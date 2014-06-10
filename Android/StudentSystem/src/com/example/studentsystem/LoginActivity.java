package com.example.studentsystem;

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
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	// setupNFC();
	NfcAdapter mNfcAdapter;
	PendingIntent pendingIntent;
	IntentFilter[] mFilters;
	String[][] mTechLists;
	// processLogin(data[])
	String ARRAY_VALUE = "user";
	//timer:
	IdleTimer idt;
	int time;
	Preferences p;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the view layout:
		
		setContentView(R.layout.activity_login);
		// stop keyboard auto showing at the start of the application
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setupNFC();
		// setup the timer: auto starts.
		p = new Preferences(getApplicationContext());
		time = p.getIdleTime();
		Log.v("TIME",Integer.toString(time));		
		// Idle time, 1 = idle screen 2 = login screen.
		idt = new IdleTimer(time,getApplicationContext(),1);	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
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

	/*
	 * When the Login button is pressed
	 */
	public void loginButton(View view) {
		TextView userNameInput = (TextView) findViewById(R.id.usernameInput);
		TextView passWordInput = (TextView) findViewById(R.id.passwordInput);
		String userName = userNameInput.getText().toString();
		String passWord = passWordInput.getText().toString();
		String URL = getURL();
		String[] data = { URL, "username", userName, "password", passWord };
		System.out.println(URL + userName + passWord);
		processLogin(data);
	}

	/*
	 * Get the URL for the JSON login request
	 */
	public String getURL() {
		Urls urls = new Urls();
		String url = urls.getLoginURL();
		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	public void onNewIntent(Intent intent) {
		System.out.println("Scanning card");
		// Intent Changes when card is scanned.
		String tagId = getTagID(intent);
		// get the url for the NFC login
		String URL = getURL();
		// create the array to send to the task to fetch the login data
		String[] data = { URL, "uid", tagId };
		processLogin(data);
	}

	public void processLogin(String[] data) {
		
		// Processes the Data array containing: URL,PostData.
		JSONTask jTask = new JSONTask(this.getApplicationContext());
		// gets the JSONObject from the task.
		try {
			JSONObject loginResponse = jTask.execute(data).get();
			if (loginResponse != null) {
				try {
					JSONArray jArray = loginResponse.getJSONArray(ARRAY_VALUE);
					JSONObject jObj = jArray.getJSONObject(0);
					String userName = jObj.getString("userName");
					String userType = jObj.getString("userType");
					System.out.println("JObj" + userName + userType);

					Intent intentSwitch = null;
					// switch intent based upon user intent:
					int userTypeSwitch = Integer.parseInt(userType);
					switch (userTypeSwitch) {
					case 0:
						intentSwitch = new
						Intent(this,AdminHomeActivity.class);
						break;
					case 1:						
						intentSwitch = new Intent(this,
						 LecturerRegisterActivity.class);						
						break;
					case 2:
						intentSwitch = new Intent(this,
								StudentTimetableActivity.class);
						break;
					default:
						Toast.makeText(getApplicationContext(),
								"Error Switching Intent", Toast.LENGTH_LONG)
								.show();
						break;
					}						
					// set the shared preferences for the next page:
					//intentSwitch.putExtra("userName", userName);
					//intentSwitch.putExtra("userType", userType);
					p.createSession(userName, userType);
					startActivity(intentSwitch);
					finish();
				} catch (JSONException e) {

				}
			} else {
				// invalid login				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			Toast.makeText(this.getApplicationContext(), "Execution exception", Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		// pause timer when not in the foreground		
	    idt.cancel();
		// disable NFC scans while program is in the background
		mNfcAdapter.disableForegroundDispatch(this);				
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		idt.start();		
		// enable NFC scans while program is in the foreground
		mNfcAdapter.enableForegroundDispatch(this, pendingIntent, mFilters,
				mTechLists);		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onUserInteraction()
	 */
	@Override
	public void onUserInteraction() {
		// reset the timer back to 0
		idt.cancel();
		idt.start();
	}
}
