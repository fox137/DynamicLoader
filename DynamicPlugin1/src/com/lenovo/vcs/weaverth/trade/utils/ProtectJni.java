package com.lenovo.vcs.weaverth.trade.utils;

import com.example.dynamicplugin.BasePluginApplication;

import android.os.Environment;
import android.util.Log;

public class ProtectJni {
	
	static{
		try {
		
			System.load("/data/data/" + BasePluginApplication.context.getPackageName()+"/lib/libprotect.so");
//			System.load("/data/data/com.example.dynamicloader/app_pluginlib/libprotect.so");
//			System.loadLibrary("protect");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static native String alpk();
	public static native String alpbk();

}
