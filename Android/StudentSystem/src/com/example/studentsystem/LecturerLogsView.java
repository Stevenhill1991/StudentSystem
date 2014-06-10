package com.example.studentsystem;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LecturerLogsView extends Activity {
	 ArrayList<String> listItems;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_lecturer_logs_view);
	        ListView namesList = (ListView)findViewById(R.id.logNamesList);
	        String names[]={"Joe Bloggs", "David Jones"};

	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
	                this, android.R.layout.simple_list_item_1,
	                names);
	        namesList.setAdapter(adapter);

	        namesList.getChildAt(0).setBackgroundColor(Color.GREEN);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lecturer_logs_view, menu);
		return true;
	}

}
