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

        TextView titleComment = (TextView) convertView.findViewById(R.id.titleComment);

        // smallImage
        Uri uri = Uri.parse(flickrPhoto.getSmallImageUrl());
        SimpleDraweeView draweeView = (SimpleDraweeView) convertView.findViewById(R.id.smallImageView);
        draweeView.setImageURI(uri);

        // titleComment
        String titleString = flickrPhoto.getTitleComment();
        if (titleString.isEmpty()) {
            titleString = "Title not available";
        }
        titleComment.setText(titleString);



        return convertView;
    }
}
