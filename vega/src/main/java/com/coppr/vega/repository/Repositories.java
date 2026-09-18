package com.coppr.vega.repository;

import com.coppr.supernova.extension.Extensible;
import com.coppr.vega.repository.implementation.InMemoryRepository;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Repositories extends Extensible {

    private static final Map<Class<?>, Repository<?, ?>> repositories =
            new ConcurrentHashMap<>();

    public Repositories() {
    }

    @SuppressWarnings("unchecked")
    public static <K, E, R extends Repository<K, E>> R create(Class<R> repositoryClass) {
        if (!repositoryClass.isInterface()) {
            throw new IllegalStateException("repository must be interface");
        }

        InMemoryRepository<K, E> implementation =
                new InMemoryRepository<>();

        R repository = (R) Proxy.newProxyInstance(
                repositoryClass.getClassLoader(),
                new Class<?>[]{repositoryClass},
                (proxy, method, args) -> method.invoke(
                        implementation,
                        args
                )
        );

        repositories.put(repositoryClass, repository);

        return repository;
    }

    @SuppressWarnings("unchecked")
    public static <K, E, R extends Repository<K, E>> R repository(Class<R> repositoryClass) {
        return (R) repositories.get(repositoryClass);
    }

    public static List<Repository<?, ?>> repositories() {
        return new ArrayList<>(repositories.values());
    }

}
