package com.example.studentsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.Toast;
	
public class IdleTimer extends CountDownTimer {
	/*
	 * creates a timer for certain actions: 
	 * 1: switches to the login idle screen
	 * 2: switches to the login screen (idle when logged in)
	 */
	Context c;
	int switchType;
	public IdleTimer(long time,Context c, int switchType) {
		super(time, 1000);
		this.c = c;
		this.switchType = switchType;
		System.out.println("CONTEXT AND TYPE SAVED");
	}
	
	@Override
	public void onFinish() {
		Intent intentSwitch;
		// When clock reaches its goal, switch to the idle screen.
		switch (switchType){
		case 1: 
			// switch to the login idle screen
			intentSwitch = new Intent(c,LoginIdleActivity.class);
			intentSwitch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			c.startActivity(intentSwitch);
			break;
		case 2:
			// force login screen and logout
			
		    // clear session:
			Preferences p = new Preferences(c);
			p.getSession();
			Activity activity = (Activity) c;
			p.logoutSession(activity);			
			break;
		default:
			Toast.makeText(c.getApplicationContext(), "Error with timer!", Toast.LENGTH_LONG).show();
			break;
		}		
	}

	@Override
	public void onTick(long millisUntilFinished) {
		
	}
}
