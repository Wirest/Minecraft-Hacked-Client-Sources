// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import java.util.Locale;

public enum Level
{
    OFF(0), 
    FATAL(1), 
    ERROR(2), 
    WARN(3), 
    INFO(4), 
    DEBUG(5), 
    TRACE(6), 
    ALL(Integer.MAX_VALUE);
    
    private final int intLevel;
    
    private Level(final int val) {
        this.intLevel = val;
    }
    
    public static Level toLevel(final String sArg) {
        return toLevel(sArg, Level.DEBUG);
    }
    
    public static Level toLevel(final String name, final Level defaultLevel) {
        if (name == null) {
            return defaultLevel;
        }
        final String cleanLevel = name.toUpperCase(Locale.ENGLISH);
        for (final Level level : values()) {
            if (level.name().equals(cleanLevel)) {
                return level;
            }
        }
        return defaultLevel;
    }
    
    public boolean isAtLeastAsSpecificAs(final Level level) {
        return this.intLevel <= level.intLevel;
    }
    
    public boolean isAtLeastAsSpecificAs(final int level) {
        return this.intLevel <= level;
    }
    
    public boolean lessOrEqual(final Level level) {
        return this.intLevel <= level.intLevel;
    }
    
    public boolean lessOrEqual(final int level) {
        return this.intLevel <= level;
    }
    
    public int intLevel() {
        return this.intLevel;
    }
}
