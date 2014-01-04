package com.example.whackamole;

import android.app.Application;
import android.content.Context;

public class WhackAMole extends Application {
	
	private static Context context;
	
	public void onCreate() {
		super.onCreate();
		WhackAMole.context = getApplicationContext();
	}
	
	public static Context getContext() {
		return WhackAMole.context;
	}
}
