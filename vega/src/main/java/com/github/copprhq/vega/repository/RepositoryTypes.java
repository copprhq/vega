package com.github.copprhq.vega.repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class RepositoryTypes {

    private RepositoryTypes() {
    }

    public static Class<?>[] resolve(Class<?> repositoryInterface) {
        for (Type type : repositoryInterface.getGenericInterfaces()) {
            if (type instanceof ParameterizedType parameterizedType) {
                if (parameterizedType.getRawType() == Repository.class) {
                    Type[] arguments = parameterizedType.getActualTypeArguments();

                    return new Class<?>[]{
                            (Class<?>) arguments[0],
                            (Class<?>) arguments[1]
                    };
                }
            }
        }

        throw new IllegalStateException(
                "Repository must extend Repository<K, E>"
        );
    }
}
