package com.sparkwing.localview;

import org.junit.runners.model.InitializationError;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;

/**
 * Created by zsfree00 on 5/8/16.
 */
public class LocalviewTestRunner extends RobolectricGradleTestRunner {

    public LocalviewTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }
}
