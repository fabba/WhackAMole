package com.example.whackamole;

import java.util.ArrayList;
import java.util.List;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentViewSetting();
	}
	
	private void setContentViewSetting() {
		setContentView(R.layout.activity_setting);
		SeekBar levelSelect = (SeekBar) findViewById(R.id.startLevelSelect);
		levelSelect.setMax(5);
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
   
        SharedPreferences setting = getSharedPreferences("Setting", MODE_PRIVATE);
        startLevel = setting.getInt("Startlevel", 1);
        TextView textProgress = (TextView)findViewById(R.id.showStartLevel);
        textProgress.setText(Integer.toString(startLevel));
   
        levelSelect.setProgress(startLevel);
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

	/* private void setContentViewSelectUser() {
		setContentView(R.layout.select_user);
		GamesTable db = new GamesTable(this);
		db.open();
		this.userNames = db.getUserNames();
		db.close();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, this.userNames);
		this.userListView = (ListView)findViewById(R.id.selectUser);
		this.userListView.setAdapter(adapter);
		
        // ListView Item Click Listener
        this.userListView.setOnItemClickListener(new OnItemClickListener() {

        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		String userName = (String)userListView.getItemAtPosition(position);
        		  
				boolean succes = setUserName(view, userName);
				
				if (succes) {
					setContentViewSetting();
				}
        	}
        });
        
        this.userTextField = (TextView)findViewById(R.id.userTextField);
        
        this.userTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
				
				boolean succes = setUserName(view, view.getText().toString());

				if (succes) {
					setContentViewSetting();
				}
				return true;
			}
		});
	}
	*/
	private boolean setUserName(View view, String userName) {
		if (userName.length() < 1) {
			((TextView)findViewById(R.id.selectUserError)).setText(
					"Username must be atleast 1 letter long.");
			return false;
		}
		
		SharedPreferences.Editor editor = getSharedPreferences("Setting", MODE_PRIVATE).edit();
		editor.putString("name", userName);
		editor.commit();
		return true;
	}
	
	public void addUsers(View v, String userName) {
		this.userNames.add(userName);
		this.adapter.notifyDataSetChanged();
	}

    public void onClick(View view) {
    	if (view.getId() == R.id.save) {
    	
	    	
	        SharedPreferences.Editor editor = getSharedPreferences("Setting", MODE_PRIVATE).edit();
		    editor.putInt("Startlevel", startLevel); // TODO hardcoded magic String, fab
		    editor.commit();
		    
	    	Intent intent = new Intent (view.getContext(), MainActivity.class);
	    	startActivityForResult(intent, 0);
    	}
    	else if (view.getId() == R.id.selectUser) {
    		//setContentViewSelectUser();
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
