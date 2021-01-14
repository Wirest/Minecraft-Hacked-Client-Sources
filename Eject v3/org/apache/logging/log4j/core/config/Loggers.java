package org.apache.logging.log4j.core.config;

import java.util.concurrent.ConcurrentMap;

public class Loggers {
    private final ConcurrentMap<String, LoggerConfig> map;
    private final LoggerConfig root;

    public Loggers(ConcurrentMap<String, LoggerConfig> paramConcurrentMap, LoggerConfig paramLoggerConfig) {
        this.map = paramConcurrentMap;
        this.root = paramLoggerConfig;
    }

    public ConcurrentMap<String, LoggerConfig> getMap() {
        return this.map;
    }

    public LoggerConfig getRoot() {
        return this.root;
    }
}




