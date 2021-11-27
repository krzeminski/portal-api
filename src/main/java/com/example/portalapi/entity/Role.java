package com.example.portalapi.entity;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Role {
    USER, MODERATOR, ADMIN;

    private static final Map<String, Role> MAP = Stream.of(Role.values()).collect(Collectors.toMap(Enum::name, Function.identity()));

    public static Role fromName(String name) {
        return MAP.get(name);
    }
}