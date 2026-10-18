package com.github.copprhq.vega.runtime;

import com.github.copprhq.vega.application.VegaApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultVegaRuntime implements VegaRuntime {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultVegaRuntime.class);
    private final VegaApplication application;

    private RuntimeState state = RuntimeState.STOPPED;

    public DefaultVegaRuntime(VegaApplication application) {
        this.application = application;
    }

    @Override
    public VegaApplication application() {
        return application;
    }

    @Override
    public RuntimeState state() {
        return state;
    }

    @Override
    public void start() {
        if (state == RuntimeState.STARTING || state == RuntimeState.STARTED) {
            return;
        }

        LOGGER.info("Starting Vega runtime...");
        state = RuntimeState.STARTING;

        LOGGER.info("Vega runtime started");
        state = RuntimeState.STARTED;
    }

    @Override
    public void stop() {
        if (state == RuntimeState.STOPPING || state == RuntimeState.STOPPED) {
            return;
        }

        LOGGER.info("Stopping Vega runtime...");
        state = RuntimeState.STOPPING;

        LOGGER.info("Vega runtime stopped");
        state = RuntimeState.STOPPED;
    }
}
