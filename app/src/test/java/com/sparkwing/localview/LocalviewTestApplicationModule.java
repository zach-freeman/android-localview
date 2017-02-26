package com.sparkwing.localview;


import android.app.Activity;
import android.content.pm.PackageManager;

import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

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
        PhotoListManager photoListManager = Mockito.mock(PhotoListManager.class);
        Mockito.stub(photoListManager.getPhotoListFetched()).toReturn(true);

        return photoListManager;
    }

    @Provides
    @Singleton
    LocationUpdater provideLocationUpdater() {
        return Mockito.mock(LocationUpdater.class);
    }

    @Provides
    @Singleton
    RequestPermissionUtils provideRequestPermissionUtils() {
        RequestPermissionUtils requestPermissionUtils = Mockito.mock(RequestPermissionUtils.class);
        Mockito.stub(requestPermissionUtils.checkPermission(any(Activity.class), anyString())).toReturn(PackageManager.PERMISSION_GRANTED);
        Mockito.doCallRealMethod().when(requestPermissionUtils).requestPermission(any(Activity.class),anyString(),any(RequestPermissionUtils.RequestPermissionCallback.class), anyInt());
        return requestPermissionUtils;
    }

}
