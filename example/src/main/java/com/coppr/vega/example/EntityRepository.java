package com.coppr.vega.example;

import com.coppr.vega.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface EntityRepository extends Repository<UUID, Entity> {

    Optional<Entity> findByUsername(String username);

}
