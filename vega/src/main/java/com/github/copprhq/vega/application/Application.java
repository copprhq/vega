package com.github.copprhq.vega.application;

public abstract class Application {

    private ApplicationContext context;

    public Application() {
    }

    public final ApplicationContext getContext() {
        return context;
    }

    public final void setContext(ApplicationContext context) {
        this.context = context;
    }
}
