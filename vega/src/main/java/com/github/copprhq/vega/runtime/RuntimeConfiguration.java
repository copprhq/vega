package com.github.copprhq.vega.runtime;

public final class RuntimeConfiguration {

    private RuntimeConfiguration() {
    }

    public static void runtimeTypeClass(Class<? extends VegaRuntime> runtime) {
        RuntimeFactory.setRuntimeTypeClass(runtime);
    }
}
