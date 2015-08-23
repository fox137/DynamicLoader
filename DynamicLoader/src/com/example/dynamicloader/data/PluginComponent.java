package com.example.dynamicloader.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PluginComponent {
	public List<String> activityList;
	public List<String> recieverList;
	public List<String> serviceList;
	
	public PluginComponent(Context context, String path) {
		initComponents(context, path);
	}

	private void initComponents(Context context, String path) {
		initActivities(context, path);
		initServices(context, path);
	}
	
	private void initActivities(Context context, String path) {
		activityList = new ArrayList<String>();
		PackageInfo pi = context.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
		for (int i = 0; i < pi.activities.length; i++) {
			activityList.add(pi.activities[i].name);
		}
	}

	private void initServices(Context context, String path) {
		serviceList = new ArrayList<String>();
		PackageInfo pi = context.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_SERVICES);
		for (int i = 0; i < pi.services.length; i++) {
			serviceList.add(pi.services[i].name);
		}
	}

}
