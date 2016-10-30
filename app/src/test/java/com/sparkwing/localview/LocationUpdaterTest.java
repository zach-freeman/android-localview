package com.sparkwing.localview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.sparkwing.localview.util.MockInject;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertTrue;

@RunWith(LocalviewTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "../app/src/main/AndroidManifest.xml")
public class LocationUpdaterTest {

    LocationUpdater subject;
    Context mContext;
    @MockInject
    RequestPermissionUtils mRequestPermissionUtils;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mContext = RuntimeEnvironment.application;
        subject = new LocationUpdater(mContext);
        Mockito.stub(mRequestPermissionUtils.checkPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)).toReturn(PackageManager.PERMISSION_GRANTED);
    }

    @Test
    public void testConstructor() {
        assertTrue(subject != null);
        Assertions.assertThat(subject.getGoogleApiClientStatus().equals(LocationUpdater.GoogleApiClientStatus.CONNECTION_UNKNOWN)).isTrue();
        Assertions.assertThat(subject.getGoogleApiClient()).isNotNull();
        Assertions.assertThat(subject.getLocationRequest()).isNotNull();
    }

    @Test
    public void testStartLocationUpdates_whenConnectedAndPermissionGranted_DoesNotShowToast() {
        subject.setGoogleApiClientStatus(LocationUpdater.GoogleApiClientStatus.CONNECTION_SUCCESS);
        subject.startLocationUpdates();
        Toast actualToast = ShadowToast.getLatestToast();
        Assertions.assertThat(actualToast).isNull();
    }

    @Test
    public void testStartLocationUpdates_whenNotConnectedAndPermissionGranted_DoesNotShowToast() {
        subject.setGoogleApiClientStatus(LocationUpdater.GoogleApiClientStatus.CONNECTION_FAIL);
        subject.startLocationUpdates();
        Toast actualToast = ShadowToast.getLatestToast();
        Assertions.assertThat(actualToast).isNotNull();
        String actualToastText = ShadowToast.getTextOfLatestToast();
        Assertions.assertThat(actualToastText).isEqualTo("Google Api Client Not connected");

    }
}
