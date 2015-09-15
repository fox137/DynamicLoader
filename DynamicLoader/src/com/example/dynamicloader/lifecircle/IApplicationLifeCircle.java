package com.example.dynamicloader.lifecircle;

import android.app.Activity;
import android.app.Application;

public interface IApplicationLifeCircle {

	void attach(PluginContext context);
	void callOnCreate();
}
