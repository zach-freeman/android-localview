package com.sparkwing.localview;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.google.inject.Inject;

import java.util.List;

/**
 * Created by zachfreeman on 9/19/15.
 */
public class PhotoListManager implements LocationUpdaterListener, PhotoListFetcherListener {
    private static final String TAG = PhotoListManager.class.getSimpleName();

    public Boolean getPhotoListFetched() {
        return mPhotoListFetched;
    }

    private Boolean mPhotoListFetched;
    private LocationUpdater mLocationUpdater;
    private PhotoListManagerListener mPhotoListManagerListener;

    public void setPhotoListManagerListener(PhotoListManagerListener mPhotoListManagerListener) {
        this.mPhotoListManagerListener = mPhotoListManagerListener;
        this.mLocationUpdater.startLocationUpdates();
    }



    @Inject
    public PhotoListManager(Context context) {
        this.mPhotoListFetched = false;
        this.mLocationUpdater = new LocationUpdater(context);
        if (Reachability.isConnected(context)) {
            this.mLocationUpdater.setLocationUpdaterListener(this);
        } else {
            Toast.makeText(context,
                    "Network Unavailable", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void locationAvailable(Location currentLocation) {
        if (!mPhotoListFetched) {
            PhotoListFetcher photoListFetcher = new PhotoListFetcher();
            photoListFetcher.setPhotoListFetcherListener(this);
            photoListFetcher.execute(String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()));
        }
    }

    @Override
    public void onPhotoListFetched(List<FlickrPhoto> photoList) {
        this.mPhotoListFetched = true;
        this.mPhotoListManagerListener.photoListManagerDidFinish(photoList);
    }
}
