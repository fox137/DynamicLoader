package com.example.dynamicplugin.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TestSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "testdb";
	private static final int VERSION = 1;
	
	public TestSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		createAllTables(db);

	}

	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropAllTables(db);
		createAllTables(db);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropAllTables(db);
		createAllTables(db);
	}


	private void createAllTables(SQLiteDatabase db) {
		db.execSQL(TestDBContainer.TestTableA.SQL_CREATE_TABLE);
	}

	
	private void dropAllTables(SQLiteDatabase db) {
		db.execSQL(TestDBContainer.TestTableA.SQL_DROP_TABLE);
	}

}
