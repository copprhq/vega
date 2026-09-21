package com.coppr.vega;

import com.coppr.vega.repository.provider.InMemoryRepositoryProvider;
import com.coppr.vega.repository.provider.RepositoryProvider;

public abstract class Properties {
    
    private static Properties INSTANCE;

    public static Properties getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VegaProperties();
        }
        return INSTANCE;
    }

    public static class VegaProperties extends Properties {
    }
    
    protected Properties() {
        INSTANCE = this;
    }

    public RepositoryProvider repositoryProvider() {
        return new InMemoryRepositoryProvider();
    }

}
