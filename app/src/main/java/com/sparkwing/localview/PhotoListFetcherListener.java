package com.sparkwing.localview;

import com.sparkwing.localview.Models.Photo;

import java.util.List;

/**
 * Created by zachfreeman on 9/13/15.
 */
public interface PhotoListFetcherListener {
    public void onPhotoListFetched(List<Photo> photoList);
}
