package com.coppr.vega;

import com.coppr.vega.repository.Repositories;

import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        UserRepository userRepository = Repositories.create(UserRepository.class);

        EntityFromIdentifiable entityFromIdentifiable = new EntityFromIdentifiable(
                UUID.randomUUID(),
                "John Doe"
        );
        userRepository.save(entityFromIdentifiable);

        System.out.println(userRepository.all());
    }
}
