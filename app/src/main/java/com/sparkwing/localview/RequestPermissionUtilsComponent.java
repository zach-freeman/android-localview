package com.sparkwing.localview;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zachfreeman on 2/18/17.
 */
@Singleton
@Component(modules = {LocalviewApplicationModule.class})
public interface RequestPermissionUtilsComponent {
    void inject(PhotosListViewActivity photosListViewActivity);
    void inject(LocationUpdater locationUpdater);
}
