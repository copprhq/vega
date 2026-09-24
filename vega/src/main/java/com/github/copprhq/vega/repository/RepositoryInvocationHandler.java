package com.github.copprhq.vega.repository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RepositoryInvocationHandler<K, E>
        implements InvocationHandler {

    private final Repository<K, E> implementation;

    public RepositoryInvocationHandler(Repository<K, E> implementation) {
        this.implementation = implementation;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            Method implementationMethod = implementation.getClass().getMethod(
                    method.getName(),
                    method.getParameterTypes()
            );

            return implementationMethod.invoke(implementation, args);
        } catch (NoSuchMethodException ignored) {
        }

        return DerivedQuery.execute(implementation, method, args);
    }
}
