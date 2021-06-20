package com.sparkwing.localview;

import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sparkwing.localview.Models.Photo;

/**
 * Created by zachfreeman on 9/19/15.
 */
public class PhotoFullScreenActivity extends AppCompatActivity implements ControllerListener {
    private static final String TAG = PhotoFullScreenActivity.class.getSimpleName();
    private static final String SAVE_PHOTO_KEY = "photo";

    private SimpleDraweeView mFullImageView;
    private TextView mTitleTextView;
    Animation mSlideAnimation;
    Photo mFlickrPhoto;

    private boolean mShouldAnimateImage;
    private boolean mIsTabletSize;

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
        mFlickrPhoto = bundle.getParcelable(FlickrConstants.BUNDLE_KEY);

        mShouldAnimateImage = true;
        mIsTabletSize = getResources().getBoolean(R.bool.isTablet);

        setupFullImageView(mFlickrPhoto);

        if (mIsTabletSize) {
            setupTitleTextView(mFlickrPhoto);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        // onDestroy gets called when back button is tapped
        super.onDestroy();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SAVE_PHOTO_KEY, mFlickrPhoto);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(SAVE_PHOTO_KEY)) {
            this.mFlickrPhoto = savedInstanceState.getParcelable(SAVE_PHOTO_KEY);
            mShouldAnimateImage = false;
        }
    }

    protected void setupFullImageView(Photo flickrPhoto) {
        String bigImageUrl = FlickrApiUtils.getPhotoUrl(FlickrApiUtils.FlickrPhotoSize.PhotoSizeLarge1024,
                flickrPhoto.getId(),
                flickrPhoto.getServer(),
                flickrPhoto.getFarm().toString(),
                flickrPhoto.getSecret());
        Uri uri = Uri.parse(bigImageUrl);
        PointF focusPoint = new PointF(0.0f, 0.0f);
        ScalingUtils.ScaleType actualImageScaleType = ScalingUtils.ScaleType.CENTER;
        if (mIsTabletSize) {
            actualImageScaleType = ScalingUtils.ScaleType.FOCUS_CROP;
            focusPoint = new PointF(0.0f, 0.5f);
        }

        GenericDraweeHierarchyBuilder draweeHierarchyBuilder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy draweeHierarchy = draweeHierarchyBuilder
                .setProgressBarImage(new ProgressBarDrawable())
                .setActualImageScaleType(actualImageScaleType)
                .setActualImageFocusPoint(focusPoint)
                .build();
        mFullImageView.setHierarchy(draweeHierarchy);


        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setControllerListener(this)
                .build();
        mFullImageView.setController(controller);

    }

    protected void setupTitleTextView(Photo flickrPhoto) {

        String titleComment = flickrPhoto.getTitle();
        if (titleComment.isEmpty() || null == titleComment) {
            titleComment = "No title available";
        }
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
            if (mShouldAnimateImage == true) {
                mFullImageView.startAnimation(mSlideAnimation);
            }
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


