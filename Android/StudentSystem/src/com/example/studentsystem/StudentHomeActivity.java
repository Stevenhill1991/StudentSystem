package com.example.studentsystem;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
public class StudentHomeActivity extends Activity {
	String userName = "Error";
	String userType = "Error";
	Preferences p;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout                  .activity_student_home);
		// Show the Up button in the action bar.
		setupActionBar();
		ActionBar actionBar = getActionBar();	
		actionBar.setHomeButtonEnabled(false);		
		actionBar.setDisplayHomeAsUpEnabled(false);
		// get extras such as username and usertype.
		//Bundle extras = getIntent().getExtras();
		p = new Preferences(getApplicationContext());
		p.checkSession();		
	    userName = p.getUserName();
		userType = p.getUserType();		
		actionBar.setTitle("Student Home - " + userName);
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
	
	public void viewTimetable(View view){			
		 Intent intentSwitch = new Intent(this, StudentTimetableActivity.class);		
		 startActivity(intentSwitch);		
	}


}
