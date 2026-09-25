package com.github.copprhq.vega.application;

import com.github.copprhq.vega.Properties;
import com.github.copprhq.vega.repository.Repository;
import com.github.copprhq.vega.repository.RepositoryInvocationHandler;
import com.github.copprhq.vega.repository.RepositoryTypes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class ApplicationContext {

    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    public <T> T createBean(Class<T> beanType) {
        try {
            Constructor<?>[] constructors = beanType.getDeclaredConstructors();
            if (constructors.length != 1) {
                throw new IllegalStateException("Bean " + beanType.getName()
                                + " must have exactly one constructor");
            }

            Constructor<?> constructor = beanType.getDeclaredConstructors()[0];

            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] dependencies = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                dependencies[i] = getBean(parameterTypes[i]);
            }

            return beanType.cast(constructor.newInstance(dependencies));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <R> R createRepository(Class<R> repositoryType) {
        Objects.requireNonNull(repositoryType);

        if (!repositoryType.isInterface()) throw new IllegalStateException("repository must be interface");

        if (!Repository.class.isAssignableFrom(repositoryType)) {
            throw new IllegalStateException("Interface " + repositoryType.getName() +
                    " does not extend Repository");
        }

        Class<?>[] types = RepositoryTypes.resolve(repositoryType);

        Class<?> keyType = types[0];
        Class<?> entityType = types[1];

        Repository<?, ?> implementation = getBean(Properties.class).repositoryProvider()
                .create(repositoryType, keyType, entityType);

        return (R) Proxy.newProxyInstance(repositoryType.getClassLoader(),
                new Class<?>[]{repositoryType}, new RepositoryInvocationHandler<>(implementation));
    }

    public <T> T getBean(Class<T> beanType) {
        Objects.requireNonNull(beanType);

        Object existing = beans.get(beanType);
        if (existing != null) {
            return beanType.cast(existing);
        }

        if (Repository.class.isAssignableFrom(beanType) && beanType.isInterface()) {
            T repository = createRepository(beanType);
            beans.put(beanType, repository);
            return repository;
        }

        T bean = createBean(beanType);
        beans.put(beanType, bean);
        return bean;
    }

    public void addBean(Class<?> type, Object bean) {
        beans.put(type, bean);
    }
}
