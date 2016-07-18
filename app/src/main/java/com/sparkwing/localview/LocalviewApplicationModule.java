package com.sparkwing.localview;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.sparkwing.localview.Providers.LocationUpdaterProvider;
import com.sparkwing.localview.Providers.RequestPermissionUtilsProvider;

/**
 * Created by zachfreeman on 5/9/16.
 */
public class LocalviewApplicationModule extends AbstractModule {
    @Inject PhotoListManager mPhotoListManager;
    @Inject LocationUpdater mLocationUpdater;
    @Override
    protected void configure() {
        bind(RequestPermissionUtils.class).toProvider(RequestPermissionUtilsProvider.class);
        bind(LocationUpdater.class).toProvider(LocationUpdaterProvider.class);
    }
}
