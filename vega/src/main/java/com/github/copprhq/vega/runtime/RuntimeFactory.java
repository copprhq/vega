package com.github.copprhq.vega.runtime;

import com.github.copprhq.vega.application.VegaApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuntimeFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeFactory.class);

    private static Class<? extends VegaRuntime> runtimeTypeClass;

    private RuntimeFactory() {
    }

    static void setRuntimeTypeClass(Class<? extends VegaRuntime> runtimeTypeClass) {
        RuntimeFactory.runtimeTypeClass = runtimeTypeClass;
    }

    public static VegaRuntime create(VegaApplication application) {
        LOGGER.info("Creating Vega runtime...");

        if (runtimeTypeClass == null) {
            LOGGER.warn("No runtime implementation found. "
                    + "Falling back to the default no-op runtime. "
                    + "No server functionality will be provided.");
            return new DefaultVegaRuntime(application);
        }

        try {
            return runtimeTypeClass.getDeclaredConstructor(VegaApplication.class).newInstance(application);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeCreationException(exception);
        }
    }
}
