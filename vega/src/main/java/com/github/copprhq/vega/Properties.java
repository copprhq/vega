package com.github.copprhq.vega;

import com.github.copprhq.vega.repository.provider.InMemoryRepositoryProvider;
import com.github.copprhq.vega.repository.provider.RepositoryProvider;

public abstract class Properties {

    public Properties() {
    }

    public RepositoryProvider repositoryProvider() {
        return new InMemoryRepositoryProvider();
    }

    public static class VegaProperties extends Properties {
    }

}
