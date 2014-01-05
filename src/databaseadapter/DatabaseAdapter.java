package databaseadapter;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import com.example.whackamole.WhackAMole;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

// With help from: 
// http://pheide.com/page/11/tab/24#post13
// http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
public abstract class DatabaseAdapter {
 
    // Database Version
    private static final int DATABASE_VERSION = 63;
    
    // Database Name
    private static final String DATABASE_NAME = "whackAMole.db";
	
	protected static final String TAG = "DatabaseAdapter";
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;
    private boolean isOpen = false;
    protected final Context mCtx;
	
    protected static class DatabaseHelper extends SQLiteOpenHelper {
    	
    	 //The Android's default system path of your application database.
        private static String DB_PATH = "/data/data/com.example.whackamole/databases/";
     
        private SQLiteDatabase myDataBase; 
     
     
        /**
         * Constructor
         * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
         * @param context
         */
        public DatabaseHelper(Context context) {
        	super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }	
     
        /**
         * Creates a empty database on the system and rewrites it with your own database.
         */
        public void createDataBase() throws IOException{
     
        	boolean dbExist = checkDataBase();
     
        	if (!dbExist) {
        		// By calling this method and empty database will be created into the default system path
                // of your application so we are gonna be able to overwrite that database with our database.
            	this.getReadableDatabase();
            	
            	try {
        			copyDataBase();
        		} catch (IOException e) {
            		throw new Error("Error copying database");
            	}
        	}
        }
        
        private void createDataBase(boolean forced) {
        	if (forced) {
        		//WhackAMole.getContext().deleteDatabase(DATABASE_NAME);
  	
        		//By calling this method and empty database will be created into the default system path
                //of your application so we are gonna be able to overwrite that database with our database.       	
        		try {
					this.createDataBase();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            	
            	try {
        			copyDataBase();
        		} catch (IOException e) {
            		throw new Error("Error copying database");
            	}
        	} else {
        		try {
					createDataBase();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
     
        /**
         * Check if the database already exist to avoid re-copying the file each time you open the application.
         * @return true if it exists, false if it doesn't
         */
        private boolean checkDataBase() {
        	SQLiteDatabase checkDB = null;
     
        	try {
        		String myPath = DB_PATH + DATABASE_NAME;
        		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        	} catch (SQLiteException e) {
        		//Database does not exist yet.
        	}
     
        	if(checkDB != null){
        		checkDB.close();
        	}
     
        	return checkDB != null;
        }
     
        /**
         * Copies your database from your local assets-folder to the just created empty database in the
         * system folder, from where it can be accessed and handled.
         * This is done by transfering bytestream.
         * */
        private void copyDataBase() throws IOException {
        	
        	//Open your local db as the input stream
        	InputStream myInput = WhackAMole.getContext().getAssets().open(DATABASE_NAME);
     
        	// Path to the just created empty db
        	String outFileName = DB_PATH + DATABASE_NAME;
     
        	//Open the empty db as the output stream
        	OutputStream myOutput = new FileOutputStream(outFileName);
     
        	//transfer bytes from the inputfile to the outputfile
        	byte[] buffer = new byte[1024];
        	int length;
        	while ((length = myInput.read(buffer))>0){
        		myOutput.write(buffer, 0, length);
        	}
     
        	//Close the streams
        	myOutput.flush();
        	myOutput.close();
        	myInput.close();
        }
     
        public void openDataBase() throws SQLException{
            String myPath = DB_PATH + DATABASE_NAME;
        	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }
     
        @Override
    	public synchronized void close() {
    	    if (myDataBase != null) {
    		    myDataBase.close();
    	    }
    	    super.close();
    	}
	 
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    	createDataBase(true);
	    }
	    
	    @Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    	ArrayList<Hashtable<String, String>> userContent = UserAdapter.getContent(db);
	    	ArrayList<Hashtable<String, String>> scoreContent = ScoreAdapter.getContent(db);
	    	
	    	onCreate(db);
	    	
	    	ScoreAdapter.addContent(db, scoreContent);
		    UserAdapter.addContent(db, userContent);
	    }
    }
    
    public DatabaseAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DatabaseAdapter open() throws SQLException {
    	dbHelper = new DatabaseHelper(mCtx);
 
        try {
        	dbHelper.createDataBase();
	 	} catch (IOException ioe) {
	 		throw new Error("Unable to create database");
	 	}
	 
	 	try {
	 		dbHelper.openDataBase();
	 	} catch(SQLException sqle) {
	 		throw sqle;
	 	}
	 	
        db = dbHelper.getWritableDatabase();
        isOpen = true;
        return this;
    }

    public boolean checkOpen() {
    	return isOpen;
    }
    
    public void close() {
    	isOpen = false;
        dbHelper.close();
    }
    
    public void printTableNames() {
    	Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type = 'table'", null);
    	
    	while(cursor.moveToNext()) {
    		System.out.println("table " + cursor.getString(1));
    	}
    }
}