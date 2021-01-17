// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.util.VersionInfo;

public final class ICUDebug
{
    private static String params;
    private static boolean debug;
    private static boolean help;
    public static final String javaVersionString;
    public static final boolean isJDK14OrHigher;
    public static final VersionInfo javaVersion;
    
    public static VersionInfo getInstanceLenient(final String s) {
        final int[] ver = new int[4];
        boolean numeric = false;
        int i = 0;
        int vidx = 0;
        while (i < s.length()) {
            final char c = s.charAt(i++);
            if (c < '0' || c > '9') {
                if (!numeric) {
                    continue;
                }
                if (vidx == 3) {
                    break;
                }
                numeric = false;
                ++vidx;
            }
            else if (numeric) {
                ver[vidx] = ver[vidx] * 10 + (c - '0');
                if (ver[vidx] > 255) {
                    ver[vidx] = 0;
                    break;
                }
                continue;
            }
            else {
                numeric = true;
                ver[vidx] = c - '0';
            }
        }
        return VersionInfo.getInstance(ver[0], ver[1], ver[2], ver[3]);
    }
    
    public static boolean enabled() {
        return ICUDebug.debug;
    }
    
    public static boolean enabled(final String arg) {
        if (ICUDebug.debug) {
            final boolean result = ICUDebug.params.indexOf(arg) != -1;
            if (ICUDebug.help) {
                System.out.println("\nICUDebug.enabled(" + arg + ") = " + result);
            }
            return result;
        }
        return false;
    }
    
    public static String value(final String arg) {
        String result = "false";
        if (ICUDebug.debug) {
            int index = ICUDebug.params.indexOf(arg);
            if (index != -1) {
                index += arg.length();
                if (ICUDebug.params.length() > index && ICUDebug.params.charAt(index) == '=') {
                    ++index;
                    final int limit = ICUDebug.params.indexOf(",", index);
                    result = ICUDebug.params.substring(index, (limit == -1) ? ICUDebug.params.length() : limit);
                }
                else {
                    result = "true";
                }
            }
            if (ICUDebug.help) {
                System.out.println("\nICUDebug.value(" + arg + ") = " + result);
            }
        }
        return result;
    }
    
    static {
        try {
            ICUDebug.params = System.getProperty("ICUDebug");
        }
        catch (SecurityException ex) {}
        ICUDebug.debug = (ICUDebug.params != null);
        ICUDebug.help = (ICUDebug.debug && (ICUDebug.params.equals("") || ICUDebug.params.indexOf("help") != -1));
        if (ICUDebug.debug) {
            System.out.println("\nICUDebug=" + ICUDebug.params);
        }
        javaVersionString = System.getProperty("java.version", "0");
        javaVersion = getInstanceLenient(ICUDebug.javaVersionString);
        final VersionInfo java14Version = VersionInfo.getInstance("1.4.0");
        isJDK14OrHigher = (ICUDebug.javaVersion.compareTo(java14Version) >= 0);
    }
}
