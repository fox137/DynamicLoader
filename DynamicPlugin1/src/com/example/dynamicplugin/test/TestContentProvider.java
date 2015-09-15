package com.example.dynamicplugin.test;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class TestContentProvider extends ContentProvider {
	
	private static final String TAG = "TestContentProvider";
	public static final Uri CONTENT_URI = Uri.parse("");
	public static final String AUTHORITY  = "com.example.dynamicplugin";
	private static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.example.dynamicplugin.test";
	
	private static final int URI_MATCHER_TESTA = 1;
	private SQLiteOpenHelper mSqLiteOpenHelper;
	
	public static UriMatcher URI_MATCHER;
	static{
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(AUTHORITY, TestDBContainer.TestTableA.TABLE_NAME, URI_MATCHER_TESTA);
	}

	@Override
	public boolean onCreate() {
		mSqLiteOpenHelper = new TestSQLiteOpenHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mSqLiteOpenHelper.getReadableDatabase();
		String table = switchTable(uri);
		if (db == null || table == null) {
			return null;
		}
		Cursor cursor = null;
		try {
			cursor = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Query SQL error:" + e.getMessage(), e);
		} catch (SQLiteException e) {
			Log.e(TAG, "Query database error!", e);
		} catch (SQLException e) {
			Log.e(TAG, "Query fail!", e);
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return CONTENT_TYPE;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mSqLiteOpenHelper.getWritableDatabase();
		String table = switchTable(uri);
		if (db == null || table == null) {
			return null;
		}
		try {
			long r = db.insert(table, null, values);
			return r>0 ? uri : null;
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "SQL error:" + e.getMessage(), e);
		} catch (SQLiteException e) {
			Log.e(TAG, "database error!", e);
		} catch (SQLException e) {
			Log.e(TAG, "fail!", e);
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mSqLiteOpenHelper.getWritableDatabase();
		String table = switchTable(uri);
		if (db == null || table == null) {
			return -1;
		}
		int r = -1;
		try {
			r = db.delete(table, selection, selectionArgs);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "SQL error:" + e.getMessage(), e);
		} catch (SQLiteException e) {
			Log.e(TAG, "database error!", e);
		} catch (SQLException e) {
			Log.e(TAG, "fail!", e);
		}
		return r;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mSqLiteOpenHelper.getWritableDatabase();
		String table = switchTable(uri);
		if (db == null || table == null) {
			return -1;
		}
		int r = -1;
		try {
			r = db.update(table, values, selection, selectionArgs);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "SQL error:" + e.getMessage(), e);
		} catch (SQLiteException e) {
			Log.e(TAG, "database error!", e);
		} catch (SQLException e) {
			Log.e(TAG, "fail!", e);
		}
		return r;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		SQLiteDatabase db = mSqLiteOpenHelper.getWritableDatabase();
		String table = switchTable(uri);
		if (db == null || table == null) {
			return -1;
		}
		int r = 0;
		try {
			db.beginTransaction();
			for (ContentValues contentValues : values) {
				if (insert(uri, contentValues) != null) {
					r++;
				}
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		return r;
	}
	
	
	
	private String switchTable(Uri uri){
		String table = null;
		switch (URI_MATCHER.match(uri)) {
		case URI_MATCHER_TESTA:
			table = TestDBContainer.TestTableA.TABLE_NAME;
			break;

		default:
			break;
		}
		return table;
	}

}
