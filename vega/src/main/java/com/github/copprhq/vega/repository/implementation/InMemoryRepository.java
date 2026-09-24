package com.github.copprhq.vega.repository.implementation;

import com.coppr.supernova.functional.Result;
import com.github.copprhq.vega.Identifiable;
import com.github.copprhq.vega.Identity;
import com.github.copprhq.vega.IdentityMissingException;
import com.github.copprhq.vega.repository.Repository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository<K, E> implements Repository<K, E> {

    private final Map<K, E> entities = new ConcurrentHashMap<>();

    public InMemoryRepository() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public Result<E> save(E entity) {
        Objects.requireNonNull(entity);

        K key = null;

        IdentityMissingException identityMissingException = new IdentityMissingException(
                entity
        );

        if (Identifiable.class.isAssignableFrom(entity.getClass())) {
            key = (K) ((Identifiable<?>) entity).identity();
        } else {
            for (Field field : entity.getClass().getFields()) {
                if (field.isAnnotationPresent(Identity.class)) {
                    try {
                        key = (K) field.get(entity);
                    } catch (IllegalAccessException exception) {
                        return Result.interrupted(exception);
                    }
                } else {
                    return Result.interrupted(identityMissingException);
                }
            }
        }

        if (key == null) {
            return Result.interrupted(identityMissingException);
        }

        entities.put(key, entity);
        return Result.successful(entity);
    }

    @Override
    public Result<Void> delete(K key) {
        Objects.requireNonNull(key);
        entities.remove(key);
        return Result.successful();
    }

    @Override
    public Optional<E> find(K key) {
        Objects.requireNonNull(key);
        return Optional.ofNullable(entities.get(key));
    }

    @Override
    public Optional<E> findFirst() {
        return entities.values().stream().findFirst();
    }

    @Override
    public List<E> all() {
        return List.copyOf(entities.values());
    }

    @Override
    public int count() {
        return entities.size();
    }
}
