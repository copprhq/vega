package com.github.copprhq.vega.repository;

import com.coppr.supernova.extension.Extensible;
import com.github.copprhq.vega.Properties;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public class Repositories extends Extensible {

    private final Map<Class<?>, Repository<?, ?>> repositories =
            new ConcurrentHashMap<>();
    private final Properties properties;

    public Repositories(Properties properties) {
        this.properties = Objects.requireNonNull(properties);
    }

    @SuppressWarnings("unchecked")
    public <K, E, R extends Repository<K, E>> R create(Class<R> repositoryClass) {
        Objects.requireNonNull(repositoryClass);

        if (!repositoryClass.isInterface()) {
            throw new IllegalStateException("repository must be interface");
        }

        if (!Repository.class.isAssignableFrom(repositoryClass)) {
            throw new IllegalStateException("Interface " + repositoryClass.getName() + " does not extend Repository");
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
    public <K, E, R extends Repository<K, E>> R get(Class<R> repositoryClass) {
        Objects.requireNonNull(repositoryClass);
        return (R) repositories.getOrDefault(repositoryClass, create(repositoryClass));
    }

    public List<Repository<?, ?>> repositories() {
        return new ArrayList<>(repositories.values());
    }

}
