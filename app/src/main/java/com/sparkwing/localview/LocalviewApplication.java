package com.sparkwing.localview;

import android.app.Application;

import roboguice.RoboGuice;

/**
 * Created by zachfreeman on 5/9/16.
 */
public class LocalviewApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setBaseInjector();
    }

    protected void setBaseInjector() {
        RoboGuice.overrideApplicationInjector(this, new LocalviewApplicationModule());
    }
}
