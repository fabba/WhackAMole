package databaseadapter;

import models.users.UserModel;

import com.example.whackamole.WhackAMole;

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
				KEY_NAME + " = " + name, null);
		
		UserModel user = null;
		
		if (cursor.moveToFirst()) {
			user = new UserModel(cursor.getInt(0), cursor.getString(1));
		}
		
		return user;
	}
	
	public void addUser(String name, String password) {
		// TODO implement
	}
}
