package com.coppr.vega.repository;


import com.coppr.supernova.functional.Result;

import java.util.List;
import java.util.Optional;

/**
 * Represents as generic data repository for storing, retrieving, updating,
 * and deleting entities.
 *
 * @param <K> the type of the entity identifier/key
 * @param <E> the type of the stored entity
 */
public interface Repository<K, E> {

    /**
     * Saves a new entity into the repository.
     *
     * @param entity the entity to save
     */
    Result<E> save(E entity);

    /**
     * Removes an entity from the repository.
     *
     * @param key the key to delete
     */
    Result<Void> delete(K key);

    /**
     * Finds an entity by its identifier.
     *
     * @param key the identifier of the entity
     * @return an {@link Optional} containing the entity if found,
     *         or an empty Optional if no entity exists with the given key
     */
    Optional<E> find(K key);

    Optional<E> findFirst();

    /**
     * Retrieves all entities stored in the repository.
     *
     * @return a list containing all stored entities
     */
    List<E> all();

    int count();
}
