package com.example.dynamicloader.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.dynamicloader.data.PluginComponentInfo.PluginActivityInfo;
import com.example.dynamicloader.data.PluginComponentInfo.PluginProviderInfo;
import com.example.dynamicloader.data.PluginComponentInfo.PluginServiceInfo;

import dalvik.system.DexClassLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Components Operation 
 * @author xufeng
 *
 */
public class PluginComponent {
	public static final String ACTION_MAIN = "android.intent.action.MAIN";

	private static final String TAG = "PluginComponent";

	public List<PluginActivityInfo> mActivities = new ArrayList<PluginActivityInfo>();
	public List<PluginActivityInfo> mReceivers = new ArrayList<PluginActivityInfo>();
	public List<PluginServiceInfo> mServices = new ArrayList<PluginServiceInfo>();
	public List<PluginProviderInfo> mProviders = new ArrayList<PluginProviderInfo>();
	
	public Set<BroadcastReceiver> mRegisterReceivers = new HashSet<BroadcastReceiver>();


	public PluginComponent(Context context, String path, DexClassLoader classLoader) {
		initComponents(context, path, classLoader);
	}


	private void initComponents(Context context, String path, DexClassLoader classLoader) {
		PackageParser pp = new PackageParser();
		pp.parse(context, path);
		mActivities = pp.getActivities();
		mReceivers = pp.getReceivers();
		mServices = pp.getServices();
		mProviders = pp.getProviders();
		registerReceivers(context.getApplicationContext(), classLoader);
	}

	private void registerReceivers(Context context, DexClassLoader classLoader) {
		try {
			for (PluginActivityInfo pi : mReceivers) {
				Class clazz = classLoader.loadClass(pi.name);
				BroadcastReceiver receiver = (BroadcastReceiver) clazz.newInstance();
				for (IntentFilter filter : pi.intents) {
					for (int j = 0; j < filter.countActions(); j++) {
						context.registerReceiver(receiver, filter);
						mRegisterReceivers.add(receiver);
						Log.i(TAG, "registerReceiver " + pi.name + ", " + filter);
					}
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}
	}
	
	public void unregisterReceivers(Context context) {
		for (BroadcastReceiver receiver : mRegisterReceivers) {
			context.unregisterReceiver(receiver);
		}
	}

	public String getActivityByAction(String action) {
		for (PluginActivityInfo pi : mActivities) {
			for (IntentFilter filter : pi.intents) {
				for (int j = 0; j < filter.countActions(); j++) {
					if (filter.getAction(j).equals(action)) {
						return pi.name;
					}
				}
			}
		}
		return "";
	}


}
