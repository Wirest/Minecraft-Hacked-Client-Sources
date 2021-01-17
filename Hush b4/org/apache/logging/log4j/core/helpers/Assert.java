// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

public final class Assert
{
    private Assert() {
    }
    
    public static <T> T isNotNull(final T checkMe, final String name) {
        if (checkMe == null) {
            throw new NullPointerException(name + " is null");
        }
        return checkMe;
    }
}
