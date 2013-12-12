package databaseadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.whackamole.WhackAMole;

import models.levels.LevelModel;
import models.levels.RoundModel;
import models.users.UserModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ScoreAdapter extends DatabaseAdapter {
	
	// id (int) | userId (int) | score (int)
    public static final String TABLE_NAME = "scores";
    public static final String KEY_ID = "_id";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_SCORE = "score";
    public static final String KEY_LEVEL_ID = "levelId";
    public static final String KEY_ROUND_ID = "roundId";
    
    private static final String[] COLUMNS = {KEY_ID, KEY_USER_ID, KEY_SCORE,
    	KEY_LEVEL_ID, KEY_ROUND_ID};
 
    public ScoreAdapter() {
    	this(WhackAMole.getContext());
    }
    
    public ScoreAdapter(Context context) {
        super(context);
    }
    
    public boolean addScore(int score, UserModel user, LevelModel level) {
    	int previousScore = getScore(user, level);
    	
    	if (score > previousScore) {
	    	ContentValues values = new ContentValues();
	    	values.put(KEY_USER_ID, user.getId());
	    	values.put(KEY_LEVEL_ID, level.getNumLevel());
	    	values.put(KEY_ROUND_ID, level.getNumRound());
	    	values.put(KEY_SCORE, score);
	    	
	    	if (previousScore == -1) {
	    		db.insert(TABLE_NAME, null, values);
	    	} else {
	    		String[] whereArgs = {String.valueOf(user.getId()),
	    				String.valueOf(level.getNumRound()),
	    				String.valueOf(level.getNumLevel())};

	    		db.update(TABLE_NAME, values,
	    				KEY_USER_ID + " = ? AND " + KEY_ROUND_ID + " = ? AND " + KEY_LEVEL_ID + " = ?",
	    				whereArgs);
	    	}
			return true;
    	} else {
    		return false;
    	}
    }
    
    public int getScore(UserModel user, LevelModel level) {
    	Cursor cursor = db.rawQuery("SELECT " + KEY_SCORE + " FROM " + TABLE_NAME +
    			" WHERE " + KEY_ROUND_ID + " = " + level.getNumRound() + 
    			" AND " + KEY_USER_ID + " = " + user.getId() +
    			" AND " + KEY_LEVEL_ID + " = " + level.getNumLevel(), null);
        
    	int score = -1;
    	if (cursor.moveToFirst()) {
			score = cursor.getInt(0);
        }
    	
    	return score;
    }
    
    public List<Map<String, String>> getAllPoints() {
    	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    	
    	Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME + " order by " + KEY_SCORE + " desc LIMIT 10", null);
    	if (cursor.moveToFirst()) {
        	do{ 
        		HashMap<String, String> scores = new HashMap<String, String>(2);
        		scores.put("Name", cursor.getString(1));
      		    scores.put("Score", cursor.getString(4));
      		    data.add(scores);
        	} while (cursor.moveToNext());
        } 
      return data;
    }
}
