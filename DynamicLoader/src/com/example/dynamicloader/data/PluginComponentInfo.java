package com.example.dynamicloader.data;

import java.util.List;

import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;

public class PluginComponentInfo {
	public String name;
	public List<IntentFilter> intents;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(": name=").append(name);
		sb.append(": intents=").append(intents);
		return sb.toString();
	}

	public static class PluginActivityInfo extends PluginComponentInfo {

		public ActivityInfo info;

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(super.toString());
			sb.append(": info=").append(info);
			return sb.toString();
		}
	}

	public static class PluginProviderInfo extends PluginComponentInfo {

		public ProviderInfo info;

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(super.toString());
			sb.append(": info=").append(info);
			return sb.toString();
		}
	}

	public static class PluginServiceInfo extends PluginComponentInfo {

		public ServiceInfo info;

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(super.toString());
			sb.append(": info=").append(info);
			return sb.toString();
		}
	}

}
