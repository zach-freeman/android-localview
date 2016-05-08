package com.sparkwing.localview;


import android.view.View;
import android.widget.ProgressBar;

import org.fest.assertions.api.ANDROID;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.api.android.widget.ProgressBarAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowProgressBar;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.fest.assertions.api.ANDROID.*;
import org.fest.assertions.api.Assertions.*;

@RunWith(LocalviewTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class PhotosListViewActivityTest {
    PhotosListViewActivity subject;
    ActivityController<PhotosListViewActivity> activityController;
    private ProgressBar progressBarSpinner;
    @Before
    public void setUp() throws Exception {
        activityController = Robolectric.buildActivity(PhotosListViewActivity.class);
        subject = activityController.get();
        activityController.create().start();
    }

    @Test
    public void testSomething() throws Exception {
        assertTrue(Robolectric.setupActivity(PhotosListViewActivity.class) != null);
    }

    @Test
    public void testOnCreate_setsUpProgressBar() {
        ANDROID.assertThat(subject).isNotNull();
        progressBarSpinner = subject.getProgressBarSpinner();
        ANDROID.assertThat(progressBarSpinner).isNotNull();
        int visibility = progressBarSpinner.getVisibility();
        Assertions.assertThat(visibility).isEqualTo(View.VISIBLE);
    }
}