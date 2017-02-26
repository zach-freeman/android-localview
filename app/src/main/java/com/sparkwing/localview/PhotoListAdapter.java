package com.sparkwing.localview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sparkwing.localview.Models.Photo;

import java.util.List;

/**
 * Created by zachfreeman on 9/12/15.
 */
public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder> {
    private List<Photo> mFlickrPhotoList;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Photo mFlickrPhoto;
        public SimpleDraweeView mSmallImageView;
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mSmallImageView = (SimpleDraweeView) view.findViewById(R.id.smallImageView);
        }

        public void setFlickrPhoto(Photo flickrPhoto) {
            mFlickrPhoto = flickrPhoto;
        }

        @Override
        public void onClick(View v) {
            Intent fullPhotoViewIntent = new Intent(v.getContext(), PhotoFullScreenActivity.class);
            Bundle fullPhotoViewBundle = new Bundle();
            fullPhotoViewBundle.putParcelable(FlickrConstants.BUNDLE_KEY, mFlickrPhoto);
            fullPhotoViewIntent.putExtras(fullPhotoViewBundle);
            v.getContext().startActivity(fullPhotoViewIntent);
        }
    }


    public PhotoListAdapter(List<Photo> flickrPhotoList) {
        this.mFlickrPhotoList = flickrPhotoList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PhotoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_list_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Photo flickrPhoto = mFlickrPhotoList.get(position);
        holder.setFlickrPhoto(flickrPhoto);
        Uri smallImageUri = Uri.parse(FlickrApiUtils.getPhotoUrl(FlickrApiUtils.FlickrPhotoSize.PhotoSizeSmallSquare75,
                flickrPhoto.getId(),
                flickrPhoto.getServer(),
                flickrPhoto.getFarm().toString(),
                flickrPhoto.getSecret()));
        holder.mSmallImageView.setImageURI(smallImageUri);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mFlickrPhotoList.size();
    }

}
