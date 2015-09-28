package com.sparkwing.localview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

public class PhotosListViewActivity extends AppCompatActivity implements PhotoListManagerListener, AdapterView.OnItemClickListener {

    private static final String TAG = PhotosListViewActivity.class.getSimpleName();
    private static final String SAVE_PHOTO_LIST_KEY = "photo-list";
    private static final String PHOTO_LIST_STATE_KEY = "photo-list-state";
    private Parcelable mPhotoListState = null;
    ListView mPhotoListView;
    private List<FlickrPhoto> mFlickrPhotoList = new ArrayList<FlickrPhoto>();
    private ProgressBar mProgressBarSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_photos_list_view);
        mProgressBarSpinner = (ProgressBar)findViewById(R.id.progressBarSpinner);
        mPhotoListView = (ListView) findViewById(R.id.listView);

        if (savedInstanceState != null) {
            this.mFlickrPhotoList = savedInstanceState.getParcelableArrayList(SAVE_PHOTO_LIST_KEY);
            this.mPhotoListState = savedInstanceState.getParcelable(PHOTO_LIST_STATE_KEY);
            setupPhotoList();
        } else {
            this.startPhotoListManager();
        }

    }

    protected void startPhotoListManager() {
        this.mProgressBarSpinner.setVisibility(View.VISIBLE);
        PhotoListManager photoListManager = new PhotoListManager(this);
        photoListManager.setPhotoListManagerListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mProgressBarSpinner.setVisibility(View.INVISIBLE);
        if (this.mPhotoListState != null) {
            this.mPhotoListView.onRestoreInstanceState(this.mPhotoListState);
        }
        this.mPhotoListState = null;
    }


    @Override
    protected void onDestroy() {
        // onDestroy gets called when back button is tapped
        super.onDestroy();
        this.mPhotoListState = this.mPhotoListView.onSaveInstanceState();

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
        this.mPhotoListState = this.mPhotoListView.onSaveInstanceState();
        outState.putParcelable(PHOTO_LIST_STATE_KEY, this.mPhotoListState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mPhotoListState = savedInstanceState.getParcelable(PHOTO_LIST_STATE_KEY);
        if (savedInstanceState.containsKey(SAVE_PHOTO_LIST_KEY)) {
            this.mFlickrPhotoList = savedInstanceState.getParcelableArrayList(SAVE_PHOTO_LIST_KEY);
            setupPhotoList();
        }
    }


    protected void setupPhotoList() {
        PhotoListAdapter photoListAdapter = new PhotoListAdapter(this, this.mFlickrPhotoList);
        this.mPhotoListView.setAdapter(photoListAdapter);
        if (this.mPhotoListState != null) {
            this.mPhotoListView.onRestoreInstanceState(this.mPhotoListState);
        }
        this.mPhotoListView.setOnItemClickListener(this);
    }

    @Override
    public void photoListManagerDidFinish(List<FlickrPhoto> photoList) {
        this.mFlickrPhotoList = photoList;
        this.mProgressBarSpinner.setVisibility(View.INVISIBLE);
        setupPhotoList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent fullPhotoViewIntent = new Intent(this, PhotoFullScreenActivity.class);
        FlickrPhoto flickrPhoto = this.mFlickrPhotoList.get(position);
        Bundle fullPhotoViewBundle = new Bundle();
        fullPhotoViewBundle.putParcelable(FlickrPhoto.BUNDLE_KEY, flickrPhoto);
        fullPhotoViewIntent.putExtras(fullPhotoViewBundle);
        startActivity(fullPhotoViewIntent);
    }
}
