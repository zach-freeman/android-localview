package com.sparkwing.localview;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zachfreeman on 2/12/17.
 */

@Singleton
@Component(modules = {LocalviewApplicationModule.class})
public interface LocationUpdaterComponent {
    void inject(PhotoListManager photoListManager);
}
