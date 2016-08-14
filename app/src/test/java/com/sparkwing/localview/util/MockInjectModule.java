package com.sparkwing.localview.util;

import com.sparkwing.localview.LocalviewApplicationModule;

import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zachfreeman on 5/9/16.
 */
public class MockInjectModule extends LocalviewApplicationModule {
    Map<Class, Object> bindings = new HashMap<Class, Object>();
    @Override
    protected void configure() {
        for (Class mockClass : bindings.keySet()) {
            bind(mockClass).toInstance(bindings.get(mockClass));
        }
    }

    public <T> void addMockBinding(Class<T> mockClass, T mock) { bindings.put(mockClass, mock); }

    public static class MockInjectAnnotations {
        public static void initInjectMocks(Class testClass, MockInjectModule mockInjectModule, Object test) {
            Field[] fields = testClass.getDeclaredFields();
            for (Field field : fields) {
                for (Annotation annotation : field.getAnnotations()) {
                    if (annotation.annotationType() == MockInject.class) {
                        addMockToModule(field, mockInjectModule, test);
                    }
                }
            }
        }

        private static <T extends Object> void addMockToModule(Field field, MockInjectModule mockInjectModule, Object test) {
            Class<T> type = (Class) field.getType();
            T mock = Mockito.mock(type, "@MockInject(" + type.getSimpleName() + ")");
            mockInjectModule.addMockBinding(type, mock);
            try {
                new FieldSetter(test, field).set(mock);

            } catch (Exception ex) {
                throw new MockitoException("Error setting field " + field.getName() + " annotated with " + MockInject.class, ex);
            }
        }
    }
}
