package com.example.dynamicloader;

import java.io.File;

import org.w3c.dom.Text;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class PluginConstant {

//	public static final String SP_NAME = "plugin_data";
	public static final String SP_KEY_INSTALL = "install";
	
	public static boolean isInstalled(Context context, String path){
		boolean r = false;
		String name  = getPluginName(path);
		SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		r = sp.getBoolean(SP_KEY_INSTALL, false);
		return r;
	}
	
	public static void setInstalled(Context context, String path){
		String name  = getPluginName(path);
		SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		sp.edit().putBoolean(SP_KEY_INSTALL, true).apply();
	}
	
	public static  String getPluginName(String path){
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		String s[] = path.split(File.separator);
		if (s == null || s.length == 0) {
			return null;
		}
		return s[s.length-1];
	}
}
