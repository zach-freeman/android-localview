package com.sparkwing.localview;

import com.sparkwing.localview.Providers.LocationUpdaterProvider;
import com.sparkwing.localview.Providers.RequestPermissionUtilsProvider;

/**
 * Created by zachfreeman on 5/9/16.
 */
public class LocalviewTestApplicationModule extends LocalviewApplicationModule {
    @Override
    protected void configure() {
        bind(RequestPermissionUtils.class).toProvider(RequestPermissionUtilsProvider.class);
        bind(LocationUpdater.class).toProvider(LocationUpdaterProvider.class);
    }
}
