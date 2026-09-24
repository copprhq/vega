package com.github.copprhq.vega.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {

    private final Map<Class<?>, Object> containers = new HashMap<>();

    public <T> ApplicationContext addBean(T bean) {
        containers.put(bean.getClass(), bean);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> beanClass) {
        return (T) containers.get(beanClass);
    }

    public List<Object> getBeans() {
        return List.copyOf(containers.values());
    }

}
