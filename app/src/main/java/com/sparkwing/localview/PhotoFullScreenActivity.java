package com.sparkwing.localview;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by zachfreeman on 9/19/15.
 */
public class PhotoFullScreenActivity extends AppCompatActivity implements ControllerListener {
    private static final String TAG = PhotoFullScreenActivity.class.getSimpleName();

    private SimpleDraweeView mFullImageView;
    private TextView mTitleTextView;
    Animation mSlideAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSlideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide);
        setContentView(R.layout.activity_photo_full_view);
        mFullImageView = (SimpleDraweeView) findViewById(R.id.fullImageView);
        mTitleTextView = (TextView)findViewById(R.id.titleText);

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

        String bigImageUrl = flickrPhoto.getBigImageUrl();
        Uri uri = Uri.parse(bigImageUrl);


        GenericDraweeHierarchyBuilder draweeHierarchyBuilder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy draweeHierarchy = draweeHierarchyBuilder
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        mFullImageView.setHierarchy(draweeHierarchy);


        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setControllerListener(this)
                .build();
        mFullImageView.setController(controller);

    }

    protected void setupTitleTextView(FlickrPhoto flickrPhoto) {

        String titleComment = flickrPhoto.getTitleComment();
        mTitleTextView.setText(titleComment);
        mTitleTextView.setGravity(Gravity.CENTER);
    }

    //region ControllerListener implementation methods
    @Override
    public void onSubmit(String s, Object o) {

    }

    @Override
    public void onFinalImageSet(
            String id,
            @Nullable Object imageInfo,
            @Nullable Animatable animatable) {
        if (imageInfo == null) {
            return;
        } else {
            mFullImageView.startAnimation(mSlideAnimation);
        }
    }

    @Override
    public void onIntermediateImageSet(String s, @Nullable Object o) {

    }

    @Override
    public void onIntermediateImageFailed(String s, Throwable throwable) {

    }

    @Override
    public void onFailure(String s, Throwable throwable) {
        Toast.makeText(this, "Photo Download Failure", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRelease(String s) {

    }
    //endregion


}


