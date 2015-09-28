package com.sparkwing.localview;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by zachfreeman on 9/19/15.
 */
public class PhotoFullScreenActivity extends AppCompatActivity {
    private static final String TAG = PhotoFullScreenActivity.class.getSimpleName();

    private SimpleDraweeView mFullImageView;
    private TextView mTitleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_full_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        FlickrPhoto flickrPhoto = bundle.getParcelable(FlickrPhoto.BUNDLE_KEY);

        setupFullImageView(flickrPhoto);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            setupTitleTextView(flickrPhoto);
        }

    }

    protected void setupFullImageView(FlickrPhoto flickrPhoto) {
        mFullImageView = (SimpleDraweeView) findViewById(R.id.fullImageView);
        GenericDraweeHierarchyBuilder draweeHierarchyBuilder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy draweeHierarchy = draweeHierarchyBuilder
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        mFullImageView.setHierarchy(draweeHierarchy);

        String bigImageUrl = flickrPhoto.getBigImageUrl();

        Uri uri = Uri.parse(bigImageUrl);
        mFullImageView.setImageURI(uri);

        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide);
        mFullImageView.startAnimation(slideAnimation);
    }

    protected void setupTitleTextView(FlickrPhoto flickrPhoto) {
        mTitleTextView = (TextView)findViewById(R.id.titleText);
        String titleComment = flickrPhoto.getTitleComment();
        mTitleTextView.setText(titleComment);
        mTitleTextView.setGravity(Gravity.CENTER);
    }
}


