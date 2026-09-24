package com.github.copprhq.vega.repository.provider;

import com.github.copprhq.vega.repository.Repository;

public interface RepositoryProvider {

    <K, E> Repository<K, E> create(Class<?> repositoryInterface, Class<K> keyType,
                                   Class<E> entityType);
}
