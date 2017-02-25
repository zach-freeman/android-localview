package com.sparkwing.localview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import com.sparkwing.localview.util.MockInject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;


@RunWith(LocalviewTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "../app/src/main/AndroidManifest.xml")
public class RequestPermissionUtilsTest {

    RequestPermissionUtils subject;
    RequestPermissionUtils.RequestPermissionCallback mRequestPermissionCallback;
    Context mContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mContext = RuntimeEnvironment.application;
        mRequestPermissionCallback = mock(RequestPermissionUtils.RequestPermissionCallback.class);
        subject = new RequestPermissionUtils();
        Mockito.spy(subject);
    }

    @Test
    public void testOnRequestPermissionsResult_WhenPermissionGranted_CallsGranted() {
        subject.onRequestPermissionsResult(mRequestPermissionCallback,
                Constants.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                new int[] {PackageManager.PERMISSION_GRANTED});
        Mockito.verify(mRequestPermissionCallback).Granted(Constants.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    @Test
    public void testOnRequestPermissionsResult_WhenPermissionDenied_CallsDenied() {
        subject.onRequestPermissionsResult(mRequestPermissionCallback,
                Constants.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                new int[] {PackageManager.PERMISSION_DENIED});
        Mockito.verify(mRequestPermissionCallback).Denied(Constants.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }
}
