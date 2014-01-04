package com.example.whackamole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.whackamole.R;

import databaseadapter.LevelAdapter;
import databaseadapter.ScoreAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ScoreActivity extends Activity {

	// List of lists where the indices represent the levels
	// and the content of the inner list the rounds.
	private ArrayList<ArrayList<Integer>> levelAndRoundNumbers;
	
	private List<Map<String, String>> data;
	private float onDownX, onDownY;
	private int currentLevel, currentRound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
		
		LevelAdapter levelAdapter = new LevelAdapter(this);
		levelAdapter.open();
		this.levelAndRoundNumbers = levelAdapter.getLevelAndRoundNumbers();
		levelAdapter.close();
		
		// load scores for the first level and first round
		loadScores(0, 0);
		
		OnTouchListener myOnTouchListener = new OnTouchListener() {

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
				        dy = event.getY() - onDownY;
				        
				        // Use dx and dy to determine the direction
				        if(Math.abs(dx) <= Math.abs(dy)) {
				        	loadNextScores(dy > 0);
				        }
				        break;	      
				}
				
				return true;  
			}		
		};
		
		this.findViewById(android.R.id.content).setOnTouchListener(myOnTouchListener);
	}
	
	/**
	 * Fill the score list with new scores, isUp gives the direction.
	 * @param isUp true if next scores should be an increment.
	 */
	public void loadNextScores(boolean isUp) {
		ArrayList<Integer> roundNumbers = this.levelAndRoundNumbers.get(this.currentLevel);
		
		if (isUp) {	
			if (this.currentRound + 1 < roundNumbers.size()) {
				loadScores(this.currentLevel, this.currentRound + 1);
			} 
			else if (this.currentLevel + 1 < this.levelAndRoundNumbers.size()) {
				loadScores(this.currentLevel + 1, this.currentRound);
			}
		} else {
			if (this.currentRound > 0) {
				loadScores(this.currentLevel, this.currentRound - 1);
			} 
			else if (this.currentLevel > 0) {
				loadScores(this.currentLevel - 1, this.currentRound);
			}
		}
	}
	
	/**
	 * Load scores for level and round from database, and present in view.
	 * @param level level to load scores for.
	 * @param round round to load scores for.
	 */
	private void loadScores(int level, int round) {
		this.currentLevel = level;
		this.currentRound = round;

		TextView levelText = (TextView)findViewById(R.id.levelText);
		levelText.setText("Level: " + (level + 1)); // counting from 1, not 0.
		TextView roundText = (TextView)findViewById(R.id.roundText);
		roundText.setText("Round: " + this.levelAndRoundNumbers.get(level).get(round));
		
		ScoreAdapter db = new ScoreAdapter(this);
		db.open();
		data = db.getPoints(level + 1, this.levelAndRoundNumbers.get(level).get(round), 10);
		db.close();
		
		ListView score_list= (ListView) findViewById(R.id.listView1);
		SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"Name", "Score"},
                new int[] {android.R.id.text1,
                           android.R.id.text2});
		score_list.setAdapter(adapter);
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
