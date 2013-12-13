package com.example.whackamole;

import java.util.ArrayList;
import java.util.List;

import models.users.UserModel;

import com.example.whackamole.R;
import com.example.whackamole.R.id;
import com.example.whackamole.R.layout;
import com.example.whackamole.R.menu;

import databaseadapter.ScoreAdapter;
import databaseadapter.UserAdapter;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;

public class SettingActivity extends Activity implements OnSeekBarChangeListener{
	
	private ArrayAdapter<String> adapter;
	private ArrayList<String> userNames;
	private ListView userListView;
	private TextView userTextField;
	private int startLevel;
	private int startRound;
	
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
		user.close();
		ScoreAdapter score = new ScoreAdapter(this);
		score.open();
		int[] level = score.getLevel(userName);
		score.close();
		SeekBar levelSelect = (SeekBar) findViewById(R.id.startLevelSelect);
		levelSelect.setMax(level[0]);
		levelSelect.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                // TODO Auto-generated method stub
            	TextView textProgress = (TextView)findViewById(R.id.showStartLevel);
            	textProgress.setText(Integer.toString(progress));
                startLevel = progress;

            }
        });
   
        
        startLevel = setting.getInt("Startlevel", 1);
        TextView textProgress = (TextView)findViewById(R.id.showStartLevel);
        textProgress.setText(Integer.toString(startLevel));
   
        levelSelect.setProgress(startLevel);
        
        SeekBar roundSelect = (SeekBar) findViewById(R.id.startRoundSelect);
		roundSelect.setMax(level[1]);
		roundSelect.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                // TODO Auto-generated method stub
            	TextView textProgress = (TextView)findViewById(R.id.showStartRound);
            	textProgress.setText(Integer.toString(progress));
                startRound = progress;

            }
        });
   
        
        startRound = setting.getInt("Startround", 1);
        TextView textProgressRound = (TextView)findViewById(R.id.showStartRound);
        textProgressRound.setText(Integer.toString(startRound));
   
        roundSelect.setProgress(startRound);
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
    	if (view.getId() == R.id.save) {
    	
	    	
	        SharedPreferences.Editor editor = getSharedPreferences("Setting", MODE_PRIVATE).edit();
		    editor.putInt("Startlevel", startLevel); // TODO hardcoded magic String, fab\
		    editor.putInt("Startround", startRound); // TODO hardcoded magic String, fab
		    editor.commit();
		    
	    	Intent intent = new Intent (view.getContext(), MainActivity.class);
	    	startActivityForResult(intent, 0);
    	}
    }
    @Override
	/* Inflate the menu; this adds items to the action bar if it is present. */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}
/*	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Context context = getBaseContext();

		Intent intent = new Intent();
		switch (item.getItemId()) {
	        case R.id.home:
	        	intent = new Intent (context, MainActivity.class);
	        	break;
	            
	        case R.id.score:
	        	intent = new Intent (context, Score.class);
	        	break;
	            
	        case R.id.game:
	        	intent = new Intent (context, Game.class);
	        	SharedPreferences.Editor editor = getSharedPreferences("new", MODE_PRIVATE).edit();
			    editor.putBoolean("new", true); 
			    editor.commit();
		    	break;
	        
			case R.id.resume:
	        	intent = new Intent (context, Game.class);
	        	editor = getSharedPreferences("new", MODE_PRIVATE).edit();
			    editor.putBoolean("new", false); 
			    editor.commit();
	        	break;
	    }
		startActivityForResult(intent, 0);
		return false;
	}*/

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (seekBar == (SeekBar) findViewById(R.id.startLevelSelect)){
			TextView textProgress = (TextView)findViewById(R.id.showStartLevel);
        	startLevel = progress;
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
}
