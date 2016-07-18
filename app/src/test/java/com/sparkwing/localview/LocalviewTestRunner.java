package com.sparkwing.localview;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.sparkwing.localview.util.MockInjectModule;

import org.junit.runners.model.InitializationError;
import org.mockito.MockitoAnnotations;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.TestLifecycle;

import java.lang.reflect.Method;

import roboguice.RoboGuice;

/**
 * Created by zsfree00 on 5/8/16.
 */
public class LocalviewTestRunner extends RobolectricGradleTestRunner {

    public LocalviewTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    /*@Override
    protected AndroidManifest getAppManifest(Config config) {
        String manifestPath = "../app/src/main/AndroidManifest.xml";
        File file = new File(manifestPath);
        if (file.exists()) {
            int hello = 1;
        }
        String resPath = "../app/src/main/res";
        String assetPath = "../app/src/main/assets";
        AndroidManifest manifest = createAppManifest(Fs.fileFromPath(manifestPath),
                Fs.fileFromPath(resPath),
                Fs.fileFromPath(assetPath),
                "com.sparkwing.localview");
        // Robolectric is already going to look in the  'app' dir ...
        // so no need to add to package name
        return manifest;
    }*/

    @Override
    protected Class<? extends TestLifecycle> getTestLifecycleClass() {
        return TestLifeCycleWithInjection.class;
    }

    public static class TestLifeCycleWithInjection extends DefaultTestLifecycle {
        @Override
        public void prepareTest(Object test) {
            //LocalviewApplication application = (LocalviewApplication)RuntimeEnvironment.application;

            LocalviewApplicationModule applicationModule = new LocalviewApplicationModule();
            Module testModule = Modules.override(applicationModule).with(new LocalviewTestApplicationModule());

            MockInjectModule mockInjectModule = new MockInjectModule();
            MockInjectModule.MockInjectAnnotations.initInjectMocks(test.getClass(), mockInjectModule, test);
            Module testMockModule = Modules.override(testModule).with(mockInjectModule);
            RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, testMockModule);

            RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(test);
            MockitoAnnotations.initMocks(test);
        }

        @Override
        public void afterTest(Method method) {
            RoboGuice.Util.reset();
            super.afterTest(method);
        }
    }

}
