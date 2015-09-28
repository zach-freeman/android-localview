package com.sparkwing.localview;

import java.util.List;

/**
 * Created by zachfreeman on 9/19/15.
 */
public interface PhotoListManagerListener {
    public void photoListManagerDidFinish(List<FlickrPhoto> photoList);
}
