package com.github.copprhq.vega.example.model;

import com.github.copprhq.vega.Identifiable;

import java.util.UUID;

public record UserFromIdentifiable(UUID identity, String username) implements User, Identifiable<UUID> {
}
