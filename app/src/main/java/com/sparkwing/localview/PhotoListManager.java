package com.sparkwing.localview;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import java.util.List;

/**
 * Created by zachfreeman on 9/19/15.
 */
public class PhotoListManager implements LocationUpdaterListener, PhotoListFetcherListener {
    private static final String TAG = PhotoListManager.class.getSimpleName();
    private Context mContext;
    private Boolean mPhotoListFetched;

    public void setPhotoListManagerListener(PhotoListManagerListener mPhotoListManagerListener) {
        this.mPhotoListFetched = false;
        this.mPhotoListManagerListener = mPhotoListManagerListener;
    }

    private PhotoListManagerListener mPhotoListManagerListener;

    public PhotoListManager(Context context) {

        if (Reachability.isConnected(context)) {
            this.mContext = context;
            LocationUpdater locationUpdater = new LocationUpdater(this.mContext);
            locationUpdater.setLocationUpdaterListener(this);
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
