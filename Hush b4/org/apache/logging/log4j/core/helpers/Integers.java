// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

public class Integers
{
    public static int parseInt(final String s, final int defaultValue) {
        return Strings.isEmpty(s) ? defaultValue : Integer.parseInt(s);
    }
    
    public static int parseInt(final String s) {
        return parseInt(s, 0);
    }
}
