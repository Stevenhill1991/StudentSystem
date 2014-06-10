package com.example.studentsystem;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class LecturerHomeActivity extends Activity {
	Preferences p;
	String userName;
	String userType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_home);
		p = new Preferences(getApplicationContext());
		p.checkSession();
		userName = p.getUserName();
		userType = p.getUserType();
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
	public void viewBook(View view){			
		 Intent intentSwitch = new Intent(this, LecturerBookingActivity.class);		
		 startActivity(intentSwitch);		
	}
	public void viewRegister(View view){			
		 Intent intentSwitch = new Intent(this, LecturerRegisterActivity.class);		
		 startActivity(intentSwitch);		
	}

}
