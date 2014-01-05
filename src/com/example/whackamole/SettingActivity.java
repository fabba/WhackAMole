package com.example.whackamole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.users.UserModel;

import com.example.whackamole.R;

import databaseadapter.UserAdapter;

import android.R.integer;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
	
		UserAdapter userAdapter = new UserAdapter(this);
		userAdapter.open();
		final ArrayList<int[]> level = userAdapter.getAllLevels(WhackAMole.getUser());
		userAdapter.close();

    	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    	for( int[] currentLevel : level){
	    	HashMap<String, String> levels = new HashMap<String, String>(2);
	    	levels.put("Level", " Start level : " + currentLevel[0]);
			levels.put("Round", " Round : " + currentLevel[1]);
			System.out.println(" Level : " + currentLevel[0] + " and round : " + currentLevel[1]);
					
			data.add(levels);
    	}
    	
		final ListView score_list= (ListView) findViewById(R.id.levelSelector);
		SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"Level", "Round"},
                new int[] {android.R.id.text1,
                           android.R.id.text2});
		score_list.setAdapter(adapter);
		score_list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			    {
			      int[] levelRound = level.get(position);
			      System.out.println(levelRound[1]);
			      SharedPreferences.Editor editor = getSharedPreferences("Setting", MODE_PRIVATE).edit();
			      editor.putInt("Startlevel", levelRound[0]);
			      editor.putInt("Startround", levelRound[1]);
		    	  editor.commit();
	    		  Intent intent = new Intent(view.getContext(), GameActivity.class);
			      startActivityForResult(intent,0);
			    }});

		
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
    @Override
	/* Inflate the menu; this adds items to the action bar if it is present. */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}
}