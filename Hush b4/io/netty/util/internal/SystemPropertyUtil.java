// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.regex.Pattern;
import io.netty.util.internal.logging.InternalLogger;

public final class SystemPropertyUtil
{
    private static boolean initializedLogger;
    private static final InternalLogger logger;
    private static boolean loggedException;
    private static final Pattern INTEGER_PATTERN;
    
    public static boolean contains(final String key) {
        return get(key) != null;
    }
    
    public static String get(final String key) {
        return get(key, null);
    }
    
    public static String get(final String key, final String def) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        if (key.isEmpty()) {
            throw new IllegalArgumentException("key must not be empty.");
        }
        String value = null;
        try {
            if (System.getSecurityManager() == null) {
                value = System.getProperty(key);
            }
            else {
                value = AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction<String>() {
                    @Override
                    public String run() {
                        return System.getProperty(key);
                    }
                });
            }
        }
        catch (Exception e) {
            if (!SystemPropertyUtil.loggedException) {
                log("Unable to retrieve a system property '" + key + "'; default values will be used.", e);
                SystemPropertyUtil.loggedException = true;
            }
        }
        if (value == null) {
            return def;
        }
        return value;
    }
    
    public static boolean getBoolean(final String key, final boolean def) {
        String value = get(key);
        if (value == null) {
            return def;
        }
        value = value.trim().toLowerCase();
        if (value.isEmpty()) {
            return true;
        }
        if ("true".equals(value) || "yes".equals(value) || "1".equals(value)) {
            return true;
        }
        if ("false".equals(value) || "no".equals(value) || "0".equals(value)) {
            return false;
        }
        log("Unable to parse the boolean system property '" + key + "':" + value + " - " + "using the default value: " + def);
        return def;
    }
    
    public static int getInt(final String key, final int def) {
        String value = get(key);
        if (value == null) {
            return def;
        }
        value = value.trim().toLowerCase();
        if (SystemPropertyUtil.INTEGER_PATTERN.matcher(value).matches()) {
            try {
                return Integer.parseInt(value);
            }
            catch (Exception ex) {}
        }
        log("Unable to parse the integer system property '" + key + "':" + value + " - " + "using the default value: " + def);
        return def;
    }
    
    public static long getLong(final String key, final long def) {
        String value = get(key);
        if (value == null) {
            return def;
        }
        value = value.trim().toLowerCase();
        if (SystemPropertyUtil.INTEGER_PATTERN.matcher(value).matches()) {
            try {
                return Long.parseLong(value);
            }
            catch (Exception ex) {}
        }
        log("Unable to parse the long integer system property '" + key + "':" + value + " - " + "using the default value: " + def);
        return def;
    }
    
    private static void log(final String msg) {
        if (SystemPropertyUtil.initializedLogger) {
            SystemPropertyUtil.logger.warn(msg);
        }
        else {
            Logger.getLogger(SystemPropertyUtil.class.getName()).log(Level.WARNING, msg);
        }
    }
    
    private static void log(final String msg, final Exception e) {
        if (SystemPropertyUtil.initializedLogger) {
            SystemPropertyUtil.logger.warn(msg, e);
        }
        else {
            Logger.getLogger(SystemPropertyUtil.class.getName()).log(Level.WARNING, msg, e);
        }
    }
    
    private SystemPropertyUtil() {
    }
    
    static {
        SystemPropertyUtil.initializedLogger = false;
        logger = InternalLoggerFactory.getInstance(SystemPropertyUtil.class);
        SystemPropertyUtil.initializedLogger = true;
        INTEGER_PATTERN = Pattern.compile("-?[0-9]+");
    }
}
