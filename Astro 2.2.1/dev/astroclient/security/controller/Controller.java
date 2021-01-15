package dev.astroclient.security.controller;

import java.io.Serializable;

public abstract class Controller implements Serializable {

    protected Controller() {
        super();
    }

    public abstract void preAuth();

    public abstract boolean auth();

    public abstract boolean postAuth();

    public abstract void setup();

    public abstract void shutdown();
}