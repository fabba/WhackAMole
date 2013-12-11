package databaseadapter;

import java.util.ArrayList;
import java.util.Hashtable;

import com.example.whackamole.GameScene;
import com.example.whackamole.WhackAMole;

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

public class RoundAdapter extends DatabaseAdapter {
    public static final String TABLE_ROUNDS = "rounds";
    public static final String KEY_ID = "_id";
    public static final String KEY_LEVEL_ID = "levelId";
    public static final String KEY_MOLE_ID = "modelId";
    public static final String KEY_TIME = "time";
    public static final String KEY_APPEARANCE_TIME = "appearanceTime";
    
    private static final String[] COLUMNS = {KEY_ID, KEY_LEVEL_ID, KEY_MOLE_ID,
    	KEY_TIME, KEY_APPEARANCE_TIME};
 
    private static final Hashtable<Integer, Class<?>> typeToMoleClass = new Hashtable<Integer, Class<?>>();
    private static final Hashtable<Class<?>, Integer> moleClassToType = new Hashtable<Class<?>, Integer>();
    
    public RoundAdapter() {
    	this(WhackAMole.getContext());
    }
    
    public RoundAdapter(Context context) {
        super(context);
        System.out.println("roundadapter test" + context);
        
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
    
    public void addRound(RoundModel round) {
    	for (MoleModel mole : round.getMoles()) {
	    	ContentValues values = new ContentValues();
	    	values.put(KEY_LEVEL_ID, round.getLevel());
	    	values.put(KEY_MOLE_ID, moleClassToType.get(mole.getClass()));
	    	values.put(KEY_TIME, mole.getTime());
	    	values.put(KEY_APPEARANCE_TIME, mole.getAppearanceTime());
	
    		db.insert(TABLE_ROUNDS, null, values);
    	}
    }
    
    public RoundModel getRound(int numRound, GameScene scene) {
    	
    	printTableNames();
    	
    	Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ROUNDS +
    			" WHERE " + KEY_ID + " = " + numRound, null);
        
    	RoundModel round = null;
    	if (cursor.moveToFirst()) {
    		ArrayList<Class<?>> moleClasses = new ArrayList<Class<?>>();
    		ArrayList<Float> times = new ArrayList<Float>();
    		ArrayList<Float> appearanceTimes = new ArrayList<Float>();
    		
    		Hashtable<String, String> data = new Hashtable<String, String>();
    		
    		do {
	    		for (int i = 0; i < COLUMNS.length; i++) {
	    			data.put(COLUMNS[i], cursor.getString(i));
	    		}
	    		
	    		times.add(Float.valueOf(data.get(KEY_TIME)));
	    		appearanceTimes.add(Float.valueOf(data.get(KEY_APPEARANCE_TIME)));
	    		moleClasses.add(typeToMoleClass.get(Integer.valueOf(data.get(KEY_TIME))));
    		} while (cursor.moveToNext());
    		
    		for (Class<?> c : moleClasses) {
    			System.out.println("Class: " + c);
    		}
    		
    		ArrayList<MoleModel> moles = scene.createMoles(moleClasses, times, appearanceTimes);
    		int level = Integer.valueOf(data.get(KEY_LEVEL_ID));
    		round = new RoundModel(numRound, level, moles);
        }
    	
    	return round;
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
