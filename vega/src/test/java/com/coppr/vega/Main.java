package com.coppr.vega;

import com.coppr.vega.repository.Repositories;

import java.util.Optional;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        Properties properties = new TestProperties();
        UserRepository userRepository = Repositories.get(UserRepository.class);

        EntityFromIdentifiable entityFromIdentifiable = new EntityFromIdentifiable(
                UUID.randomUUID(),
                "John Doe"
        );
        userRepository.save(entityFromIdentifiable);

        Optional<Entity> optional = userRepository.findByName("John Doe");
        if (optional.isEmpty()) {
            System.out.println("error");
        } else {
            EntityFromIdentifiable entity = (EntityFromIdentifiable) optional.get();
            System.out.println(entity.getName());
        }

        System.out.println(userRepository.all());
    }
}
