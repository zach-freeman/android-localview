package com.sparkwing.localview;


import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sparkwing.localview.util.MockInject;

import org.fest.assertions.api.ANDROID;
import org.fest.assertions.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(LocalviewTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "../app/src/main/AndroidManifest.xml")
public class PhotosListViewActivityTest {
    PhotosListViewActivity subject;
    ActivityController<PhotosListViewActivity> activityController;
    @MockInject PhotoListManager mPhotoListManager;
    @MockInject RequestPermissionUtils mRequestPermissionUtils;

    List flickrPhotoList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.stub(mPhotoListManager.getPhotoListFetched()).toReturn(true);

        activityController = Robolectric.buildActivity(PhotosListViewActivity.class);
        subject = activityController.get();
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                subject.requestPermissionCallback.Granted(1);
                return null;
            }
        }).when(mRequestPermissionUtils).requestPermission(subject, Manifest.permission.ACCESS_FINE_LOCATION, subject.requestPermissionCallback, Constants.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        flickrPhotoList = new ArrayList<FlickrPhoto> (Arrays.asList(new FlickrPhoto("some-photo", "some-small-url", "some-big-url")));
        subject.setFlickrPhotoList(flickrPhotoList);
        activityController.create().start();
    }

    @Test
    public void testSomething() throws Exception {
        assertTrue(Robolectric.setupActivity(PhotosListViewActivity.class) != null);
    }

    @Test
    public void testOnCreate_setsUpProgressBar() {
        ANDROID.assertThat(subject).isNotNull();
        ProgressBar progressBarSpinner = subject.getProgressBarSpinner();
        ANDROID.assertThat(progressBarSpinner).isNotNull();
        int visibility = progressBarSpinner.getVisibility();
        Assertions.assertThat(visibility).isEqualTo(View.VISIBLE);
    }

    @Test
    public void testOnCreate_callsSetPhotoListManager() {

        Mockito.verify(mPhotoListManager).setPhotoListManagerListener(subject);
    }

    @Test
    public void testOnCreate_setsUpPhotoListView() {
        ANDROID.assertThat(subject.getPhotoListView()).isNotNull();
    }

    @Test
    public void testPhotoListManagerDidFinish_withNoPhotos_ShowsToast() {
        List emptyFlickrPhotoList = new ArrayList<FlickrPhoto> ();
        subject.photoListManagerDidFinish(emptyFlickrPhotoList);
        Toast actualToast = ShadowToast.getLatestToast();
        Assertions.assertThat(actualToast).isNotNull();
        String actualToastText = ShadowToast.getTextOfLatestToast();
        Assertions.assertThat(actualToastText).isEqualTo("Flickr api response malformed");
    }

    @Test
    public void testPhotoListManagerDidFinish_withPhotos_DoesNotShowToast() {
        subject.photoListManagerDidFinish(flickrPhotoList);
        Toast actualToast = ShadowToast.getLatestToast();
        Assertions.assertThat(actualToast).isNull();
    }

    @Test
    public void testPhotoListManagerDidFinish_withPhotos_setsFlickrPhotoList() {
        subject.photoListManagerDidFinish(flickrPhotoList);
        List actualPhotoList = subject.getFlickrPhotoList();
        Assertions.assertThat(flickrPhotoList).isEqualTo(actualPhotoList);
    }

    @Test
    public void testOnItemClick_startsFullPhotoViewIntent() {
        subject.onItemClick(null, null, 0, 1);
        Intent expectedIntent = new Intent(subject, PhotoFullScreenActivity.class);
        ShadowActivity shadowActivity = Shadows.shadowOf(subject);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        Assertions.assertThat(actualIntent).isNotNull();
        assertTrue(actualIntent.filterEquals(expectedIntent));
    }

    // @After => JUnit 4 annotation that specifies this method should be run after each test
    @After
    public void tearDown() {
        // Destroy activity after every test
        activityController
                .pause()
                .stop()
                .destroy();
    }
}