package com.github.copprhq.vega.configuration;

import com.github.copprhq.vega.repository.provider.InMemoryRepositoryProvider;
import com.github.copprhq.vega.repository.provider.RepositoryProvider;

/**
 * This configuration is for when developer don't want to initialize some bean by themselves.
 */
@Configuration
public class DefaultConfiguration {

    @Bean
    public RepositoryProvider repositoryProvider() {
        return new InMemoryRepositoryProvider();
    }

}
