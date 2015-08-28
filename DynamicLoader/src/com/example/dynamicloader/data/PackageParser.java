package com.example.dynamicloader.data;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.dynamicloader.data.PluginComponentInfo.PluginActivityInfo;
import com.example.dynamicloader.data.PluginComponentInfo.PluginProviderInfo;
import com.example.dynamicloader.data.PluginComponentInfo.PluginServiceInfo;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

/**
 * Parse androidmanifest.xml by android.content.pm.PackageParser
 * @author xufeng
 *
 */
public class PackageParser {

	private static final String TAG = "PackageParser";
	private List<PluginActivityInfo> mActivities = new ArrayList<PluginActivityInfo>();
	private List<PluginActivityInfo> mReceivers = new ArrayList<PluginActivityInfo>();
	private List<PluginServiceInfo> mServices   = new ArrayList<PluginServiceInfo>();
	private List<PluginProviderInfo> mProviders = new ArrayList<PluginProviderInfo>();
	private ApplicationInfo mApplicationInfo;

	public PackageParser() {
	}

	public void parse(Context context, String fileName) {
		Log.d(TAG, "begin");
		try {
			// String fileName = Environment.getExternalStorageDirectory() +
			// File.separator + "dl" + File.separator+"DynamicPlugin.apk";
			Class clazz = Class.forName("android.content.pm.PackageParser");
			Constructor<?> cons = clazz.getConstructor(String.class);
			Object instance = cons.newInstance(fileName);
			Method parsePackageMethod = clazz.getDeclaredMethod("parsePackage", java.io.File.class,
					String.class, DisplayMetrics.class, int.class);
			Object packageInfo = parsePackageMethod.invoke(instance, new File(fileName),
					Environment.getExternalStorageDirectory() + File.separator + "dl"
							+ File.separator, context.getResources().getDisplayMetrics(), 1 << 5);
			Log.d(TAG, "package = " + packageInfo);

			Class packageClazz = Class.forName("android.content.pm.PackageParser$Package");
			Field fa = packageClazz.getDeclaredField("activities");
			Field fr = packageClazz.getDeclaredField("receivers");
			Field fp = packageClazz.getDeclaredField("providers");
			Field fs = packageClazz.getDeclaredField("services");
			Field fapp = packageClazz.getDeclaredField("applicationInfo");
			fa.setAccessible(true);
			fr.setAccessible(true);
			fp.setAccessible(true);
			fs.setAccessible(true);
			fapp.setAccessible(true);
			ArrayList<Object> activities = (ArrayList<Object>) fa.get(packageInfo);
			ArrayList<Object> receivers = (ArrayList<Object>) fr.get(packageInfo);
			ArrayList<Object> providers = (ArrayList<Object>) fp.get(packageInfo);
			ArrayList<Object> services = (ArrayList<Object>) fs.get(packageInfo);
			mApplicationInfo = (ApplicationInfo) fapp.get(packageInfo);
			Log.i(TAG, "ApplicationInfo " + mApplicationInfo + ", name=" + (mApplicationInfo==null ? "null" : mApplicationInfo.className));

			Class activityClazz = Class.forName("android.content.pm.PackageParser$Activity");
			for (Object o : activities) {
				PluginActivityInfo pai = new PluginActivityInfo();
				Field fn = activityClazz.getField("className");
				Field fi = activityClazz.getField("intents");
				Field fif = activityClazz.getField("info");
				fn.setAccessible(true);
				fi.setAccessible(true);
				fif.setAccessible(true);
				pai.name = (String) fn.get(o);
				pai.intents = (ArrayList<IntentFilter>) fi.get(o);
				pai.info = (ActivityInfo) fif.get(o);
				Log.d(TAG, "activity = " + pai);
				mActivities.add(pai);
			}

			for (Object o : receivers) {
				PluginActivityInfo pa = new PluginActivityInfo();
				Field fn = activityClazz.getField("className");
				Field fi = activityClazz.getField("intents");
				Field fif = activityClazz.getField("info");
				fn.setAccessible(true);
				fi.setAccessible(true);
				fif.setAccessible(true);
				pa.name = (String) fn.get(o);
				pa.intents = (ArrayList<IntentFilter>) fi.get(o);
				pa.info = (ActivityInfo) fif.get(o);
				Log.d(TAG, "receiver = " + pa);
				mReceivers.add( pa);
			}

			Class providerClazz = Class.forName("android.content.pm.PackageParser$Provider");
			for (Object o : providers) {
				PluginProviderInfo pa = new PluginProviderInfo();
				Field fn = providerClazz.getField("className");
				Field fi = providerClazz.getField("intents");
				Field fif = providerClazz.getField("info");
				fn.setAccessible(true);
				fi.setAccessible(true);
				fif.setAccessible(true);
				pa.name = (String) fn.get(o);
				pa.intents = (ArrayList<IntentFilter>) fi.get(o);
				pa.info = (ProviderInfo) fif.get(o);
				Log.d(TAG, "provider = " + pa);
				mProviders.add( pa);
			}

			Class serviceClazz = Class.forName("android.content.pm.PackageParser$Service");
			for (Object o : services) {
				PluginServiceInfo pi = new PluginServiceInfo();
				Field fn = serviceClazz.getField("className");
				Field fi = serviceClazz.getField("intents");
				Field fif = serviceClazz.getField("info");
				fn.setAccessible(true);
				fi.setAccessible(true);
				fif.setAccessible(true);
				pi.name = (String) fn.get(o);
				pi.intents = (ArrayList<IntentFilter>) fi.get(o);
				pi.info = (ServiceInfo) fif.get(o);
				Log.d(TAG, "service = " + pi);
				mServices.add(pi);
			}
			Log.d(TAG, "" + mActivities + mProviders + mReceivers + mServices);
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}
	}

	public List<PluginActivityInfo> getActivities() {
		return mActivities;
	}

	public List<PluginActivityInfo> getReceivers() {
		return mReceivers;
	}

	public List<PluginProviderInfo> getProviders() {
		return mProviders;
	}

	public List<PluginServiceInfo> getServices() {
		return mServices;
	}

	public ApplicationInfo getApplicationInfo() {
		return mApplicationInfo;
	}

}
