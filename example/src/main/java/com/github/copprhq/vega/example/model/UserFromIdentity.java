package com.github.copprhq.vega.example.model;

import com.github.copprhq.vega.Identity;

import java.util.UUID;

public record UserFromIdentity(@Identity UUID identity, String username) implements User {
}
