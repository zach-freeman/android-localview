package com.sparkwing.localview.Providers;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sparkwing.localview.LocationUpdater;

/**
 * Created by zachfreeman on 7/17/16.
 */
public class LocationUpdaterProvider implements Provider<LocationUpdater> {

    @Inject
    private Context context;

    @Override
    public LocationUpdater get() {
        return new LocationUpdater(context);
    }
}
