package com.example.dynamicloader.lifecircle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public interface IActivityLifeCircle {

	void setContext(PluginContext context);
	void callOnCreate(Bundle bundle);
	void callOnStart();
	void callOnRestart();
	void callOnResume();
	void callOnPause();
	void callOnStop();
	void callOnDestory();
	void callOnActivityResult(int requestCode, int resultCode, Intent data);
}
