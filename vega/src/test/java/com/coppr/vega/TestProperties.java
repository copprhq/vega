package com.coppr.vega;

import com.coppr.vega.repository.Repository;
import com.coppr.vega.repository.provider.RepositoryProvider;

public class TestProperties extends Properties {

    @Override
    public RepositoryProvider repositoryProvider() {
        return new RepositoryProvider() {
            @Override
            public <K, E> Repository<K, E> create(Class<?> repositoryInterface, Class<K> keyType, Class<E> entityType) {
                return new SimpleRepository<>();
            }
        };
    }
}
