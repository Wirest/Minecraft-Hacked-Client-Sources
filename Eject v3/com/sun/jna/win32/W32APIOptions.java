package com.sun.jna.win32;

import java.util.HashMap;
import java.util.Map;

public abstract interface W32APIOptions
        extends StdCallLibrary {
    public static final Map UNICODE_OPTIONS = new HashMap() {
    };
    public static final Map ASCII_OPTIONS = new HashMap() {
    };
    public static final Map DEFAULT_OPTIONS = Boolean.getBoolean("w32.ascii") ? ASCII_OPTIONS : UNICODE_OPTIONS;
}




