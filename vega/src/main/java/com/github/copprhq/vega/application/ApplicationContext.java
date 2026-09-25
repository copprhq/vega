package com.github.copprhq.vega.application;

import com.github.copprhq.vega.Properties;
import com.github.copprhq.vega.command.CommandHandlers;
import com.github.copprhq.vega.repository.Repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ApplicationContext(
        Properties properties, CommandHandlers commandHandlers, Repositories repositories) {

    private static final Map<Class<?>, Object> containers = new HashMap<>();

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
