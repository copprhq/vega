package com.coppr.vega.repository;

import com.coppr.supernova.extension.Extensible;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Repositories extends Extensible {

    private static final Map<Class<?>, Repository<?, ?>> repositories =
            new ConcurrentHashMap<>();

    public Repositories() {
    }

    public static  <E, I, R extends Repository<E, I>> void addRepository(R repository) {
        repositories.put(repository.getClass(), repository);
    }

    @SuppressWarnings("unchecked")
    public static  <E, I, R extends Repository<E, I>> E repository(Class<R> repositoryClass) {
        return (E) repositories.get(repositoryClass);
    }

    public static List<Repository<?, ?>> repositories() {
        return new ArrayList<>(repositories.values());
    }

}
