package com.coppr.vega.repository.provider;

import com.coppr.vega.repository.Repository;

public interface RepositoryProvider {

    <K, E> Repository<K, E> create(Class<?> repositoryInterface, Class<K> keyType,
                                   Class<E> entityType);
}
