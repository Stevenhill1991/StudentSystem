package com.example.studentsystem;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

public class LecturerBookingActivity extends Activity {
	Preferences p;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_booking);
		p = new Preferences(getApplicationContext());
		p.checkSession();		
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
	

}
