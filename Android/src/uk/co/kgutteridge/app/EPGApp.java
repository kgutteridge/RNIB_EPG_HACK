package uk.co.kgutteridge.app;

import android.app.Application;
import android.content.Context;

public class EPGApp extends Application {
	
    private static EPGApp instance;

	@Override
	public void onCreate() {
		super.onCreate();
    	instance = this;
	}

	public static Context getContext() {
        return instance.getApplicationContext();
    }
}

