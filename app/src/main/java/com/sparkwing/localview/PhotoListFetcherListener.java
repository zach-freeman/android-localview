package com.sparkwing.localview;

import java.util.List;

/**
 * Created by zachfreeman on 9/13/15.
 */
public interface PhotoListFetcherListener {
    public void onPhotoListFetched(List<FlickrPhoto> photoList);
}
