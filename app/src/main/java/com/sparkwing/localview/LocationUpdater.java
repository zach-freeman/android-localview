package com.sparkwing.localview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

/**
 * Created by zachfreeman on 9/19/15.
 */
public class LocationUpdater
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = LocationUpdater.class.getSimpleName();
    public static class GoogleApiClientStatus {
        public static final String CONNECTION_SUCCESS = "connection_success";
        public static final String CONNECTION_FAIL = "connnection_fail";
        public static final String CONNECTION_UNKNOWN = "connection_unknown";
    }
    
    private String mGoogleApiClientStatus;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Boolean mRequestingLocationUpdates;
    private Location mCurrentLocation;
    @Inject RequestPermissionUtils mRequestPermissionUtils;
    private LocationUpdaterListener mLocationUpdaterListener;

    @Inject
    public LocationUpdater(Context context) {
        this.mContext = context;
        ((LocalviewApplication) context).getRequestPermissionUtilsComponent().inject(this);
        this.mGoogleApiClientStatus = GoogleApiClientStatus.CONNECTION_UNKNOWN;
        setupLocationService();
    }

    public void setupLocationService() {
        this.buildGoogleApiClient();
        this.mRequestingLocationUpdates = true;
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
        this.mGoogleApiClientStatus = GoogleApiClientStatus.CONNECTION_SUCCESS;
        startLocationUpdates();
    }

    public void startLocationUpdates() {
        mRequestPermissionUtils = new RequestPermissionUtils();
        int permissionCheck = mRequestPermissionUtils.checkPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            } catch (IllegalStateException illegalStateException) {
                Log.d(TAG, "google api client not connected");
                Log.d(TAG, illegalStateException.getLocalizedMessage());
            }
        } else {
            Toast.makeText(this.mContext, "Permission not granted", Toast.LENGTH_LONG).show();
        }
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
        this.mGoogleApiClientStatus = GoogleApiClientStatus.CONNECTION_FAIL;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.mCurrentLocation = location;
        //this.stopLocationUpdates();
        if (this.mLocationUpdaterListener != null) {
            this.mRequestingLocationUpdates = false;
            this.mLocationUpdaterListener.locationAvailable(this.mCurrentLocation);
        }
    }

    public String getGoogleApiClientStatus() {
        return mGoogleApiClientStatus;
    }

    public void setGoogleApiClientStatus(String mGoogleApiClientStatus) {
        this.mGoogleApiClientStatus = mGoogleApiClientStatus;
    }

    public LocationRequest getLocationRequest() {
        return mLocationRequest;
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setLocationUpdaterListener(LocationUpdaterListener mLocationUpdaterListener) {
        this.mLocationUpdaterListener = mLocationUpdaterListener;
    }
}
