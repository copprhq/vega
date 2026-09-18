package com.coppr.vega;

import java.util.UUID;

public class EntityFromIdentifiable implements Entity, Identifiable<UUID> {

    private final UUID uuid;
    private final String name;

    public EntityFromIdentifiable(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public UUID identity() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "EntityFromIdentifiable{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                '}';
    }
}
