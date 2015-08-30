package com.example.dynamicloader.lifecircle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public interface IServiceLifeCircle {
	void setContext(PluginContext context);
	IBinder callOnBind(Intent intent);
	void callOnCreate();
	int callOnStartCommand(Intent intent, int flags, int startId);
	boolean callOnUnbind(Intent intent);
	void callOnRebind(Intent intent);
	void callOnLowMemory();
	void callOnTrimMemory(int level);
	void callOnDestroy();
}
