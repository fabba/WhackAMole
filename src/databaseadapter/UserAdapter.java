package databaseadapter;

import java.util.ArrayList;
import java.util.Hashtable;

import models.users.UserModel;

import com.example.whackamole.WhackAMole;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database adapter for accessing the user data.
 */
public class UserAdapter extends DatabaseAdapter {

	public static final String TABLE_NAME = "users";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "password";
    
    private static final String[] COLUMNS = {KEY_ID, KEY_NAME, KEY_PASSWORD};
	
	public UserAdapter() {
		this(WhackAMole.getContext());
	}
	
	public UserAdapter(Context ctx) {
		super(ctx);
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
	
	public ArrayList<UserModel> getUsers() {
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
		
		ArrayList<UserModel> users = new ArrayList<UserModel>();
		
		if (cursor.moveToFirst()) {
			do {
				users.add(new UserModel(cursor.getInt(0), cursor.getString(1)));
			} while (cursor.moveToNext());
		}
		
		return users;
	}
	
	public UserModel getUser(String name) {
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + 
				KEY_NAME + " = '" + name + "'", null);
		
		UserModel user = null;
		
		if (cursor.moveToFirst()) {
			user = new UserModel(cursor.getInt(0), cursor.getString(1));
		}
		
		return user;
	}
	
	public UserModel getUser(int id) {
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + 
				KEY_ID + " = " + id, null);
		
		UserModel user = null;
		
		if (cursor.moveToFirst()) {
			user = new UserModel(cursor.getInt(0), cursor.getString(1));
		}
		
		return user;
	}
	
	/**
	 * Get the password belonging to user with name name.
	 * @param name
	 * @return 
	 */
	public String getPassword(String name) {
		Cursor cursor = db.rawQuery("SELECT password FROM " + TABLE_NAME + " WHERE " + 
				KEY_NAME + " = '" + name +"'", null);
		
		String password = null;
		
		if (cursor.moveToFirst()) {
			  password = cursor.getString(0);
		}
		
		return password;
	}
	
	/**
	 * Add a user with name and password to the database.
	 * @param name
	 * @param password
	 */
	public void addUser(String name, String password) {
		ContentValues values = new ContentValues();
    	values.put(KEY_NAME, name);
    	values.put(KEY_PASSWORD, password);
    	db.insert(TABLE_NAME, null, values);
	}
	
	/**
	 * Get the last level and round the user has played
	 * @param user
	 * @return array containing a level, round number pair.
	 */
	public int[] getLastLevelAndRound(UserModel user) {
		int numRound = -1;
		int numLevel = -1;
		
		Cursor cursor = db.rawQuery(
				"SELECT " + ScoreAdapter.KEY_LEVEL_ID + ", " + ScoreAdapter.KEY_ROUND_ID +
				" FROM " + ScoreAdapter.TABLE_NAME +
				" WHERE " + ScoreAdapter.KEY_USER_ID + " = " + user.getId() +
				" ORDER BY " + ScoreAdapter.KEY_LEVEL_ID + " DESC, " +
				ScoreAdapter.KEY_ROUND_ID + " DESC LIMIT 1", null);
		
		if (cursor.moveToFirst()) {
			numLevel = cursor.getInt(0);
			numRound = cursor.getInt(1);
		}
		
		return new int[] {numLevel, numRound};
	}
	
	/**
	 * Get all the levels and rounds the user has played.
	 * @param user
	 * @return list containing level, round number pairs.
	 */
	public ArrayList<int[]> getAllLevels(UserModel user) {
    	Cursor cursor = db.rawQuery(
    			"SELECT " + ScoreAdapter.KEY_LEVEL_ID + ", " + ScoreAdapter.KEY_ROUND_ID + 
    			" FROM " + ScoreAdapter.TABLE_NAME +
    			" WHERE " + ScoreAdapter.KEY_USER_ID + " = " + user.getId(), null);
        
    	ArrayList<int[]> allLevels = new ArrayList<int[]>();
    	while (cursor.moveToNext()) {
    		allLevels.add(new int[] {cursor.getInt(0), cursor.getInt(1)});
        }
    	
    	return allLevels;
    }
}
