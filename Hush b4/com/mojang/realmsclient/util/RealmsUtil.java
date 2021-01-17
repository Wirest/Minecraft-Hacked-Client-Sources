// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.util;

import org.apache.logging.log4j.LogManager;
import java.net.URI;
import org.apache.logging.log4j.Logger;

public class RealmsUtil
{
    private static final Logger LOGGER;
    private static final int MINUTES = 60;
    private static final int HOURS = 3600;
    private static final int DAYS = 86400;
    
    public static void browseTo(final String uri) {
        try {
            final URI link = new URI(uri);
            final Class<?> desktopClass = Class.forName("java.awt.Desktop");
            final Object o = desktopClass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            desktopClass.getMethod("browse", URI.class).invoke(o, link);
        }
        catch (Throwable e) {
            RealmsUtil.LOGGER.error("Couldn't open link");
        }
    }
    
    public static String convertToAgePresentation(final Long timeDiff) {
        if (timeDiff < 0L) {
            return "right now";
        }
        final long timeDiffInSeconds = timeDiff / 1000L;
        if (timeDiffInSeconds < 60L) {
            return ((timeDiffInSeconds == 1L) ? "1 second" : (timeDiffInSeconds + " seconds")) + " ago";
        }
        if (timeDiffInSeconds < 3600L) {
            final long minutes = timeDiffInSeconds / 60L;
            return ((minutes == 1L) ? "1 minute" : (minutes + " minutes")) + " ago";
        }
        if (timeDiffInSeconds < 86400L) {
            final long hours = timeDiffInSeconds / 3600L;
            return ((hours == 1L) ? "1 hour" : (hours + " hours")) + " ago";
        }
        final long days = timeDiffInSeconds / 86400L;
        return ((days == 1L) ? "1 day" : (days + " days")) + " ago";
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
