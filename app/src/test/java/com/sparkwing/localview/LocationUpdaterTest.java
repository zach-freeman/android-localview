package com.sparkwing.localview;

import android.content.Context;
import android.widget.Toast;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(LocalviewTestRunner.class)
@Config(constants = BuildConfig.class, packageName = BuildConfig.BASE_APP_ID, sdk = 21, manifest = "../app/src/main/AndroidManifest.xml")
public class LocationUpdaterTest {

    LocationUpdater subject;
    Context mContext;
    RequestPermissionUtils mRequestPermissionUtils;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mContext = RuntimeEnvironment.application;
        subject = new LocationUpdater(mContext);
        mRequestPermissionUtils = mock(RequestPermissionUtils.class);
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
        when(subject.mRequestPermissionUtils.checkPermission(any(Context.class), anyString())).thenReturn(-1);
        subject.setGoogleApiClientStatus(LocationUpdater.GoogleApiClientStatus.CONNECTION_FAIL);
        subject.startLocationUpdates();
        Toast actualToast = ShadowToast.getLatestToast();
        Assertions.assertThat(actualToast).isNotNull();
        String actualToastText = ShadowToast.getTextOfLatestToast();
        Assertions.assertThat(actualToastText).isEqualTo("Permission not granted");

    }
}
