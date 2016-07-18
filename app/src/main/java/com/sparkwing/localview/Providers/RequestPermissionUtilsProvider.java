package com.sparkwing.localview.Providers;

import com.google.inject.Provider;
import com.sparkwing.localview.RequestPermissionUtils;

/**
 * Created by zachfreeman on 7/17/16.
 */
public class RequestPermissionUtilsProvider implements Provider<RequestPermissionUtils> {

    @Override
    public RequestPermissionUtils get() {
        return new RequestPermissionUtils();
    }
}
