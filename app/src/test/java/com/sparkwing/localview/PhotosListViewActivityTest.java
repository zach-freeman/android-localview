package com.sparkwing.localview;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sparkwing.localview.Models.Photo;

import org.fest.assertions.api.ANDROID;
import org.fest.assertions.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;

@RunWith(LocalviewTestRunner.class)
@Config(constants = BuildConfig.class, packageName = BuildConfig.BASE_APP_ID, sdk = 21, manifest = "../app/src/main/AndroidManifest.xml")
public class PhotosListViewActivityTest {
    PhotosListViewActivity subject;
    ActivityController<PhotosListViewActivity> activityController;

    List flickrPhotoList;

    @Before
    public void setUp() throws Exception {

        activityController = Robolectric.buildActivity(PhotosListViewActivity.class);
        subject = activityController.get();
        Photo somePhoto = new Photo();
        somePhoto.setId("some-photo-id");
        somePhoto.setFarm(1);
        somePhoto.setServer("some-server");
        somePhoto.setSecret("shhh");
        flickrPhotoList = new ArrayList<Photo> (Arrays.asList(somePhoto));
        subject.setFlickrPhotoList(flickrPhotoList);
        activityController.create().start();
        spy(subject);
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
    public void testOnCreate_callsSetPhotoListManagerListener() {
        Mockito.verify(subject.mPhotoListManager).setPhotoListManagerListener(subject);
    }

    @Test
    public void testOnCreate_setsUpPhotoListView() {
        ANDROID.assertThat(subject.getPhotoRecyclerView()).isNotNull();
    }

    @Test
    public void testPhotoListManagerDidFinish_withNoPhotos_ShowsToast() {
        List emptyFlickrPhotoList = new ArrayList<Photo> ();
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
        subject.setupPhotoList();
        RecyclerView recyclerView = subject.getPhotoRecyclerView();
        recyclerView.measure(0,0);
        recyclerView.layout(0,0,100,1000);
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
        View view = viewHolder.itemView;
        view.performClick();
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