package com.sparkwing.localview;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by zachfreeman on 9/12/15.
 */
public class PhotoListAdapter extends BaseAdapter {
    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<FlickrPhoto> mFlickrPhotoList;

    public PhotoListAdapter(Activity activity, List<FlickrPhoto> flickrPhotoList) {
        this.mActivity = activity;
        this.mFlickrPhotoList = flickrPhotoList;
    }

    @Override
    public int getCount() {
        return mFlickrPhotoList.size();
    }

    @Override
    public Object getItem(int location) {
        return mFlickrPhotoList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (mInflater == null) {
            mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.photo_list_row_layout, null);
        }

        // getting photo data for the row
        FlickrPhoto flickrPhoto = mFlickrPhotoList.get(position);

        // smallImage
        setupSmallImageView(flickrPhoto, convertView);

        // titleComment
        setupTitleCommentView(flickrPhoto, convertView);

        return convertView;
    }

    private void setupSmallImageView(FlickrPhoto flickrPhoto, View convertView) {
        Uri smallImageUri = Uri.parse(flickrPhoto.getSmallImageUrl());
        SimpleDraweeView smallImageView = (SimpleDraweeView) convertView.findViewById(R.id.smallImageView);
        smallImageView.setImageURI(smallImageUri);
    }

    private void setupTitleCommentView(FlickrPhoto flickrPhoto, View convertView) {
        TextView titleCommentView = (TextView) convertView.findViewById(R.id.titleComment);
        String titleString = flickrPhoto.getTitleComment();
        if (titleString.isEmpty()) {
            titleString = "Title not available";
        }
        titleCommentView.setText(titleString);

    }
}
