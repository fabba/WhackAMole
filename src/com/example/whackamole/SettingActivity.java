package com.example.whackamole;

import java.util.ArrayList;

import models.users.UserModel;

import com.example.whackamole.R;

import databaseadapter.UserAdapter;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SettingActivity extends Activity {
	
	private ArrayAdapter<String> adapter;
	private ArrayList<String> userNames;
	private ListView userListView;
	private TextView userTextField;
	private int maxLevel;
	private int maxRound;
	private int currentLevel;
	private float onDownX;
	private float onDownY;
	private Button round1;
	private Button round2;
	private Button round3;
	private Button round4;
	private TextView upText;
	private TextView downText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentViewSetting();
	}
	
	private void setContentViewSetting() {
		setContentView(R.layout.activity_setting);
	
		SharedPreferences setting = getSharedPreferences("Setting", MODE_PRIVATE);
		String name = setting.getString("Name", "");
		UserAdapter user = new UserAdapter(this);
		user.open();
		UserModel userName = user.getUser(name);
		int[] level = user.getLastLevelAndRound(userName);
		user.close();
	
		maxLevel = level[0];
		maxRound = level[1];
		if( maxLevel == 0 && maxRound == 0){
			maxLevel = 1;
			maxRound = 0;
		}
		
		round2 = (Button) findViewById(R.id.round1);
		round1 = (Button) findViewById(R.id.round2);
		round3 = (Button) findViewById(R.id.round3);
		round4 = (Button) findViewById(R.id.round4);
		
		upText = (TextView) findViewById(R.id.slideUpText);
		downText = (TextView) findViewById(R.id.slideDownText);
		currentLevel = 1;
		setLevel();
		this.findViewById(android.R.id.content).setOnTouchListener(MyOnTouchListener);
	}
	
	OnTouchListener MyOnTouchListener = new OnTouchListener(){

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			float dx, dy;
			switch(event.getAction()) {
				case(MotionEvent.ACTION_DOWN):
				    onDownX = event.getX();
				    onDownY = event.getY();
				    break;
				case(MotionEvent.ACTION_UP):
				    dx = event.getX() - onDownX;
				    dy = event.getY()-onDownY;
				    
				    // Use dx and dy to determine the direction
				    if(Math.abs(dx) <= Math.abs(dy)) {
				    	setNextLevel(dy>0);
				    }
				    break;	      
			}
			return true;  
		}
	};
	 
	private void setNextLevel(boolean down) {
		if (down && currentLevel < maxLevel)  currentLevel++;
		else if (!down && currentLevel > 1) currentLevel--;
		setLevel();
	}

	private void setLevel(){
		if ( currentLevel == 1) upText.setText("");
		else upText.setText("Slide Up for previous level");
		
		if (currentLevel == maxLevel) { 
			downText.setText("");
			if (maxRound < 1) round2.setVisibility(View.INVISIBLE);
			if (maxRound < 2) round3.setVisibility(View.INVISIBLE);
			if (maxRound < 3) round4.setVisibility(View.INVISIBLE);
		} else {
			downText.setText("Slide down for next level");
			round1.setVisibility(View.VISIBLE);
			round2.setVisibility(View.VISIBLE);
			round3.setVisibility(View.VISIBLE);
			round4.setVisibility(View.VISIBLE);
		}
		
		round1.setText("Start Level: " + currentLevel + " Round: 1");
		round2.setText("Start Level: " + currentLevel + " Round: 2");
		round3.setText("Start Level: " + currentLevel + " Round: 3");
		round4.setText("Start Level: " + currentLevel + " Round: 4");	 
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {
		FragmentManager fm = getFragmentManager();
		
	    if (fm.getBackStackEntryCount() > 0) {
	        fm.popBackStack();
	    } else {
	        super.onBackPressed();  
	    }
	}

    public void onClick(View view) {
    	SharedPreferences.Editor editor = getSharedPreferences("Setting", MODE_PRIVATE).edit();
		editor.putInt("Startlevel", currentLevel);
		
    	if (view.getId() == R.id.round1) {
    		editor.putInt("Startround", 1);
    	}
    	else if (view.getId() == R.id.round2) {
    		editor.putInt("Startround", 2);
    	}
    	else if (view.getId() == R.id.round3) {
    		editor.putInt("Startround", 3);
    	}
    	else if (view.getId() == R.id.round4) {
    		editor.putInt("Startround", 4);
    	}
    	
    	editor.commit();
    	Intent intent = new Intent(view.getContext(), GameActivity.class);
    	startActivityForResult(intent,0);
    }
    
    @Override
	/* Inflate the menu; this adds items to the action bar if it is present. */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}
}