package com.coppr.vega;

/**
 * Represents an environment variable as a key-value pair.
 *
 * <p>An environment consists of a key and its associated value. Instances
 * are immutable and can be created either from separate key and value
 * components or from a {@code KEY=VALUE} formatted string.</p>
 *
 * @param key   the environment variable key
 * @param value the environment variable value
 */
public record Environment(String key, String value) {

    /**
     * Creates an environment variable from a key and value.
     *
     * @param key   the environment variable key
     * @param value the environment variable value
     * @return a new environment variable
     */
    public static Environment of(String key, String value) {
        return new Environment(key, value);
    }

    /**
     * Creates an environment variable from a {@code KEY=VALUE} formatted string.
     *
     * <p>The first {@code '='} character separates the key from the value.
     * Any subsequent {@code '='} characters are considered part of the value.</p>
     *
     * <p>For example, {@code "DATABASE_URL=jdbc:mysql://localhost:3306/db"}
     * produces a key of {@code "DATABASE_URL"} and a value of
     * {@code "jdbc:mysql://localhost:3306/db"}.</p>
     *
     * @param environment the environment variable in {@code KEY=VALUE} format
     * @return a new environment variable
     * @throws IllegalStateException if the supplied string does not contain
     *                               an {@code '='} separator
     */
    public static Environment of(String environment) {
        String[] parts = environment.split("=", 2);
        if (parts.length != 2)
            throw new IllegalStateException("Environment must be KEY=VALUE format");
        return new Environment(parts[0], parts[1]);
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}