package com.github.copprhq.vega.example.repository;

import com.github.copprhq.vega.example.model.User;
import com.github.copprhq.vega.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<UUID, User> {

    Optional<User> findByUsername(String username);

}
