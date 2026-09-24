package com.github.copprhq.vega.application;

public abstract class Application {

    private ApplicationContext context;

    public Application() {
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }
}
