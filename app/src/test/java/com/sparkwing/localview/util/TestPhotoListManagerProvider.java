package com.sparkwing.localview.util;

import android.content.Context;

import com.sparkwing.localview.PhotoListManager;

import javax.inject.Inject;
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
