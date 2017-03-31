package com.example.dharampalsolanki.dharamgo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dharampalsolanki.dharamgo.activity.MainActivity;


public class DBAdapter {
	private static final String TAG = "DBAdapter";
	private static final String DATABASE_NAME = "List_Viewer";
	private static String DATABASE_TABLE;
	private static final int DATABASE_VERSION = 1;
	String defaultUserId = "99999";

	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context){  super(context, "database", null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				   db.execSQL("create table tableObject(itemName text Primary Key,count Integer,endDate text,icon text, uId text)");

        db.execSQL("create table tableDefault(itemName text Primary Key,count Integer,endDate text,icon text, uId text)");

			} catch (SQLException e) {
				e.printStackTrace();
			}


		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			onCreate(db);
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}



	public void insertdata(String itemName,
						   Integer count, String endDate, String icon) {

		ContentValues cv = new ContentValues();
		cv.put("itemName", itemName);
		cv.put("count", count);
		cv.put("endDate", endDate);
		cv.put("icon", icon);

		cv.put("uid", getUserId());



		db.insert(getTableName(), null, cv);

	}



	public Cursor fetchData() {
		Cursor cursor = null;
		try {

			cursor = db.rawQuery("select * from " + getTableName() + " where uId = '"
					+ getUserId() + "'", null);

			if (cursor != null && cursor.getCount()>0) {
				cursor.moveToFirst();
			}
		//	this.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		return cursor;
	}







	public int getProductCount(String itemName) {

		int count = 0;
		try {
			//SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.rawQuery("SELECT count FROM " + getTableName() + " where itemName = '"
					+ itemName +"' AND uId ='" + getUserId() + "'", null);

			if (cursor != null && cursor.getCount()>0) {
				cursor.moveToFirst();




				do {

					count = cursor.getInt(cursor.getColumnIndex("count"));

				} while (cursor.moveToNext());

			//	db.close();

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return count;

	}


	public void updateData(String itemName,
						   Integer count) {
		//SQLiteDatabase data = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		Log.e("itemName", "*****" + itemName);

		Log.e("count", "*****" + count);



		cv.put("count", count);


		db.update(getTableName(), cv, "itemName ='" + itemName +"' AND uId ='" + getUserId() + "'", null);
	//	this.close();
	}

	public void deleteTable() {
		//SQLiteDatabase data = this.getReadableDatabase();
		db.delete("tableLogin" + " where uid = '"
				+ getUserId() + "'", null, null);
		//this.close();
	}

	public void deleteTableDefault() {
		//SQLiteDatabase data = this.getReadableDatabase();
		db.delete("tableDefault"+ " where uid = '"
				+ getUserId() + "'", null, null);
		//this.close();
	}

	public void deleteData(String itemName) {
	//	SQLiteDatabase data = this.getReadableDatabase();
		db.delete(getTableName(), "itemName ='" + itemName + "' AND uId ='" + getUserId() + "'", null);
		//this.close();
	}




	public String getTableName() {

		String tableName = "";



			tableName = "tableObject";


		return tableName;
	}


	public String getUserId() {

		String userId = MainActivity.email;


		return userId;
	}





}
