package com.coppr.vega.repository;

import com.coppr.supernova.extension.Extensible;
import com.coppr.vega.Properties;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Repositories extends Extensible {

    private static final Properties properties = Properties.getInstance();

    private static final Map<Class<?>, Repository<?, ?>> repositories =
            new ConcurrentHashMap<>();

    public Repositories() {
    }

    @SuppressWarnings("unchecked")
    private static <K, E, R extends Repository<K, E>> R create(Class<R> repositoryClass) {
        if (!repositoryClass.isInterface()) {
            throw new IllegalStateException("repository must be interface");
        }

        Class<?>[] types = RepositoryTypes.resolve(repositoryClass);

        Class<K> keyType = (Class<K>) types[0];
        Class<E> entityType = (Class<E>) types[1];

        Repository<K, E> implementation = properties.repositoryProvider()
                .create(repositoryClass, keyType, entityType);

        R repository = (R) Proxy.newProxyInstance(
                repositoryClass.getClassLoader(),
                new Class<?>[]{repositoryClass},
                new RepositoryInvocationHandler<>(implementation)
        );

        repositories.put(repositoryClass, repository);

        return repository;
    }

    @SuppressWarnings("unchecked")
    public static <K, E, R extends Repository<K, E>> R get(Class<R> repositoryClass) {
        return (R) repositories.getOrDefault(repositoryClass, create(repositoryClass));
    }

    public static List<Repository<?, ?>> repositories() {
        return new ArrayList<>(repositories.values());
    }

}
