package databaseadapter;

import java.util.ArrayList;
import java.util.Hashtable;


import com.example.whackamole.GameScene;
import com.example.whackamole.WhackAMole;

import models.levels.LevelModel;
import models.levels.LocationModel;
import models.levels.RoundModel;
import models.moles.BurnyModel;
import models.moles.GoldyModel;
import models.moles.HattyModel;
import models.moles.IcyModel;
import models.moles.MoleModel;
import models.moles.NormyModel;
import models.moles.SmogyModel;
import models.moles.SniffyModel;
import models.moles.SpeedyModel;
import models.moles.TankyModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class LevelAdapter extends DatabaseAdapter {
    public static final String ROUND_TABLE_NAME = "rounds";
    public static final String ROUND_KEY_ID = "_id";
    public static final String ROUND_KEY_ROUND_ID = "roundId";
    public static final String ROUND_KEY_LEVEL_ID = "levelId";
    public static final String ROUND_KEY_MOLE_ID = "modelId";
    public static final String ROUND_KEY_TIME = "time";
    public static final String ROUND_KEY_APPEARANCE_TIME = "appearanceTime";
    
    private static final String[] ROUND_COLUMNS = {ROUND_KEY_ID, ROUND_KEY_ROUND_ID, 
    	ROUND_KEY_LEVEL_ID, ROUND_KEY_MOLE_ID, ROUND_KEY_TIME, ROUND_KEY_APPEARANCE_TIME};
    
    public static final String LOCATION_TABLE_NAME = "locations";
    public static final String LOCATION_KEY_ID = "_id";
    public static final String LOCATION_KEY_LEVEL_ID = "levelId";
    public static final String LOCATION_KEY_X = "x";
    public static final String LOCATION_KEY_Y = "y";
    
    private static final String[] LOCATION_COLUMNS = {LOCATION_KEY_ID, LOCATION_KEY_LEVEL_ID,
    	LOCATION_KEY_X, LOCATION_KEY_Y};
    
    private static final Hashtable<Integer, Class<?>> typeToMoleClass = new Hashtable<Integer, Class<?>>();
    private static final Hashtable<Class<?>, Integer> moleClassToType = new Hashtable<Class<?>, Integer>();
    
    public LevelAdapter() {
    	this(WhackAMole.getContext());
    }
    
    public LevelAdapter(Context context) {
        super(context);
        
        typeToMoleClass.put(6, NormyModel.class);
        typeToMoleClass.put(1, HattyModel.class);
        typeToMoleClass.put(5, TankyModel.class);
        typeToMoleClass.put(2, SniffyModel.class);
        typeToMoleClass.put(4, GoldyModel.class);
        typeToMoleClass.put(3, SpeedyModel.class);
        typeToMoleClass.put(7, BurnyModel.class);
        typeToMoleClass.put(0, IcyModel.class);
        typeToMoleClass.put(8, SmogyModel.class);
        
        for (Integer type : typeToMoleClass.keySet()) {
        	moleClassToType.put(typeToMoleClass.get(type), type);
        }
    }
    
    public void addRound(RoundModel round, LevelModel level) {
    	for (MoleModel mole : round.getMoles()) {
	    	ContentValues values = new ContentValues();
	    	values.put(ROUND_KEY_ROUND_ID, round.getNumRound());
	    	values.put(ROUND_KEY_LEVEL_ID, level.getNumLevel());
	    	values.put(ROUND_KEY_MOLE_ID, moleClassToType.get(mole.getClass()));
	    	values.put(ROUND_KEY_TIME, mole.getTime());
	    	values.put(ROUND_KEY_APPEARANCE_TIME, mole.getAppearanceTime());
	
    		db.insert(ROUND_TABLE_NAME, null, values);
    	}
    }
    
    public RoundModel getRound(int numRound, int numLevel,
    		ArrayList<LocationModel> locations, GameScene scene) {
    	printTableNames();
    	
    	Cursor cursor = db.rawQuery("SELECT * FROM " + ROUND_TABLE_NAME +
    			" WHERE " + ROUND_KEY_ROUND_ID + " = " + numRound + 
    			" AND " + ROUND_KEY_LEVEL_ID + " = " + numLevel, null);
        
    	RoundModel round = null;
    	if (cursor.moveToFirst()) {
    		ArrayList<Class<?>> moleClasses = new ArrayList<Class<?>>();
    		ArrayList<Float> times = new ArrayList<Float>();
    		ArrayList<Float> appearanceTimes = new ArrayList<Float>();
    		
    		Hashtable<String, String> data = new Hashtable<String, String>();
    		
    		do {
	    		for (int i = 0; i < ROUND_COLUMNS.length; i++) {
	    			data.put(ROUND_COLUMNS[i], cursor.getString(i));
	    		}
	    		
	    		times.add(Float.valueOf(data.get(ROUND_KEY_TIME)));
	    		appearanceTimes.add(Float.valueOf(data.get(ROUND_KEY_APPEARANCE_TIME)));
	    		moleClasses.add(typeToMoleClass.get(Integer.valueOf(data.get(ROUND_KEY_MOLE_ID))));
    		} while (cursor.moveToNext());
    		
    		// TODO remove
    		for (Class<?> c : moleClasses) {
    			System.out.println("Class: " + c);
    		}
    		
    		ArrayList<MoleModel> moles = scene.createMoles(moleClasses, times, appearanceTimes, locations);
    		round = new RoundModel(numRound, moles);
        }
    	
    	return round;
    }
    
    public int getNumberOfRounds(int numLevel) {
    	Cursor cursor = db.rawQuery("SELECT " + ROUND_KEY_ROUND_ID + " FROM " + ROUND_TABLE_NAME +
    			" WHERE " + ROUND_KEY_LEVEL_ID + " = " + numLevel, null);
    	
    	int numberOfRounds = 0;
    	int currentNumRound = 0;
    	if (cursor.moveToFirst()) {
    		currentNumRound = cursor.getInt(0);
    		numberOfRounds++;
    	}
    	
    	while (cursor.moveToNext()) {
    		int nextNumRound = cursor.getInt(0);
    		if (currentNumRound != nextNumRound) {
    			currentNumRound = nextNumRound;
        		numberOfRounds++;
    		}
    	}
    	
    	return numberOfRounds;
    }
    
    public ArrayList<LocationModel> getLocations(int numLevel) {
    	Cursor cursor = db.rawQuery("SELECT * FROM " + LOCATION_TABLE_NAME +
    			" WHERE " + LOCATION_KEY_LEVEL_ID + " = " + numLevel, null);
    	
    	ArrayList<LocationModel> locations = new ArrayList<LocationModel>();
    	if (cursor.moveToFirst()) {
    		do {
    			locations.add(new LocationModel(cursor.getInt(2), cursor.getInt(3)));
    		} while(cursor.moveToNext());
    	}
        
    	return locations;
    }
    
    /*
    public void printAllGames() {
    	Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_GAMES, null);
    	if (cursor.moveToFirst()) {
        	do {
        		for (int i = 1; i < COLUMNS.length - 1; i++) {
        			System.out.println(COLUMNS[i] + " : " + cursor.getString(i));
        		}
        	} while (cursor.moveToNext());
        } 
    } 
    */
    /*
    public ArrayList<String> getUserNames() {
    	Cursor cursor = db.rawQuery("SELECT " + KEY_NAME + " FROM " + TABLE_GAMES, null);
    	
    	ArrayList<String> userNames = new ArrayList<String>();
    	
    	if (cursor.moveToFirst()) {
        	do {
        		userNames.add(cursor.getString(0));
        	} while (cursor.moveToNext());
        }
    	
    	return userNames;
    }
    */
}