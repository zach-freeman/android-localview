package com.sparkwing.localview;

/**
 * Created by zachfreeman on 2/12/17.
 */

public class LocalviewTestApplication extends LocalviewApplication {
    private LocationUpdaterComponent mLocationUpdaterComponent;
    private PhotoListManagerComponent mPhotoListManagerComponent;
    private RequestPermissionUtilsComponent mRequestPermissionUtilsComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        // Dagger%COMPONENT_NAME%
        mLocationUpdaterComponent = DaggerLocationUpdaterComponent.builder()
                // list of modules that are part of this component need to be created here too
                .localviewApplicationModule(new LocalviewTestApplicationModule()) // This also corresponds to the name of your module: %component_name%Module
                .build();

        mPhotoListManagerComponent = DaggerPhotoListManagerComponent.builder()
                // list of modules that are part of this component need to be created here too
                .localviewApplicationModule(new LocalviewTestApplicationModule()) // This also corresponds to the name of your module: %component_name%Module
                .build();

        mRequestPermissionUtilsComponent = DaggerRequestPermissionUtilsComponent.builder()
                .localviewApplicationModule(new LocalviewTestApplicationModule())
                .build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mNetComponent = com.codepath.dagger.components.DaggerNetComponent.create();
    }
    @Override
    public LocationUpdaterComponent getLocationUpdaterComponent() {
        return mLocationUpdaterComponent;
    }

    @Override
    public PhotoListManagerComponent getPhotoListManagerComponent() {
        return mPhotoListManagerComponent;
    }

    @Override
    public RequestPermissionUtilsComponent getRequestPermissionUtilsComponent() {
        return mRequestPermissionUtilsComponent;
    }
}
