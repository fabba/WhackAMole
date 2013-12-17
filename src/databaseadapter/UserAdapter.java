package databaseadapter;

import models.users.UserModel;

import com.example.whackamole.WhackAMole;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

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
	
	public UserModel getUser(String name) {
		// TODO test
		
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + 
				KEY_NAME + " = '" + name + "'", null);
		
		UserModel user = null;
		
		if (cursor.moveToFirst()) {
			user = new UserModel(cursor.getInt(0), cursor.getString(1));
		}
		
		return user;
	}
	
	public UserModel getUser(int id) {
		// TODO test
		
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + 
				KEY_ID + " = " + id, null);
		
		UserModel user = null;
		
		if (cursor.moveToFirst()) {
			user = new UserModel(cursor.getInt(0), cursor.getString(1));
		}
		
		return user;
	}
	
	public String getPassword(String name) {
		// TODO test
		Cursor cursor = db.rawQuery("SELECT password FROM " + TABLE_NAME + " WHERE " + 
				KEY_NAME + " = '" + name +"'", null);
		String password = null;
		if (cursor.moveToFirst()) {
			  password = cursor.getString(0);
		}
		return password;
	}
	
	public void addUser(String name, String password) {
		System.out.println(db.toString());
		ContentValues values = new ContentValues();
    	values.put(KEY_NAME, name);
    	values.put(KEY_PASSWORD, password);
    	db.insert(TABLE_NAME, null, values);
    	
    	// TODO remove on final
    	Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +
    			" WHERE " + KEY_NAME + " = '" + name + "'" , null);
		if (cursor.moveToFirst()) {
			do{
				System.out.println(cursor.getInt(0) + " " + cursor.getString(1) + " " + cursor.getString(2));
			} while (cursor.moveToNext());
		}
	}
	
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
}
