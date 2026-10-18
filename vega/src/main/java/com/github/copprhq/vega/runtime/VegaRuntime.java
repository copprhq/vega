package com.github.copprhq.vega.runtime;

import com.github.copprhq.vega.application.VegaApplication;

public interface VegaRuntime {

    VegaApplication application();

    RuntimeState state();

    void start();

    void stop();

}
