package com.sparkwing.localview;

import android.Manifest;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import roboguice.RoboGuice;
import roboguice.activity.RoboActionBarActivity;

public class PhotosListViewActivity extends RoboActionBarActivity implements PhotoListManagerListener{

    private static final String TAG = PhotosListViewActivity.class.getSimpleName();
    private static final String SAVE_PHOTO_LIST_KEY = "photo-list";
    private static final String PHOTO_LIST_STATE_KEY = "photo-list-state";
    private Parcelable mPhotoListState = null;

    @Inject public RequestPermissionUtils requestPermissionUtils;

    public RequestPermissionUtils.RequestPermissionCallback requestPermissionCallback = new RequestPermissionUtils.RequestPermissionCallback() {
        @Override
        public void Granted(int requestCode) {
            Log.d(TAG, "permission granted");
            startPhotoListManager();
        }

        @Override
        public void Denied(int requestCode) {

        }
    };
    @Inject PhotoListManager mPhotoListManager;

    RecyclerView mPhotoRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView getPhotoRecyclerView() {
        return mPhotoRecyclerView;
    }

    private List<FlickrPhoto> mFlickrPhotoList = new ArrayList<FlickrPhoto>();

    public List<FlickrPhoto> getFlickrPhotoList() {
        return mFlickrPhotoList;
    }

    public void setFlickrPhotoList(List<FlickrPhoto> mFlickrPhotoList) {
        this.mFlickrPhotoList = mFlickrPhotoList;
    }

    public ProgressBar getProgressBarSpinner() {
        return mProgressBarSpinner;
    }

    private ProgressBar mProgressBarSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_photos_list_view);
        RoboGuice.getInjector(this).injectMembers(this);
        mProgressBarSpinner = (ProgressBar)findViewById(R.id.progressBarSpinner);
        mPhotoRecyclerView = (RecyclerView) findViewById(R.id.listView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mPhotoRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 4);
        mPhotoRecyclerView.setLayoutManager(mLayoutManager);
        PhotoListAdapter emptyPhotoListAdapter = new PhotoListAdapter(new ArrayList<FlickrPhoto>());
        mPhotoRecyclerView.setAdapter(emptyPhotoListAdapter);

        if (savedInstanceState != null) {
            this.mFlickrPhotoList = savedInstanceState.getParcelableArrayList(SAVE_PHOTO_LIST_KEY);
            this.mPhotoListState = savedInstanceState.getParcelable(PHOTO_LIST_STATE_KEY);
            setupPhotoList();
        } else {
            this.requestLocationPermission();
        }

    }

    protected void requestLocationPermission() {
        requestPermissionUtils.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, requestPermissionCallback, Constants.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    protected void startPhotoListManager() {
        mProgressBarSpinner.setVisibility(View.VISIBLE);
        boolean photoListFetched = mPhotoListManager.getPhotoListFetched();
        mPhotoListManager.setPhotoListManagerListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        requestPermissionUtils.onRequestPermissionsResult(requestPermissionCallback,
                requestCode,
                permissions,
                grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.wtf(TAG, "onResume");
        this.mProgressBarSpinner.setVisibility(View.INVISIBLE);
        if (this.mPhotoListState != null) {
            this.mPhotoRecyclerView.getLayoutManager().onRestoreInstanceState(this.mPhotoListState);
        }
        this.mPhotoListState = null;
    }


    @Override
    protected void onDestroy() {
        // onDestroy gets called when back button is tapped
        super.onDestroy();
        this.mPhotoListState = this.mPhotoRecyclerView.getLayoutManager().onSaveInstanceState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            this.mPhotoListState = null;
            this.startPhotoListManager();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<FlickrPhoto> listOfPhotos = new ArrayList<>(this.mFlickrPhotoList.size());
        listOfPhotos.addAll(this.mFlickrPhotoList);
        outState.putParcelableArrayList(SAVE_PHOTO_LIST_KEY, listOfPhotos);
        this.mPhotoListState = this.mPhotoRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(PHOTO_LIST_STATE_KEY, this.mPhotoListState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.wtf(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        this.mPhotoListState = savedInstanceState.getParcelable(PHOTO_LIST_STATE_KEY);
        if (savedInstanceState.containsKey(SAVE_PHOTO_LIST_KEY)) {
            this.mFlickrPhotoList = savedInstanceState.getParcelableArrayList(SAVE_PHOTO_LIST_KEY);
            setupPhotoList();
        }
    }


    protected void setupPhotoList() {
        PhotoListAdapter photoListAdapter = new PhotoListAdapter(this.mFlickrPhotoList);
        Log.wtf(TAG, "setting adapter on recycler view");
        this.mPhotoRecyclerView.setAdapter(photoListAdapter);
        if (this.mPhotoListState != null) {
            this.mPhotoRecyclerView.getLayoutManager().onRestoreInstanceState(this.mPhotoListState);
        }
    }

    @Override
    public void photoListManagerDidFinish(List<FlickrPhoto> photoList) {
        this.mProgressBarSpinner.setVisibility(View.INVISIBLE);
        if (photoList.size() > 0) {
            this.mFlickrPhotoList = photoList;
            setupPhotoList();
        } else {
            Toast.makeText(this,
                    "Flickr api response malformed", Toast.LENGTH_LONG).show();
        }
    }

}
