package com.github.copprhq.vega.bootstrap;

import com.github.copprhq.vega.application.ApplicationCreationException;
import com.github.copprhq.vega.application.ApplicationFactory;
import com.github.copprhq.vega.application.VegaApplication;
import com.github.copprhq.vega.runtime.RuntimeCreationException;
import com.github.copprhq.vega.runtime.RuntimeFactory;
import com.github.copprhq.vega.runtime.VegaRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class VegaBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(VegaBootstrap.class);

    private VegaBootstrap() {
    }

    public static void main(String[] args) {
        LOGGER.info("Executing Vega bootstrap...");

        try {
            VegaApplication application = ApplicationFactory.create();

            VegaRuntime runtime = RuntimeFactory.create(application);

            Runtime.getRuntime().addShutdownHook(new Thread(
                    runtime::stop,
                    "vega-shutdown")
            );

            runtime.start();
        } catch (ApplicationCreationException exception) {
            LOGGER.error("Failure on creating Vega application", exception);
        } catch (RuntimeCreationException exception) {
            LOGGER.error("Failure on creating Vega runtime", exception);
        }
    }
}