// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.status.StatusLogger;

public final class ClockFactory
{
    public static final String PROPERTY_NAME = "log4j.Clock";
    private static final StatusLogger LOGGER;
    
    private ClockFactory() {
    }
    
    public static Clock getClock() {
        return createClock();
    }
    
    private static Clock createClock() {
        final String userRequest = System.getProperty("log4j.Clock");
        if (userRequest == null || "SystemClock".equals(userRequest)) {
            ClockFactory.LOGGER.debug("Using default SystemClock for timestamps");
            return new SystemClock();
        }
        if (CachedClock.class.getName().equals(userRequest) || "CachedClock".equals(userRequest)) {
            ClockFactory.LOGGER.debug("Using specified CachedClock for timestamps");
            return CachedClock.instance();
        }
        if (CoarseCachedClock.class.getName().equals(userRequest) || "CoarseCachedClock".equals(userRequest)) {
            ClockFactory.LOGGER.debug("Using specified CoarseCachedClock for timestamps");
            return CoarseCachedClock.instance();
        }
        try {
            final Clock result = (Clock)Class.forName(userRequest).newInstance();
            ClockFactory.LOGGER.debug("Using {} for timestamps", userRequest);
            return result;
        }
        catch (Exception e) {
            final String fmt = "Could not create {}: {}, using default SystemClock for timestamps";
            ClockFactory.LOGGER.error("Could not create {}: {}, using default SystemClock for timestamps", userRequest, e);
            return new SystemClock();
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
