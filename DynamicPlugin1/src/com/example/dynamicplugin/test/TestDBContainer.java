package com.example.dynamicplugin.test;

import android.net.Uri;

public class TestDBContainer {
	public static final String URI_STR = "content://" + TestContentProvider.AUTHORITY;
	
	public static class TestTableA{
		public static final String TABLE_NAME = "testa";
		public static final Uri CONTENT_URI = Uri.parse(URI_STR + "/" + TABLE_NAME);
		
		public static final String ID = "id";
		public static final String NAME = "name";
		
		private static StringBuffer sb = new StringBuffer();
		static{
			sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append(" (");
			sb.append(ID).append(" INTEGER,");
			sb.append(NAME).append(" VARCHAR(32)");
			sb.append(");");
		}
		public static String SQL_CREATE_TABLE = sb.toString();
		public static String SQL_DROP_TABLE = (new StringBuffer()).append("DROP TABLE IF EXISTS ").
				append(TABLE_NAME).append(";").toString();
	}
}
