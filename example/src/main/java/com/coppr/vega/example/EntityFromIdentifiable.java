package com.coppr.vega.example;

import com.coppr.vega.Identifiable;
import lombok.Setter;

import java.util.UUID;

@Setter
public class EntityFromIdentifiable implements Entity, Identifiable<UUID> {

    private UUID identity;
    private String username;

    public EntityFromIdentifiable(UUID identity, String username) {
        this.identity = identity;
        this.username = username;
    }

    @Override
    public UUID identity() {
        return identity;
    }

    @Override
    public String username() {
        return username;
    }

}
