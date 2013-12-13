package com.example.whackamole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import models.levels.LevelModel;
import models.users.UserModel;

import com.example.whackamole.R;
import com.example.whackamole.R.id;
import com.example.whackamole.R.layout;
import com.example.whackamole.R.menu;


import databaseadapter.ScoreAdapter;
import databaseadapter.UserAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ScoreActivity extends Activity {

	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
		viewDatabase();
	}
	
	private void viewDatabase() {
		ScoreAdapter db = new ScoreAdapter(this);
		db.open();
		data = db.getAllPoints();
		ListView score_list= (ListView) findViewById(R.id.listView1);
		SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"Name", "Score"},
                new int[] {android.R.id.text1,
                           android.R.id.text2});
		score_list.setAdapter(adapter);
		db.close();
	}
	

	@Override
	public void onBackPressed() {
		Context context = getBaseContext();
	    Intent intent = new Intent (context, MainActivity.class);
	    startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score, menu);
		return true;
	}

}
