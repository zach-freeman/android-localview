package com.sparkwing.localview;



import com.google.android.gms.common.ConnectionResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.gms.ShadowGooglePlayServicesUtil;

import static org.junit.Assert.assertTrue;

@RunWith(LocalviewTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class PhotosListViewActivityTest {

    @Test
    public void testSomething() throws Exception {
        assertTrue(Robolectric.setupActivity(PhotosListViewActivity.class) != null);
    }
}