package com.sparkwing.localview;


import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zachfreeman on 5/9/16.
 */
@Module
public class LocalviewTestApplicationModule extends LocalviewApplicationModule {

    public LocalviewTestApplicationModule() {
        super(RuntimeEnvironment.application);
    }

    @Provides
    @Singleton
    PhotoListManager providePhotoListManager() {
        return Mockito.mock(PhotoListManager.class);
    }

    @Provides
    @Singleton
    LocationUpdater provideLocationUpdater() {
        return Mockito.mock(LocationUpdater.class);
    }

    @Provides
    @Singleton
    RequestPermissionUtils provideRequestPermissionUtils() {
        return Mockito.mock(RequestPermissionUtils.class);
    }

}
