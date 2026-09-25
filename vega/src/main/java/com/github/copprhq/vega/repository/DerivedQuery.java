package com.github.copprhq.vega.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

public final class DerivedQuery {

    private DerivedQuery() {
    }

    public static <K, E> Object execute(Repository<K, E> repository, Method method, Object[] args) {
        String methodName = method.getName();
        if (methodName.startsWith("findBy")) {
            return findBy(repository, method, args);
        }

        throw new UnsupportedOperationException("Unsupported repository method: " + methodName);
    }

    private static <K, E> Optional<E> findBy(Repository<K, E> repository, Method method,
                                             Object[] args) {
        String property = decapitalize(method.getName().substring("findBy".length()));
        Object expected = args[0];

        for (E entity : repository.all()) {
            try {
                Field field = entity.getClass().getDeclaredField(property);
                field.setAccessible(true);

                Object actual = field.get(entity);
                if (Objects.equals(actual, expected)) {
                    return Optional.of(entity);
                }
            } catch (NoSuchFieldException ignored) {
                throw new IllegalStateException("No property '" + property +
                        "' exists on " + entity.getClass().getName());
            } catch (IllegalAccessException exception) {
                throw new IllegalStateException(exception);
            }
        }

        return Optional.empty();
    }

    private static String decapitalize(String value) {
        if (value.isEmpty()) {
            return value;
        }

        return Character.toLowerCase(value.charAt(0)) + value.substring(1);
    }
}
