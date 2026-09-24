package com.github.copprhq.vega.repository.provider;

import com.github.copprhq.vega.repository.Repository;
import com.github.copprhq.vega.repository.implementation.InMemoryRepository;

public class InMemoryRepositoryProvider implements RepositoryProvider {

    @Override
    public <K, E> Repository<K, E> create(Class<?> repositoryInterface, Class<K> keyType,
                                          Class<E> entityType) {
        return new InMemoryRepository<>();
    }
}
