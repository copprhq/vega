package com.coppr.vega;

import com.coppr.vega.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<UUID, Entity> {

    Optional<Entity> findByName(String name);
}
