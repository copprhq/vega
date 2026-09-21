package com.coppr.vega;

import com.coppr.supernova.functional.Result;
import com.coppr.vega.repository.Repository;

import java.util.List;
import java.util.Optional;

public class SimpleRepository<K, E> implements Repository<K, E> {

    @Override
    public Result<E> save(E entity) {
        return null;
    }

    @Override
    public Result<Void> delete(K key) {
        return null;
    }

    @Override
    public Optional<E> find(K key) {
        return Optional.empty();
    }

    @Override
    public Optional<E> findFirst() {
        return Optional.empty();
    }

    @Override
    public List<E> all() {
        return List.of();
    }

    @Override
    public int count() {
        return 0;
    }
}
