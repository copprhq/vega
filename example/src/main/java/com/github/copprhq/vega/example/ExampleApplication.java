package com.github.copprhq.vega.example;

import com.github.copprhq.vega.application.VegaApplication;
import com.github.copprhq.vega.example.model.UserFromIdentifiable;
import com.github.copprhq.vega.example.model.UserFromIdentity;
import com.github.copprhq.vega.example.repository.UserRepository;

import java.util.UUID;

public class ExampleApplication extends VegaApplication {

    private final UserRepository userRepository;

    public ExampleApplication(UserRepository userRepository) {
        this.userRepository = userRepository;

        userRepository.save(new UserFromIdentifiable(UUID.randomUUID(), "John Doe"));
        userRepository.save(new UserFromIdentity(UUID.randomUUID(), "Izhar Atharzi"));

        System.out.println(userRepository.all());
    }
}
