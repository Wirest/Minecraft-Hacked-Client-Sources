package shadersmod.common;

import org.apache.logging.log4j.Logger;

public abstract class SMCLog {
    private static final Logger LOGGER = ;
    private static final String PREFIX = "[Shaders] ";

    public static void severe(String paramString) {
        LOGGER.error("[Shaders] " + paramString);
    }

    public static void warning(String paramString) {
        LOGGER.warn("[Shaders] " + paramString);
    }

    public static void info(String paramString) {
        LOGGER.info("[Shaders] " + paramString);
    }

    public static void fine(String paramString) {
        LOGGER.debug("[Shaders] " + paramString);
    }

    public static void severe(String paramString, Object... paramVarArgs) {
        String str = String.format(paramString, paramVarArgs);
        LOGGER.error("[Shaders] " + str);
    }

    public static void warning(String paramString, Object... paramVarArgs) {
        String str = String.format(paramString, paramVarArgs);
        LOGGER.warn("[Shaders] " + str);
    }

    public static void info(String paramString, Object... paramVarArgs) {
        String str = String.format(paramString, paramVarArgs);
        LOGGER.info("[Shaders] " + str);
    }

    public static void fine(String paramString, Object... paramVarArgs) {
        String str = String.format(paramString, paramVarArgs);
        LOGGER.debug("[Shaders] " + str);
    }
}




