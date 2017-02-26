package com.sparkwing.localview;

import android.support.annotation.NonNull;

import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

/**
 * Created by zsfree00 on 5/8/16.
 */
public class LocalviewTestRunner extends RobolectricGradleTestRunner {

    private static final int SDK_EMULATE_LEVEL = 21;

    public LocalviewTestRunner(@NonNull Class<?> clazz) throws Exception {
        super(clazz);
    }

    @Override
    public Config getConfig(@NonNull Method method) {
        final Config defaultConfig = super.getConfig(method);
        return new Config.Implementation(
                new int[]{SDK_EMULATE_LEVEL},
                defaultConfig.manifest(),
                defaultConfig.qualifiers(),
                defaultConfig.packageName(),
                defaultConfig.resourceDir(),
                defaultConfig.assetDir(),
                defaultConfig.shadows(),
                LocalviewTestApplication.class, // Here is the trick, we change application class to one with mocks.
                defaultConfig.libraries(),
                defaultConfig.constants() == Void.class ? BuildConfig.class : defaultConfig.constants()
        );
    }

}
