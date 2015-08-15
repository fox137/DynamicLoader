package com.example.pluginlib;

public interface IActivityLifeCircle {

	void callOnRestart();  
    void callOnStart();  
    void callOnResume();  
    void callOnPause();  
    void callOnStop();  
    void callOnDestory(); 

}
