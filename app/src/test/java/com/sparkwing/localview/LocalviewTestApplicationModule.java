package com.sparkwing.localview;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import org.hamcrest.core.AnyOf;
import org.hamcrest.core.IsAnything;
import org.hamcrest.core.IsInstanceOf;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RuntimeEnvironment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

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
        when(requestPermissionUtils.checkPermission(any(Context.class), anyString())).thenReturn(PackageManager.PERMISSION_GRANTED);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                //subject.requestPermissionCallback.Granted(1);
                return null;
            }
        }).when(requestPermissionUtils).requestPermission(any(Activity.class), anyString(), any(RequestPermissionUtils.RequestPermissionCallback.class), anyInt());
        return requestPermissionUtils;
    }

}
