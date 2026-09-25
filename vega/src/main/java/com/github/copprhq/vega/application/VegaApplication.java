package com.github.copprhq.vega.application;

public abstract class VegaApplication {

    private ApplicationContext applicationContext;

    public final ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public final void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
