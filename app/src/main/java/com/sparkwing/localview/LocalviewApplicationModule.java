package com.sparkwing.localview;

import android.content.Context;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zachfreeman on 5/9/16.
 */
@Module
public class LocalviewApplicationModule {

    private Context mContext;

    public LocalviewApplicationModule(Context context) {
        this.mContext = context;
    }

    @Provides
    @Singleton
    PhotoListManager providePhotoListManager() {
        return new PhotoListManager(mContext);
    }

    @Provides
    @Singleton
    LocationUpdater provideLocationUpdater() {
        return new LocationUpdater(mContext);
    }

    @Provides
    @Singleton
    RequestPermissionUtils provideRequestPermissionUtils() {
        return new RequestPermissionUtils();
    }
}
