package com.sparkwing.localview;

import android.app.Application;

/**
 * Created by zachfreeman on 5/9/16.
 */
public class LocalviewApplication extends Application {

    private LocationUpdaterComponent mLocationUpdaterComponent;
    private PhotoListManagerComponent mPhotoListManagerComponent;
    private RequestPermissionUtilsComponent mRequestPermissionUtilsComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
        mLocationUpdaterComponent = DaggerLocationUpdaterComponent.builder()
                // list of modules that are part of this component need to be created here too
                .localviewApplicationModule(new LocalviewApplicationModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .build();

        mPhotoListManagerComponent = DaggerPhotoListManagerComponent.builder()
                // list of modules that are part of this component need to be created here too
                .localviewApplicationModule(new LocalviewApplicationModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .build();

        mRequestPermissionUtilsComponent = DaggerRequestPermissionUtilsComponent.builder()
                .localviewApplicationModule(new LocalviewApplicationModule(this))
                .build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mNetComponent = com.codepath.dagger.components.DaggerNetComponent.create();
    }

    public LocationUpdaterComponent getLocationUpdaterComponent() {
        return mLocationUpdaterComponent;
    }

    public PhotoListManagerComponent getPhotoListManagerComponent() {
        return mPhotoListManagerComponent;
    }

    public RequestPermissionUtilsComponent getRequestPermissionUtilsComponent() {
        return mRequestPermissionUtilsComponent;
    }
}
