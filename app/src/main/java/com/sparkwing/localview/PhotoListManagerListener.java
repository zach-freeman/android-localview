package com.sparkwing.localview;

import com.sparkwing.localview.Models.Photo;

import java.util.List;

/**
 * Created by zachfreeman on 9/19/15.
 */
public interface PhotoListManagerListener {
    public void photoListManagerDidFinish(List<Photo> photoList);
}
