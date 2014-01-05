package databaseadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.example.whackamole.WhackAMole;

import models.levels.LevelModel;
import models.users.UserModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * DataBase adapter which grants access to the scores.
 */
public class ScoreAdapter extends DatabaseAdapter {
	
	public static final String TABLE_NAME = "scores";
    public static final String KEY_ID = "_id";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_SCORE = "score";
    public static final String KEY_LEVEL_ID = "levelId";
    public static final String KEY_ROUND_ID = "roundId";
    
    private static final String[] COLUMNS = {KEY_ID, KEY_USER_ID,
    	KEY_LEVEL_ID, KEY_ROUND_ID, KEY_SCORE};
 
    public ScoreAdapter() {
    	this(WhackAMole.getContext());
    }
    
    public ScoreAdapter(Context context) {
        super(context);
    }
    
    /**
     * Add a new score to the scores table if score does not already exist
     * for that level and round. If a score already exists and this score is
     * lower than the new score, update.
     * @param score score to enter in scores table.
     * @param user the user which scored.
     * @param level the level on which was scored (currentRound determines the round).
     * @return true if success, false otherwise.
     */
    public boolean addScore(int score, UserModel user, LevelModel level) {
    	int previousScore = getScore(user, level);
    	
    	if (score > previousScore) {
	    	ContentValues values = new ContentValues();
	    	values.put(KEY_USER_ID, user.getId());
	    	values.put(KEY_LEVEL_ID, level.getNumLevel());
	    	values.put(KEY_ROUND_ID, level.getNumRound());
	    	values.put(KEY_SCORE, score);
	    	
	    	if (previousScore == -1 ) {
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
    
    /**
     * Get score for the user on the current round of the level.
     * @param user user for which to get the score.
     * @param level level and round for which to get the score.
     * @return the score, -1 if non found.
     */
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
    
    /**
     * Get a list of maps mapping from 'Name' to user name and
     * 'Score' to score for a specific level and round pair, where num
     * limits the number of entries returned. 
     * @param level
     * @param round
     * @param num
     * @return
     */
    public List<Map<String, String>> getPoints(int level, int round, int num) {
    	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    	
    	Cursor cursor = db.rawQuery("SELECT " + KEY_SCORE + ", " + KEY_USER_ID + 
    			" FROM " + TABLE_NAME +
    			" WHERE " + KEY_LEVEL_ID + " = " + level +
    			" AND " + KEY_ROUND_ID + " = " + round +
    			" order by " + KEY_SCORE + " desc LIMIT " + num, null);
    	
    	while (cursor.moveToNext()) {
    		HashMap<String, String> scores = new HashMap<String, String>(2);
    		Cursor cursorUser = db.rawQuery("SELECT " + UserAdapter.KEY_NAME +
    				" FROM " + UserAdapter.TABLE_NAME +
    				" WHERE " + UserAdapter.KEY_ID + " = " + cursor.getInt(1), null);
  
        	while (cursorUser.moveToNext()) {
            	scores.put("Name", cursorUser.getString(0));
        		scores.put("Score", cursor.getString(0));
            }
    		
  		    data.add(scores);
        }
    	
    	return data;
    }
    
    /**
     * Get the entire 'content' of the score database table as a list of
     * Hashtables mapping from columns to values, one Hashtable thus
     * represents one row.
     * @param db the database to get the content from
     * @return
     */
    public static ArrayList<Hashtable<String, String>> getContent(SQLiteDatabase db) {
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
		
		ArrayList<Hashtable<String, String>> content = new ArrayList<Hashtable<String, String>>();
		
		while (cursor.moveToNext()) {
			Hashtable<String, String> contentRow = new Hashtable<String, String>();
			
			for (int i = 0; i < COLUMNS.length; i++) {
				contentRow.put(COLUMNS[i], cursor.getString(i));
			}
			
			content.add(contentRow);
		}
		
		return content;
	}
    
    /**
     * Add content to the database. Content should exist of a list of Hashtables
     * where each Hashtable represents one row such that every key is a column
     * in the database table.
     * Ids are maintained, as these serve as unique entries in the database
     * are updated.
     * @param db
     * @param content
     */
    public static void addContent(SQLiteDatabase db, ArrayList<Hashtable<String, String>> content) {
		ContentValues values = new ContentValues();
		
		for (Hashtable<String, String> contentRow : content) {	
			for (String column : COLUMNS) {
				values.put(column, contentRow.get(column));
			}
			
			if (db.insert(TABLE_NAME, null, values) == -1) {
	    		db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]{contentRow.get(KEY_ID)});
			}
		}
	}
    
    public void printAll(){
    	Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME , null);
    	
    	if (cursor.moveToFirst()) {
        	do { 
        		System.out.println(cursor.getInt(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3) + " " + cursor.getString(4));
        	} while (cursor.moveToNext());
    	}
    }
}
