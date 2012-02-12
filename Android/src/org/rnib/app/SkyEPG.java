package org.rnib.app;

import android.app.Application;
import android.content.Context;

public class SkyEPG extends Application {
	
    private static SkyEPG instance;

	@Override
	public void onCreate() {
		super.onCreate();
    	instance = this;
	}

	public static Context getContext() {
        return instance.getApplicationContext();
    }
	
}

