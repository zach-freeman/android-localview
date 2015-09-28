package com.sparkwing.localview;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by zachfreeman on 9/19/15.
 */
public class LocationUpdater
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = LocationUpdater.class.getSimpleName();

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Boolean mRequestingLocationUpdates;
    private Location mCurrentLocation;

    public void setLocationUpdaterListener(LocationUpdaterListener mLocationUpdaterListener) {
        this.mLocationUpdaterListener = mLocationUpdaterListener;
    }

    private LocationUpdaterListener mLocationUpdaterListener;

    public LocationUpdater(Context context) {
        this.mContext = context;
        this.mRequestingLocationUpdates = true;
        this.buildGoogleApiClient();
        mGoogleApiClient.connect();
        this.createLocationRequest();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mRequestingLocationUpdates) {
            this.startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.mCurrentLocation = location;
        this.mRequestingLocationUpdates = false;
        this.stopLocationUpdates();
        this.mLocationUpdaterListener.locationAvailable(this.mCurrentLocation);

    }
}
