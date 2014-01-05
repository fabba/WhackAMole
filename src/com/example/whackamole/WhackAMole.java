package com.example.whackamole;

import databaseadapter.UserAdapter;
import models.users.UserModel;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class WhackAMole extends Application {
	
	private static Context context;
	private static UserModel user;
	
	public void onCreate() {
		super.onCreate();
		WhackAMole.context = getApplicationContext();
		
		SharedPreferences preferences = getSharedPreferences("Setting", MODE_PRIVATE);
		String userName = preferences.getString("Name", "");
		
		UserAdapter userAdapter = new UserAdapter(WhackAMole.context);
		userAdapter.open();
		WhackAMole.user = userAdapter.getUser(userName);
		userAdapter.close();
	}
	
	public static Context getContext() {
		return WhackAMole.context;
	}
	
	public static UserModel getUser() {
		return user;
	}
	
	public static void setUser(UserModel user) {
		WhackAMole.user = user;
	}
}
