package com.coppr.vega.example;

import com.coppr.vega.Identity;
import lombok.Setter;

import java.util.UUID;

@Setter
public class EntityFromIdentity implements Entity {

    @Identity
    private UUID identity;
    private String username;

    public EntityFromIdentity(UUID identity, String username) {
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
