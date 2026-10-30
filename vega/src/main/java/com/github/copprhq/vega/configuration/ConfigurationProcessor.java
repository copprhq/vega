package com.github.copprhq.vega.configuration;

import com.github.copprhq.vega.application.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ConfigurationProcessor {

    private final ApplicationContext context;

    public ConfigurationProcessor(ApplicationContext context) {
        this.context = context;
    }

    public void process(Class<?> configurationClass) throws Exception {
        Object configuration = context.createBean(configurationClass);
        for (Method method : configurationClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Bean.class)) continue;
            if (method.getReturnType() == void.class) continue;
            if (Modifier.isAbstract(method.getModifiers())) continue;

            method.setAccessible(true);

            Object[] arguments = Arrays.stream(method.getParameterTypes())
                    .map(context::getBean)
                    .toArray();

            Object bean = method.invoke(configuration, arguments);

            if (bean == null) {
                throw new IllegalStateException(
                        "@Bean method returned null: " + method.getName());
            }

            context.addBean(method.getReturnType(), bean);
        }
    }
}