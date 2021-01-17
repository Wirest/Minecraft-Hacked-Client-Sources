// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

public final class Log
{
    private static boolean verbose;
    private static boolean forcedVerbose;
    private static final String forceVerboseProperty = "org.newdawn.slick.forceVerboseLog";
    private static final String forceVerbosePropertyOnValue = "true";
    private static LogSystem logSystem;
    
    static {
        Log.verbose = true;
        Log.forcedVerbose = false;
        Log.logSystem = new DefaultLogSystem();
    }
    
    private Log() {
    }
    
    public static void setLogSystem(final LogSystem system) {
        Log.logSystem = system;
    }
    
    public static void setVerbose(final boolean v) {
        if (Log.forcedVerbose) {
            return;
        }
        Log.verbose = v;
    }
    
    public static void checkVerboseLogSetting() {
        try {
            AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
                @Override
                public Object run() {
                    final String val = System.getProperty("org.newdawn.slick.forceVerboseLog");
                    if (val != null && val.equalsIgnoreCase("true")) {
                        Log.setForcedVerboseOn();
                    }
                    return null;
                }
            });
        }
        catch (Throwable t) {}
    }
    
    public static void setForcedVerboseOn() {
        Log.forcedVerbose = true;
        Log.verbose = true;
    }
    
    public static void error(final String message, final Throwable e) {
        Log.logSystem.error(message, e);
    }
    
    public static void error(final Throwable e) {
        Log.logSystem.error(e);
    }
    
    public static void error(final String message) {
        Log.logSystem.error(message);
    }
    
    public static void warn(final String message) {
        Log.logSystem.warn(message);
    }
    
    public static void warn(final String message, final Throwable e) {
        Log.logSystem.warn(message, e);
    }
    
    public static void info(final String message) {
        if (Log.verbose || Log.forcedVerbose) {
            Log.logSystem.info(message);
        }
    }
    
    public static void debug(final String message) {
        if (Log.verbose || Log.forcedVerbose) {
            Log.logSystem.debug(message);
        }
    }
}
