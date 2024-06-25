package com.infinityicon.mobilenews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataAccess {
	static final String TAG = "DataAccess";

	public static final String DB_NAME = "cpn";
	public static final int DB_VERSION = 1;
	public static final String DB_TABLE = "news";
	public static final String DB_TABLE2 = "updated";

	public static final String N_ID = "_id";
	public static final String N_TITLE = "title";
	public static final String N_DATE = "date";
	public static final String N_IMAGE = "image";
	public static final String N_SUMMARY = "summary";
	public static final String N_CONTENT = "content";

	public static final String U_ID = "_id";
	public static final String U_DATE = "date";
	public static final String U_SUCCESS = "success";

	Context context;
	DBHelper dbHelper;
	SQLiteDatabase db;
	
	public DataAccess(Context context ) {
		//super();
		this.context = context;
		dbHelper = new DBHelper ( );
	}
	
	public Cursor GetAllNews ( ) {
		db = dbHelper.getReadableDatabase();
		return db.query(DB_TABLE, null, null, null, null, null, null);
	}
	
	public Cursor GetNewsByTitle ( String strTitle ) {
		db = dbHelper.getReadableDatabase();
		return db.query(DB_TABLE, null, "title LIKE '" + strTitle + "'", null, null, null, null);
	}
	
	public void InsertNews ( News news ) {
		db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues ( );
		values.put(N_ID, news.getiID());
		values.put(N_TITLE, news.getStrTitle());
		values.put(N_DATE, news.getStrDate());
		values.put(N_IMAGE, news.getStrImage());
		values.put(N_SUMMARY, news.getStrSummary());
		values.put(N_CONTENT, news.getStrContent());
		
		Log.d ( TAG, "Inserting 1st table entries");
		db.insert ( DB_TABLE, null, values );
	}
	
	public void InsertUpdated ( String strDate ) {
		db = dbHelper.getWritableDatabase();
		Log.d ( TAG, "Inserting 2nd table entries");
		
		ContentValues values2 = new ContentValues ( );

		values2.put(U_ID, 1);
		values2.put(U_DATE, strDate );
		values2.put(U_SUCCESS, 1);
		db.insert(DB_TABLE2, null, values2);
	}
	
	public void ZapNews ( ) {
		Log.d ( TAG, "Zap News" );
		db = dbHelper.getWritableDatabase ( );
		db.execSQL ( String.format ( "DELETE FROM %s", DB_TABLE ) );
	}
	
	public void ZapUpdated ( ) {
		Log.d ( TAG, "Zap Updated" );
		db = dbHelper.getWritableDatabase ( );
		db.execSQL ( String.format ("DELETE FROM %s", DB_TABLE2 ) );
	}

	public void DestoryDB ( ) {
		dbHelper.close();
	}
	class DBHelper extends SQLiteOpenHelper {

		public DBHelper() {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "onCreate: 1st Table");
			db.execSQL(String.format("CREATE TABLE %s ( %s int PRIMARY KEY, "
					+ "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT )",
					DB_TABLE, N_ID, N_TITLE, N_DATE, N_IMAGE, N_SUMMARY,
					N_CONTENT));
			Log.d ( TAG, "onCreate: 2nd Table");
			
			db.execSQL(String.format("CREATE TABLE %s "
					+ "( %s int PRIMARY KEY, %s TEXT, %s int )", DB_TABLE2, U_ID,
					U_DATE, U_SUCCESS));
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP IF EXISTS " + DB_TABLE);
			db.execSQL("DROP IF EXISTS " + DB_TABLE2);
			onCreate(db);
		}
	}
}
