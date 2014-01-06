package com.example.whackamole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * Activity which allows the user to set which level he/she is going to play.
 */
public class SettingActivity extends Activity {
	
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
    	for (int[] currentLevel : level) {
	    	HashMap<String, String> levels = new HashMap<String, String>(2);
	    	levels.put("Level", " Start level : " + currentLevel[0]);
			levels.put("Round", " Round : " + currentLevel[1]);
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
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				int[] levelRound = level.get(position);
				
				SharedPreferences.Editor editor = getSharedPreferences("Setting", MODE_PRIVATE).edit();
				editor.putInt("Startlevel", levelRound[0]);
				editor.putInt("Startround", levelRound[1]);
				editor.putBoolean("ShouldResume", false);
				editor.commit();
				
				Intent intent = new Intent(view.getContext(), GameActivity.class);
				startActivityForResult(intent,0);
			}
		});
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
}