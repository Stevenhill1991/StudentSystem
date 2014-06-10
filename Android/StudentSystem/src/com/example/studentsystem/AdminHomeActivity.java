package com.example.studentsystem;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AdminHomeActivity extends Activity {
	Preferences p;
	String userName;
	String userType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admins_home);
		// get session variables and check session
		p = new Preferences(getApplicationContext());
		p.checkSession();
		userName = p.getUserName();
		userType = p.getUserType();
		// set title of actionbar
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Admin Home - " + userName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_admin_actionbar, menu);
		return true;
	}

	/*
	 * Used for the options menu
	 * 
	 * @param item item selected from the options menu
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_logout:
			// Logout button:
			p.logoutSession(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * Check for the user pressing back, if so log them out
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		p.logoutSession(this);
	}

	/*
	 * When application is resumed, update settings
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		updateSettings();

	}

	/*
	 * Used to update the settings, useful when a user comes back from the
	 * settings screen, as it will not auto update.
	 */
	public void updateSettings() {
		p = new Preferences(this);
		String roomName = p.getRoomName();
		String idleTime = Integer.toString(p.getIdleTime() / 1000) + " Seconds";
		TextView RoomName = (TextView) findViewById(R.id.admin_room_output);
		TextView IdleTime = (TextView) findViewById(R.id.admin_idle_output);
		if (!roomName.equals("null")) {
			RoomName.setText(roomName);
		}
		if (!idleTime.equals("null")) {
			IdleTime.setText(idleTime);
		}
	}

	/*
	 * Used for the settings button to switch to change settings
	 * 
	 * @param view the current view
	 */
	public void settings(View view) {
		Intent intentSwitch = new Intent(this, AdminSettingsActivity.class);
		startActivity(intentSwitch);
	}

}
