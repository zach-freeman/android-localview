package com.sparkwing.localview.util;

import android.content.Context;

import com.google.inject.Inject;
import com.sparkwing.localview.PhotoListManager;

import javax.inject.Provider;

/**
 * Created by zachfreeman on 5/9/16.
 */
public class TestPhotoListManagerProvider implements Provider<PhotoListManager> {
    //@Inject LocalviewPhotoListManagerBuilderProvider;
    @Inject
    Context context;
    @Override
    public PhotoListManager get() {
       return new PhotoListManager(context);
    }
}
