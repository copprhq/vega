package com.coppr.vega.repository.provider;

import com.coppr.vega.repository.Repository;
import com.coppr.vega.repository.implementation.InMemoryRepository;

public class InMemoryRepositoryProvider implements RepositoryProvider {

    @Override
    public <K, E> Repository<K, E> create(Class<?> repositoryInterface, Class<K> keyType,
                                          Class<E> entityType) {
        return new InMemoryRepository<>();
    }
}
