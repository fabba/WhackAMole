package com.example.whackamole;

import com.example.whackamole.GameActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
/*
 private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Intent intent = new Intent (getBaseContext(), GameActivity.class);
    	startActivityForResult(intent,0);
    }
=======
	private static final String TAG = MainActivity.class.getSimpleName();
*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void onClick(View view) {
		if (view.getId() == R.id.newGame) {
			Intent intent = new Intent (view.getContext(), GameActivity.class);
	    	startActivityForResult(intent,0);
		}
		else if (view.getId() == R.id.setting) {
	    	Intent intent = new Intent (view.getContext(), SettingActivity.class);
	    	startActivityForResult(intent,0);
		}
	}
}


