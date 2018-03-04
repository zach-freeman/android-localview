package com.sparkwing.localview;

import android.Manifest;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.sparkwing.localview.Models.Photo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PhotosListViewActivity extends ActionBarActivity implements PhotoListManagerListener{

    private static final String TAG = PhotosListViewActivity.class.getSimpleName();
    private static final String SAVE_PHOTO_LIST_KEY = "photo-list";
    private static final String PHOTO_LIST_STATE_KEY = "photo-list-state";
    private static final int SWITCHER_PROGRESS_BAR = 0;
    private static final int SWITCHER_RECYCLER_VIEW = 1;
    private Parcelable mPhotoListState = null;
    private Menu mMenu;
    private ViewSwitcher switcher;
    @Inject
    public RequestPermissionUtils requestPermissionUtils;

    public RequestPermissionUtils.RequestPermissionCallback requestPermissionCallback = new RequestPermissionUtils.RequestPermissionCallback() {
        @Override
        public void Granted(int requestCode) {
            Log.d(TAG, "permission granted");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startPhotoListManager();
                }
            });
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

    private List<Photo> mFlickrPhotoList = new ArrayList<Photo>();

    public List<Photo> getFlickrPhotoList() {
        return mFlickrPhotoList;
    }

    public void setFlickrPhotoList(List<Photo> mFlickrPhotoList) {
        this.mFlickrPhotoList = mFlickrPhotoList;
    }

    public ProgressBar getProgressBarSpinner() {
        return mProgressBarSpinner;
    }

    private ProgressBar mProgressBarSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((LocalviewApplication) getApplication()).getRequestPermissionUtilsComponent().inject(this);
        ((LocalviewApplication) getApplication()).getPhotoListManagerComponent().inject(this);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_photos_list_view);
        switcher = (ViewSwitcher) findViewById(R.id.ViewSwitcher);
        switcher.setDisplayedChild(SWITCHER_PROGRESS_BAR);
        mProgressBarSpinner = (ProgressBar)findViewById(R.id.progressBarSpinner);
        mPhotoRecyclerView = (RecyclerView) findViewById(R.id.listView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mPhotoRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 5);
        mPhotoRecyclerView.setLayoutManager(mLayoutManager);
        PhotoListAdapter emptyPhotoListAdapter = new PhotoListAdapter(new ArrayList<Photo>());
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
        switcher.setDisplayedChild(SWITCHER_PROGRESS_BAR);
        mProgressBarSpinner.setVisibility(View.VISIBLE);
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
        this.mProgressBarSpinner.setVisibility(View.VISIBLE);
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
        mMenu = menu;
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
            mMenu.findItem(R.id.action_refresh).setEnabled(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Photo> listOfPhotos = new ArrayList<>(this.mFlickrPhotoList.size());
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
        this.mPhotoRecyclerView.setAdapter(photoListAdapter);
        if (this.mFlickrPhotoList != null) {
            this.mProgressBarSpinner.setVisibility(View.VISIBLE);
            this.mPhotoRecyclerView.getLayoutManager().onRestoreInstanceState(this.mPhotoListState);
        }
    }

    @Override
    public void photoListManagerDidFinish(List<Photo> photoList) {
        this.mProgressBarSpinner.setVisibility(View.INVISIBLE);
        switcher.setDisplayedChild(SWITCHER_RECYCLER_VIEW);
        mMenu.findItem(R.id.action_refresh).setEnabled(true);
        if (photoList.size() > 0) {
            this.mFlickrPhotoList = photoList;
            setupPhotoList();
        } else {
            Toast.makeText(this,
                    "Flickr api response malformed", Toast.LENGTH_LONG).show();
        }
    }

}
