package com.sparkwing.localview;

import android.location.Location;

/**
 * Created by zachfreeman on 9/19/15.
 */
public interface LocationUpdaterListener {
    public void locationAvailable(Location currentLocation);
}
