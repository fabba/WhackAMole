package com.example.whackamole;

import com.example.whackamole.GameActivity;
import com.example.whackamole.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

/**
 * Activity containing the main menu of the game.
 */
public class MainActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void onClick(View view) {
		if (view.getId() == R.id.newGame) {
			Intent intent = new Intent(view.getContext(), GameActivity.class);
	    	GameScene.shouldResume(false);
	    	SharedPreferences.Editor editor = getSharedPreferences("Setting", MODE_PRIVATE).edit();
			editor.putInt("Startlevel", 1);
			editor.putInt("Startround", 1);
			editor.commit();
			startActivityForResult(intent,0);
		}
		else if (view.getId() == R.id.resume) {
	    	Intent intent = new Intent(view.getContext(), GameActivity.class);
	    	GameScene.shouldResume(true);
	    	startActivityForResult(intent,0);
		}
		else if (view.getId() == R.id.setting) {
	    	Intent intent = new Intent(view.getContext(), SettingActivity.class);
	    	startActivityForResult(intent,0);
		}
		else if (view.getId() == R.id.score) {
	    	Intent intent = new Intent(view.getContext(), ScoreActivity.class);
	    	startActivityForResult(intent,0);
		}
		else if (view.getId() == R.id.manual) {
	    	Intent intent = new Intent(view.getContext(), ManualActivity.class);
	    	startActivityForResult(intent,0);
		}
	}
}


