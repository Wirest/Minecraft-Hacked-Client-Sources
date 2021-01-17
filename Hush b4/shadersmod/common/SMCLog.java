// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class SMCLog
{
    private static final Logger LOGGER;
    private static final String PREFIX = "[Shaders] ";
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static void severe(final String message) {
        SMCLog.LOGGER.error("[Shaders] " + message);
    }
    
    public static void warning(final String message) {
        SMCLog.LOGGER.warn("[Shaders] " + message);
    }
    
    public static void info(final String message) {
        SMCLog.LOGGER.info("[Shaders] " + message);
    }
    
    public static void fine(final String message) {
        SMCLog.LOGGER.debug("[Shaders] " + message);
    }
    
    public static void severe(final String format, final Object... args) {
        final String s = String.format(format, args);
        SMCLog.LOGGER.error("[Shaders] " + s);
    }
    
    public static void warning(final String format, final Object... args) {
        final String s = String.format(format, args);
        SMCLog.LOGGER.warn("[Shaders] " + s);
    }
    
    public static void info(final String format, final Object... args) {
        final String s = String.format(format, args);
        SMCLog.LOGGER.info("[Shaders] " + s);
    }
    
    public static void fine(final String format, final Object... args) {
        final String s = String.format(format, args);
        SMCLog.LOGGER.debug("[Shaders] " + s);
    }
}
