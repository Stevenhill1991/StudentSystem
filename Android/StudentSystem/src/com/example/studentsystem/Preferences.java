/*
 * Author: Steven Hill
 * Date: 01/01/14
 * Version: 0.1
 * 
 */
package com.example.studentsystem;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
/*
 * Used to get stored settings and session variables
 * 
 */
public class Preferences {
	SharedPreferences settingspreferences;
	Editor editor;
	Context c;
	SharedPreferences pref;
	
public Preferences(Context context){
	c = context;	
	pref = c.getSharedPreferences("SESSION", 0);
	editor = pref.edit();
	}

/*
 * Used to get stored device settings
 */
public int getIdleTime(){	
	settingspreferences = PreferenceManager.getDefaultSharedPreferences(c
			.getApplicationContext());
	String time = settingspreferences.getString("setIdle", "null");
	int idleTime;
	if(!time.equals("null")){
		idleTime = Integer.parseInt(time);
		idleTime *= 1000;
	} else {
		//default 2 mins
		idleTime = 120000;
	}
	return idleTime;
}
public String getRoomName(){
	settingspreferences = PreferenceManager.getDefaultSharedPreferences(c
			.getApplicationContext());
	String roomName = settingspreferences.getString("setRoom", "null");
	return roomName;
}


/*
 * Session data
 * used for logging in and out.
 */

public void createSession(String userName, String userType){
	editor.putBoolean("login",true);
	editor.putString("userName",userName);
	editor.putString("userType",userType);
	editor.commit();
}
public HashMap<String,String> getSession(){	
	HashMap<String,String> sessionMap = new HashMap<String,String>();
	sessionMap.put("userName",pref.getString("userName",null));
	sessionMap.put("userType",pref.getString("userType",null));
	return sessionMap;
}
public void checkSession(){	
	if(!this.loggedIn()){
		Intent intentSwitch = new Intent(c,LoginActivity.class);
		intentSwitch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
    	intentSwitch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	intentSwitch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		c.startActivity(intentSwitch);
	}
}
public boolean loggedIn(){
	return pref.getBoolean("login", false);
}

public String getUserName(){
	return pref.getString("userName",null);
}
public String getUserType(){
	return pref.getString("userType",null);
}

public void logoutSession(final Activity act){

	// warn with dialog about logging out!
	new AlertDialog.Builder(act)
	.setCancelable(false)
    .setTitle("Logout!")
    .setMessage("Are you sure you want to logout?")
    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) { 
            // continue with logout
        	editor.clear();
        	editor.commit();        	
        	Intent intentSwitch = new Intent(act.getApplicationContext(),LoginActivity.class);
        	intentSwitch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
        	intentSwitch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	intentSwitch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    		act.startActivity(intentSwitch);       	    	
        }
     })
    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) { 
            // do nothing resume back to application
        }
     })
     .show();
		
}


}